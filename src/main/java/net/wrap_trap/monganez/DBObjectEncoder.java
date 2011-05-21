package net.wrap_trap.monganez;

import static net.wrap_trap.monganez.DBObjectConstants.CLASS_NAME;
import static net.wrap_trap.monganez.DBObjectConstants.COLLECTION_CLASS_NAME;
import static net.wrap_trap.monganez.DBObjectConstants.COLLECTION_VALUE;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.bson.BSONObject;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.ObjectId;
import org.bson.types.Symbol;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DBObjectEncoder {
	
	private Map<Object, DBObject> cached = new HashMap<Object, DBObject>();

	@SuppressWarnings("unchecked")
	public DBObject encode(Object target) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(target instanceof Map){
			return encodeMap((Map)target);	
		}else if(target instanceof Iterable){
			return encodeIterable((Iterable)target);
		}else if(target instanceof Serializable){
			return encodeSerializable((Serializable)target);
		}
		throw new RuntimeException(target.getClass().getName() + " is not (java.util.Map||java.util.Iterable||java.io.Serializable).");
	}

	@SuppressWarnings("unchecked")
	protected DBObject encodeMap(Map map) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(cached.containsKey(map)){
			return null;
		}
		
		DBObject ret = new BasicDBObject();
		for(Object key : map.keySet()){
			Object target = map.get(key);
			if(isAcceptableValue(target)){
				ret.put(key.toString(), target);
			}else{
				ret.put(key.toString(), encode(target));
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	protected DBObject encodeIterable(Iterable target) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(cached.containsKey(target)){
			return null;
		}

		BasicDBList list = new BasicDBList();
		for(Object object : (Iterable)target){
			if(isAcceptableValue(object)){
				list.add(object);
			}else{
				list.add(encode(object));
			}
		}
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put(COLLECTION_VALUE, list);
		dbObject.put(COLLECTION_CLASS_NAME, target.getClass().getName());
		cached.put(target, dbObject);
		return dbObject;
	}

	@SuppressWarnings("unchecked")
	protected DBObject encodeSerializable(Serializable object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(cached.containsKey(object)){
			return null;
		}
		Map nestedMap = PropertyUtils.describe(object);
		nestedMap.put(CLASS_NAME, object.getClass().getName());
		return encodeMap(nestedMap);	
	}
	
	protected boolean isAcceptableValue(Object val){
        return (
    			val == null
    			|| val instanceof Date
    			|| val instanceof Number
    			|| val instanceof String
    			|| val instanceof ObjectId
    			|| val instanceof BSONObject
    			|| val instanceof Boolean
    			|| val instanceof Pattern
    			|| val instanceof byte[]
    			|| val instanceof Binary
    			|| val instanceof UUID
    			|| val.getClass().isArray()
    			|| val instanceof Symbol
    			|| val instanceof BSONTimestamp
    			|| val instanceof CodeWScope
    			|| val instanceof Code
    	);
	}
}

