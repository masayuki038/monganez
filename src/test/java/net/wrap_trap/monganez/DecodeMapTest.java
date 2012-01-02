package net.wrap_trap.monganez;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;

public class DecodeMapTest {
	
	private BSONObjectMapper decoder;
	
	@Before
	public void setUp(){
		decoder = new BSONObjectMapper();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testNullValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("null", null);
		Map map = decoder.toMap(dbObject);
		assertThat(map.get("null"), is(nullValue()));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testStringValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("foo", "bar");
		dbObject.put("empty", "");
		Map map = decoder.toMap(dbObject);
		assertThat((String)map.get("foo"), is("bar"));
		assertThat((String)map.get("empty"), is(""));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testNumber() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("short", Short.MAX_VALUE);
		dbObject.put("byte", Byte.MAX_VALUE);
		dbObject.put("int", Integer.MAX_VALUE);
		dbObject.put("long", Long.MAX_VALUE);
		dbObject.put("double", Double.MAX_VALUE);
		dbObject.put("float", Float.MAX_VALUE);
		dbObject.put("decimal(long)", BigDecimal.valueOf(Long.MAX_VALUE));
		dbObject.put("decimal(double)", BigDecimal.valueOf(Double.MAX_VALUE));
		
		Map map = decoder.toMap(dbObject);

		assertThat((Short)map.get("short"), is(Short.MAX_VALUE));
		assertThat((Byte)map.get("byte"), is(Byte.MAX_VALUE));
		assertThat((Integer)map.get("int"), is(Integer.MAX_VALUE));
		assertThat((Long)map.get("long"), is(Long.MAX_VALUE));
		assertThat((Double)map.get("double"), is(Double.MAX_VALUE));
		assertThat((Float)map.get("float"), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)map.get("decimal(long)")).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)map.get("decimal(double)")).doubleValue(), is(Double.MAX_VALUE));		
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testArray() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("2 elements", new Object[]{"abc", 1});
		Map map = decoder.toMap(dbObject);
		assertThat(map.get("2 elements"), instanceOf(Object[].class));
		Object[] array = (Object[])map.get("2 elements");
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testCollection() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBObject dbObject = new BasicDBObject();
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		dbObject.put("list", list);

		Map map = decoder.toMap(dbObject);

		assertThat(map.get("list"), instanceOf(ArrayList.class));
		List<Integer> restoredList = (List<Integer>)map.get("list");
		assertThat((Integer)restoredList.get(0), is(1));
		assertThat((Integer)restoredList.get(1), is(2));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBObject dbObject = new BasicDBObject();
		Date now = new Date();
		EntityObject entityObject = new EntityObject();
		entityObject.setId(now.getTime());
		entityObject.setStringValue("foo");
		entityObject.setCreated(now);
		dbObject.put("entity", entityObject);

		Map map = decoder.toMap(dbObject);
		
		assertThat(dbObject.get("entity"), instanceOf(EntityObject.class));
		EntityObject dbEntityObject = (EntityObject)map.get("entity");
		assertThat(dbEntityObject.getId(), is(now.getTime()));
		assertThat(dbEntityObject.getStringValue(), is("foo"));
		assertThat(dbEntityObject.getCreated(), is(now));
	}
}
