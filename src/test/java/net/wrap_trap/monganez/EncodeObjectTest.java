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

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class EncodeObjectTest {

	
	private DBObjectEncoder encoder;
	
	@Before
	public void setUp(){
		encoder = new DBObjectEncoder();
	}
	
	@Test
	public void testNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setNullValue(null);
		DBObject dbObject = encoder.encode(entityObject);
		assertThat(dbObject.get("nullValue"), is(nullValue()));
	}
	
	@Test
	public void testStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setStringValue("bar");		
		DBObject dbObject = encoder.encode(entityObject);
		assertThat((String)dbObject.get("stringValue"), is("bar"));
	}

	@Test
	public void testNumberValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setShortValue(Short.MAX_VALUE);
		entityObject.setByteValue(Byte.MAX_VALUE);
		entityObject.setIntegerValue(Integer.MAX_VALUE);
		entityObject.setLongValue(Long.MAX_VALUE);
		entityObject.setDoubleValue(Double.MAX_VALUE);
		entityObject.setFloatValue(Float.MAX_VALUE);
		entityObject.setLongDecimalValue(BigDecimal.valueOf(Long.MAX_VALUE));
		entityObject.setDoubleDecimalValue(BigDecimal.valueOf(Double.MAX_VALUE));
		
		DBObject dbObject = encoder.encode(entityObject);

		assertThat((Short)dbObject.get("shortValue"), is(Short.MAX_VALUE));
		assertThat((Byte)dbObject.get("byteValue"), is(Byte.MAX_VALUE));
		assertThat((Integer)dbObject.get("integerValue"), is(Integer.MAX_VALUE));
		assertThat((Long)dbObject.get("longValue"), is(Long.MAX_VALUE));
		assertThat((Double)dbObject.get("doubleValue"), is(Double.MAX_VALUE));
		assertThat((Float)dbObject.get("floatValue"), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)dbObject.get("longDecimalValue")).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)dbObject.get("doubleDecimalValue")).doubleValue(), is(Double.MAX_VALUE));
	}
	
	@Test
	public void testArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setObjectArray(new Object[]{"abc", 1});
		
		DBObject dbObject = encoder.encode(entityObject);

		assertThat(dbObject.get("objectArray"), instanceOf(Object[].class));
		Object[] array = (Object[])dbObject.get("objectArray");
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@Test
	public void testCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		entityObject.setIntegerList(list);
		DBObject dbObject = encoder.encode(entityObject);

		assertThat(dbObject.get("integerList"), instanceOf(DBObject.class));
		DBObject listObject = (DBObject)dbObject.get("integerList");
		assertThat(listObject.containsField(DBObjectConstants.COLLECTION_CLASS_NAME), is(true));
		assertThat((String)listObject.get(DBObjectConstants.COLLECTION_CLASS_NAME), is(java.util.ArrayList.class.getName()));
		assertThat(listObject.containsField(DBObjectConstants.COLLECTION_VALUE), is(true));
		
		assertThat(listObject.get(DBObjectConstants.COLLECTION_VALUE), instanceOf(BasicDBList.class));
		BasicDBList dbList = (BasicDBList)listObject.get(DBObjectConstants.COLLECTION_VALUE);
		assertThat((Integer)dbList.get(0), is(1));
		assertThat((Integer)dbList.get(1), is(2));
	}
	
	@Test
	public void testObject() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		Date now = new Date();
		EntityObject entityObject2 = new EntityObject();
		entityObject2.setId(now.getTime());
		entityObject2.setStringValue("foo");
		entityObject2.setCreated(now);
		entityObject.setEntity(entityObject2);
		
		DBObject dbObject = encoder.encode(entityObject);

		assertThat(dbObject.get("entity"), instanceOf(DBObject.class));
		DBObject dbEntityObject = (DBObject)dbObject.get("entity");
		assertThat(dbEntityObject.containsField(DBObjectConstants.CLASS_NAME), is(true));
		assertThat((String)dbEntityObject.get(DBObjectConstants.CLASS_NAME), is(EntityObject.class.getName()));
		assertThat((Long)dbEntityObject.get("id"), is(now.getTime()));
		assertThat((String)dbEntityObject.get("stringValue"), is("foo"));
		assertThat((Date)dbEntityObject.get("created"), is(now));
	}
	
	@Test
	public void testRecursiveThis() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		Date now = new Date();
		entityObject.setCreated(now);
		entityObject.setEntity(entityObject);
		
		DBObject dbObject = encoder.encode(entityObject);
		
		assertThat((Date)dbObject.get("created"), is(now));
		assertThat(dbObject.get("entity"), is(nullValue()));		
	}
}
