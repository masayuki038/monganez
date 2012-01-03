package net.wrap_trap.monganez;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.IsNull.*;
import static net.wrap_trap.monganez.BSONObjectMapper.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BSONObject;
import org.junit.Before;
import org.junit.Test;

public class EncodeIterableTest {

	private BSONObjectMapper encoder;
	
	@Before
	public void setUp(){
		encoder = new BSONObjectMapper();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		list.add(null);

		Object object = encoder.encode(list);
		assertThat(object, is(BSONObject.class));
		BSONObject bson = (BSONObject)object;
		List<Object> encodedList = (List<Object>)bson.get(COLLECTION_VALUE);

		assertThat(encodedList.size(), is(1));
		assertThat(encodedList.get(0), is(nullValue()));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<String> list = new ArrayList<String>();
		list.add("bar");
		list.add("");

		Object object = encoder.encode(list);
		assertThat(object, is(BSONObject.class));
		BSONObject bson = (BSONObject)object;
		List<Object> encodedList = (List<Object>)bson.get(COLLECTION_VALUE);

		assertThat(encodedList.size(), is(2));
		assertThat((String)encodedList.get(0), is("bar"));
		assertThat((String)encodedList.get(1), is(""));
	}
	
	@SuppressWarnings("unchecked")
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

		Object object = encoder.encode(list);
		assertThat(object, is(BSONObject.class));
		BSONObject bson = (BSONObject)object;
		List<Object> encodedList = (List<Object>)bson.get(COLLECTION_VALUE);

		assertThat(encodedList.size(), is(8));
		assertThat((Short)encodedList.get(0), is(Short.MAX_VALUE));
		assertThat((Byte)encodedList.get(1), is(Byte.MAX_VALUE));
		assertThat((Integer)encodedList.get(2), is(Integer.MAX_VALUE));
		assertThat((Long)encodedList.get(3), is(Long.MAX_VALUE));
		assertThat((Double)encodedList.get(4), is(Double.MAX_VALUE));
		assertThat((Float)encodedList.get(5), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)encodedList.get(6)).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)encodedList.get(7)).doubleValue(), is(Double.MAX_VALUE));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		list.add(new Object[]{"abc", 1});
		
		Object object = encoder.encode(list);
		assertThat(object, is(BSONObject.class));
		BSONObject bson = (BSONObject)object;
		List<Object> encodedList = (List<Object>)bson.get(COLLECTION_VALUE);

		assertThat(encodedList.size(), is(1));
		Object[] array = (Object[])encodedList.get(0);
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		List<Integer> nestedList = new ArrayList<Integer>();
		nestedList.add(1);
		nestedList.add(2);
		list.add(nestedList);

		Object object = encoder.encode(list);
		assertThat(object, is(BSONObject.class));
		BSONObject bson = (BSONObject)object;
		List<Object> encodedList = (List<Object>)bson.get(COLLECTION_VALUE);

		assertThat(encodedList.get(0), instanceOf(BSONObject.class));
		BSONObject listObject = (BSONObject)encodedList.get(0);
		assertThat(listObject.containsField(COLLECTION_CLASS_NAME), is(true));
		assertThat((String)listObject.get(COLLECTION_CLASS_NAME), is(java.util.ArrayList.class.getName()));
		assertThat(listObject.containsField(COLLECTION_VALUE), is(true));
		
		assertThat(listObject.get(COLLECTION_VALUE), instanceOf(List.class));
		List<Object> encodedNestedList = (List<Object>)listObject.get(COLLECTION_VALUE);
		assertThat((Integer)encodedNestedList.get(0), is(1));
		assertThat((Integer)encodedNestedList.get(1), is(2));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<EntityObject> list = new ArrayList<EntityObject>();
		Date now = new Date();
		EntityObject entityObject = new EntityObject();
		entityObject.setId(now.getTime());
		entityObject.setStringValue("foo");
		entityObject.setCreated(now);
		list.add(entityObject);

		Object object = encoder.encode(list);
		assertThat(object, is(BSONObject.class));
		BSONObject bson = (BSONObject)object;
		List<Object> encodedist = (List<Object>)bson.get(COLLECTION_VALUE);

		assertThat(encodedist.get(0), instanceOf(BSONObject.class));
		BSONObject encodedEntityObject = (BSONObject)encodedist.get(0);
		assertThat(encodedEntityObject.containsField(CLASS_NAME), is(true));
		assertThat((String)encodedEntityObject.get(CLASS_NAME), is(EntityObject.class.getName()));
		assertThat((Long)encodedEntityObject.get("id"), is(now.getTime()));
		assertThat((String)encodedEntityObject.get("stringValue"), is("foo"));
		assertThat((Date)encodedEntityObject.get("created"), is(now));
	}
}
