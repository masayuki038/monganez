package net.wrap_trap.monganez;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.IsNull.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BSONObject;
import org.junit.Before;
import org.junit.Test;

public class EncodeMapTest {

	private BSONObjectMapper encoder;
	
	@Before
	public void setUp(){
		encoder = new BSONObjectMapper();
	}
	
	@Test
	public void testNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("null", null);
		BSONObject object = encoder.encode(map);
		assertThat(object.get("null"), is(nullValue()));
	}
	
	@Test
	public void testStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("foo", "bar");
		map.put("empty", "");
		BSONObject object = encoder.encode(map);
		assertThat((String)object.get("foo"), is("bar"));
		assertThat((String)object.get("empty"), is(""));
	}

	@Test
	public void testNumber() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Number> map = new HashMap<String, Number>();
		map.put("short", Short.MAX_VALUE);
		map.put("byte", Byte.MAX_VALUE);
		map.put("int", Integer.MAX_VALUE);
		map.put("long", Long.MAX_VALUE);
		map.put("double", Double.MAX_VALUE);
		map.put("float", Float.MAX_VALUE);
		map.put("decimal(long)", BigDecimal.valueOf(Long.MAX_VALUE));
		map.put("decimal(double)", BigDecimal.valueOf(Double.MAX_VALUE));

		BSONObject object = encoder.encode(map);
		assertThat((Short)object.get("short"), is(Short.MAX_VALUE));
		assertThat((Byte)object.get("byte"), is(Byte.MAX_VALUE));
		assertThat((Integer)object.get("int"), is(Integer.MAX_VALUE));
		assertThat((Long)object.get("long"), is(Long.MAX_VALUE));
		assertThat((Double)object.get("double"), is(Double.MAX_VALUE));
		assertThat((Float)object.get("float"), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)object.get("decimal(long)")).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)object.get("decimal(double)")).doubleValue(), is(Double.MAX_VALUE));
	}

	@Test
	public void testArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Object[]> map = new HashMap<String, Object[]>();
		map.put("2 elements", new Object[]{"abc", 1});
		BSONObject object = encoder.encode(map);
		assertThat(object.get("2 elements"), instanceOf(Object[].class));
		Object[] array = (Object[])object.get("2 elements");
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Collection<Integer>> map = new HashMap<String, Collection<Integer>>();
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		map.put("list", list);
		BSONObject object = encoder.encode(map);

		assertThat(object.get("list"), instanceOf(BSONObject.class));
		BSONObject listObject = (BSONObject)object.get("list");
		assertThat(listObject.containsField(BSONObjectMapper.COLLECTION_CLASS_NAME), is(true));
		assertThat((String)listObject.get(BSONObjectMapper.COLLECTION_CLASS_NAME), is(java.util.ArrayList.class.getName()));
		assertThat(listObject.containsField(BSONObjectMapper.COLLECTION_VALUE), is(true));
		
		assertThat(listObject.get(BSONObjectMapper.COLLECTION_VALUE), instanceOf(List.class));
		List<Object> encodedList = (List<Object>)listObject.get(BSONObjectMapper.COLLECTION_VALUE);
		assertThat((Integer)encodedList.get(0), is(1));
		assertThat((Integer)encodedList.get(1), is(2));
	}
	
	@Test
	public void testObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, EntityObject> map = new HashMap<String, EntityObject>();
		Date now = new Date();
		EntityObject entityObject = new EntityObject();
		entityObject.setId(now.getTime());
		entityObject.setStringValue("foo");
		entityObject.setCreated(now);
		map.put("entity", entityObject);
		BSONObject object = encoder.encode(map);

		assertThat(object.get("entity"), instanceOf(BSONObject.class));
		BSONObject encodedEntityObject = (BSONObject)object.get("entity");
		assertThat(encodedEntityObject.containsField(BSONObjectMapper.CLASS_NAME), is(true));
		assertThat((String)encodedEntityObject.get(BSONObjectMapper.CLASS_NAME), is(EntityObject.class.getName()));
		assertThat((Long)encodedEntityObject.get("id"), is(now.getTime()));
		assertThat((String)encodedEntityObject.get("stringValue"), is("foo"));
		assertThat((Date)encodedEntityObject.get("created"), is(now));
	}
}
