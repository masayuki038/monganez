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

import org.bson.BSONObject;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class EncodeObjectTest {

	
	private BSONObjectMapper encoder;
	
	@Before
	public void setUp(){
		encoder = new BSONObjectMapper();
	}
	
	@Test
	public void testNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setNullValue(null);
		BSONObject object = encoder.encode(entityObject);
		assertThat(object.get("nullValue"), is(nullValue()));
	}
	
	@Test
	public void testStringValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setStringValue("bar");		
		BSONObject object = encoder.encode(entityObject);
		assertThat((String)object.get("stringValue"), is("bar"));
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
		
		BSONObject object = encoder.encode(entityObject);

		assertThat((Short)object.get("shortValue"), is(Short.MAX_VALUE));
		assertThat((Byte)object.get("byteValue"), is(Byte.MAX_VALUE));
		assertThat((Integer)object.get("integerValue"), is(Integer.MAX_VALUE));
		assertThat((Long)object.get("longValue"), is(Long.MAX_VALUE));
		assertThat((Double)object.get("doubleValue"), is(Double.MAX_VALUE));
		assertThat((Float)object.get("floatValue"), is(Float.MAX_VALUE));
		assertThat(((BigDecimal)object.get("longDecimalValue")).longValue(), is(Long.MAX_VALUE));
		assertThat(((BigDecimal)object.get("doubleDecimalValue")).doubleValue(), is(Double.MAX_VALUE));
	}
	
	@Test
	public void testArray() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		entityObject.setObjectArray(new Object[]{"abc", 1});
		
		BSONObject object = encoder.encode(entityObject);

		assertThat(object.get("objectArray"), instanceOf(Object[].class));
		Object[] array = (Object[])object.get("objectArray");
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

		BSONObject object = encoder.encode(entityObject);

		assertThat(object.get("integerList"), instanceOf(BSONObject.class));
		BSONObject listObject = (BSONObject)object.get("integerList");
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
		EntityObject entityObject = new EntityObject();
		Date now = new Date();
		EntityObject entityObject2 = new EntityObject();
		entityObject2.setId(now.getTime());
		entityObject2.setStringValue("foo");
		entityObject2.setCreated(now);
		entityObject.setEntity(entityObject2);
		
		BSONObject object = encoder.encode(entityObject);

		assertThat(object.get("entity"), instanceOf(BSONObject.class));
		BSONObject encodedEntityObject = (BSONObject)object.get("entity");
		assertThat(encodedEntityObject.containsField(BSONObjectMapper.CLASS_NAME), is(true));
		assertThat((String)encodedEntityObject.get(BSONObjectMapper.CLASS_NAME), is(EntityObject.class.getName()));
		assertThat((Long)encodedEntityObject.get("id"), is(now.getTime()));
		assertThat((String)encodedEntityObject.get("stringValue"), is("foo"));
		assertThat((Date)encodedEntityObject.get("created"), is(now));
	}
	
	@Test
	public void testRecursiveThis() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		EntityObject entityObject = new EntityObject();
		Date now = new Date();
		entityObject.setCreated(now);
		entityObject.setEntity(entityObject);
		
		BSONObject object = encoder.encode(entityObject);
		
		assertThat((Date)object.get("created"), is(now));
		assertThat(object.get("entity"), is(nullValue()));		
	}
}
