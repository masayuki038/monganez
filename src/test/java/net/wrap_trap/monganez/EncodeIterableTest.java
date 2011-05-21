package net.wrap_trap.monganez;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.IsNull.*;
import static net.wrap_trap.monganez.DBObjectConstants.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class EncodeIterableTest {

	private DBObjectEncoder encoder;
	
	@Before
	public void setUp(){
		encoder = new DBObjectEncoder();
	}
	
	@Test
	public void testNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		list.add(null);

		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);

		assertThat(dbList.size(), is(1));
		assertThat(dbList.get(0), is(nullValue()));
	}
	
	@Test
	public void testStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<String> list = new ArrayList<String>();
		list.add("bar");
		list.add("");

		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);

		assertThat(dbList.size(), is(2));
		assertThat((String)dbList.get(0), is("bar"));
		assertThat((String)dbList.get(1), is(""));
	}
	
	@Test
	public void testNumber() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		list.add(Short.MAX_VALUE);
		list.add(Byte.MAX_VALUE);
		list.add(Integer.MAX_VALUE);
		list.add(Long.MAX_VALUE);
		list.add(Double.MAX_VALUE);
		list.add(Float.MAX_VALUE);
		list.add(BigDecimal.valueOf(Long.MAX_VALUE));
		list.add(BigDecimal.valueOf(Double.MAX_VALUE));

		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);

		assertThat(dbList.size(), is(8));
		assertThat((Short)dbList.get(0), is(Short.MAX_VALUE));
		assertThat((Byte)dbList.get(1), is(Byte.MAX_VALUE));
		assertThat((Integer)dbList.get(2), is(Integer.MAX_VALUE));
		assertThat((Long)dbList.get(3), is(Long.MAX_VALUE));
		assertThat((Double)dbList.get(4), is(Double.MAX_VALUE));
		assertThat((Float)dbList.get(5), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)dbList.get(6)).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)dbList.get(7)).doubleValue(), is(Double.MAX_VALUE));
	}
	
	@Test
	public void testArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		list.add(new Object[]{"abc", 1});
		
		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);

		assertThat(dbList.size(), is(1));
		Object[] array = (Object[])dbList.get(0);
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@Test
	public void testCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		List<Integer> nestedList = new ArrayList<Integer>();
		nestedList.add(1);
		nestedList.add(2);
		list.add(nestedList);

		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);

		assertThat(dbList.get(0), instanceOf(DBObject.class));
		DBObject listObject = (DBObject)dbList.get(0);
		assertThat(listObject.containsField(DBObjectConstants.COLLECTION_CLASS_NAME), is(true));
		assertThat((String)listObject.get(DBObjectConstants.COLLECTION_CLASS_NAME), is(java.util.ArrayList.class.getName()));
		assertThat(listObject.containsField(DBObjectConstants.COLLECTION_VALUE), is(true));
		
		assertThat(listObject.get(DBObjectConstants.COLLECTION_VALUE), instanceOf(BasicDBList.class));
		BasicDBList dbNestedList = (BasicDBList)listObject.get(DBObjectConstants.COLLECTION_VALUE);
		assertThat((Integer)dbNestedList.get(0), is(1));
		assertThat((Integer)dbNestedList.get(1), is(2));
	}
	
	@Test
	public void testObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<EntityObject> list = new ArrayList<EntityObject>();
		Date now = new Date();
		EntityObject entityObject = new EntityObject();
		entityObject.setId(now.getTime());
		entityObject.setStringValue("foo");
		entityObject.setCreated(now);
		list.add(entityObject);

		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);

		assertThat(dbList.get(0), instanceOf(DBObject.class));
		DBObject dbEntityObject = (DBObject)dbList.get(0);
		assertThat(dbEntityObject.containsField(DBObjectConstants.CLASS_NAME), is(true));
		assertThat((String)dbEntityObject.get(DBObjectConstants.CLASS_NAME), is(EntityObject.class.getName()));
		assertThat((Long)dbEntityObject.get("id"), is(now.getTime()));
		assertThat((String)dbEntityObject.get("stringValue"), is("foo"));
		assertThat((Date)dbEntityObject.get("created"), is(now));
	}
}
