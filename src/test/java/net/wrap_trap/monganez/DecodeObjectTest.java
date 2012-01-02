package net.wrap_trap.monganez;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class DecodeObjectTest {
	private BSONObjectMapper decoder;
	
	@Before
	public void setUp(){
		decoder = new BSONObjectMapper();
	}

	@Test
	public void testNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("nullValue", null);
		dbObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	
		EntityObject entityObject = (EntityObject)decoder.toObject(dbObject);
		assertThat(entityObject.getNullValue(), is(nullValue()));
	}
	
	@Test
	public void testStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("stringValue", "bar");
		dbObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	
		EntityObject entityObject = (EntityObject)decoder.toObject(dbObject);
		assertThat(entityObject.getStringValue(), is("bar"));				
	}
	
	@Test
	public void testNumberValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("shortValue", Short.MAX_VALUE);
		dbObject.put("byteValue", Byte.MAX_VALUE);
		dbObject.put("integerValue", Integer.MAX_VALUE);
		dbObject.put("longValue", Long.MAX_VALUE);
		dbObject.put("doubleValue", Double.MAX_VALUE);
		dbObject.put("floatValue", Float.MAX_VALUE);
		dbObject.put("longDecimalValue", BigDecimal.valueOf(Long.MAX_VALUE));
		dbObject.put("doubleDecimalValue", BigDecimal.valueOf(Double.MAX_VALUE));

		dbObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	
		EntityObject entityObject = (EntityObject)decoder.toObject(dbObject);

		assertThat(entityObject.getShortValue(), is(Short.MAX_VALUE));
		assertThat(entityObject.getByteValue(), is(Byte.MAX_VALUE));
		assertThat(entityObject.getIntegerValue(), is(Integer.MAX_VALUE));
		assertThat(entityObject.getLongValue(), is(Long.MAX_VALUE));
		assertThat(entityObject.getDoubleValue(), is(Double.MAX_VALUE));
		assertThat(entityObject.getFloatValue(), is(Float.MAX_VALUE));
		assertThat(entityObject.getLongDecimalValue().longValue(), is(Long.MAX_VALUE));
		assertThat(entityObject.getDoubleDecimalValue().doubleValue(), is(Double.MAX_VALUE));
	}
	
	@Test
	public void testArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("objectArray", new Object[]{"abc", 1});
		dbObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	
		EntityObject entityObject = (EntityObject)decoder.toObject(dbObject);

		assertThat(dbObject.get("objectArray"), instanceOf(Object[].class));
		Object[] array = (Object[])entityObject.getObjectArray();
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBObject dbObject = new BasicDBObject();
		BasicDBList dbList = new BasicDBList();
		dbList.add(1);
		dbList.add(2);
		BasicDBObject dbNestedObject = new BasicDBObject();
		dbNestedObject.put(BSONObjectMapper.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbNestedObject.put(BSONObjectMapper.COLLECTION_VALUE, dbList);		
		dbObject.put("integerList", dbNestedObject);
		dbObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	

		EntityObject entityObject = (EntityObject)decoder.toObject(dbObject);

		List list = entityObject.getIntegerList();
		assertThat((Integer)list.get(0), is(1));
		assertThat((Integer)list.get(1), is(2));
	}
	
	@Test
	public void testObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBObject dbObject = new BasicDBObject();
		BasicDBObject dbNestedObject = new BasicDBObject();
		Date now = new Date();
		dbNestedObject.put("id", now.getTime());
		dbNestedObject.put("stringValue", "foo");
		dbNestedObject.put("created", now);
		dbNestedObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	
		dbObject.put("entity", dbNestedObject);
		dbObject.put(BSONObjectMapper.CLASS_NAME, "net.wrap_trap.monganez.EntityObject");	
		
		EntityObject entityObject = (EntityObject)decoder.toObject(dbObject);
		
		EntityObject entityObject2 = entityObject.getEntity();
		
		assertThat(entityObject2.getId(), is(now.getTime()));
		assertThat(entityObject2.getStringValue(), is("foo"));
		assertThat(entityObject2.getCreated(), is(now));
	}
}
