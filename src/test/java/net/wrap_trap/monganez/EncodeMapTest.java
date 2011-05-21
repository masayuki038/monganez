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

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class EncodeMapTest {

	private DBObjectEncoder encoder;
	
	@Before
	public void setUp(){
		encoder = new DBObjectEncoder();
	}
	
	@Test
	public void testEncoeMapWithNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("null", null);
		DBObject dbObject = encoder.encode(map);
		assertThat(dbObject.get("null"), is(nullValue()));
	}
	
	@Test
	public void testEncodeMapWithStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("foo", "bar");
		map.put("empty", "");
		DBObject dbObject = encoder.encode(map);
		assertThat((String)dbObject.get("foo"), is("bar"));
		assertThat((String)dbObject.get("empty"), is(""));
	}

	@Test
	public void testEncodeMapWithNumber() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Number> map = new HashMap<String, Number>();
		map.put("short", Short.MAX_VALUE);
		map.put("byte", Byte.MAX_VALUE);
		map.put("int", Integer.MAX_VALUE);
		map.put("long", Long.MAX_VALUE);
		map.put("double", Double.MAX_VALUE);
		map.put("float", Float.MAX_VALUE);
		map.put("decimal(long)", BigDecimal.valueOf(Long.MAX_VALUE));
		map.put("decimal(double)", BigDecimal.valueOf(Double.MAX_VALUE));

		DBObject dbObject = encoder.encode(map);
		assertThat((Short)dbObject.get("short"), is(Short.MAX_VALUE));
		assertThat((Byte)dbObject.get("byte"), is(Byte.MAX_VALUE));
		assertThat((Integer)dbObject.get("int"), is(Integer.MAX_VALUE));
		assertThat((Long)dbObject.get("long"), is(Long.MAX_VALUE));
		assertThat((Double)dbObject.get("double"), is(Double.MAX_VALUE));
		assertThat((Float)dbObject.get("float"), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)dbObject.get("decimal(long)")).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)dbObject.get("decimal(double)")).doubleValue(), is(Double.MAX_VALUE));
	}

	@Test
	public void testEncodeMapWithArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Object[]> map = new HashMap<String, Object[]>();
		map.put("2 elements", new Object[]{"abc", 1});
		DBObject dbObject = encoder.encode(map);
		assertThat(dbObject.get("2 elements"), instanceOf(Object[].class));
		Object[] array = (Object[])dbObject.get("2 elements");
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@Test
	public void testEncodeMapWithCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, Collection<Integer>> map = new HashMap<String, Collection<Integer>>();
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		map.put("list", list);
		DBObject dbObject = encoder.encode(map);

		assertThat(dbObject.get("list"), instanceOf(DBObject.class));
		DBObject listObject = (DBObject)dbObject.get("list");
		assertThat(listObject.containsField(DBObjectConstants.COLLECTION_CLASS_NAME), is(true));
		assertThat((String)listObject.get(DBObjectConstants.COLLECTION_CLASS_NAME), is(java.util.ArrayList.class.getName()));
		assertThat(listObject.containsField(DBObjectConstants.COLLECTION_VALUE), is(true));
		
		assertThat(listObject.get(DBObjectConstants.COLLECTION_VALUE), instanceOf(BasicDBList.class));
		BasicDBList dbList = (BasicDBList)listObject.get(DBObjectConstants.COLLECTION_VALUE);
		assertThat((Integer)dbList.get(0), is(1));
		assertThat((Integer)dbList.get(1), is(2));
	}
	
	@Test
	public void testEncodeMapWithObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String, EntityObject> map = new HashMap<String, EntityObject>();
		Date now = new Date();
		EntityObject entityObject = new EntityObject();
		entityObject.setId(now.getTime());
		entityObject.setName("foo");
		entityObject.setCreated(now);
		map.put("entity", entityObject);
		DBObject dbObject = encoder.encode(map);

		assertThat(dbObject.get("entity"), instanceOf(DBObject.class));
		DBObject dbEntityObject = (DBObject)dbObject.get("entity");
		assertThat(dbEntityObject.containsField(DBObjectConstants.CLASS_NAME), is(true));
		assertThat((String)dbEntityObject.get(DBObjectConstants.CLASS_NAME), is(EntityObject.class.getName()));
		assertThat((Long)dbEntityObject.get("id"), is(now.getTime()));
		assertThat((String)dbEntityObject.get("name"), is("foo"));
		assertThat((Date)dbEntityObject.get("created"), is(now));
	}
}
