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

public class DecodeCollectionTest {

	private DBObjectDecoder decoder;
	
	@Before
	public void setUp(){
		decoder = new DBObjectDecoder();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNullValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBList dbList = new BasicDBList();
		dbList.add(null);
		BasicDBObject dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbList);

		List list = (List)decoder.toCollection(dbObject);

		assertThat(list.size(), is(1));
		assertThat(list.get(0), is(nullValue()));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testStringValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBList dbList = new BasicDBList();
		dbList.add("bar");
		dbList.add("");
		BasicDBObject dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbList);

		List list = (List)decoder.toCollection(dbObject);
	
		assertThat(list.size(), is(2));
		assertThat((String)list.get(0), is("bar"));	
		assertThat((String)list.get(1), is(""));	
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testNumber() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBList dbList = new BasicDBList();
		dbList.add(Short.MAX_VALUE);
		dbList.add(Byte.MAX_VALUE);
		dbList.add(Integer.MAX_VALUE);
		dbList.add(Long.MAX_VALUE);
		dbList.add(Double.MAX_VALUE);
		dbList.add(Float.MAX_VALUE);
		dbList.add(BigDecimal.valueOf(Long.MAX_VALUE));
		dbList.add(BigDecimal.valueOf(Double.MAX_VALUE));
		BasicDBObject dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbList);

		List list = (List)decoder.toCollection(dbObject);

		assertThat((Short)list.get(0), is(Short.MAX_VALUE));	
		assertThat((Byte)list.get(1), is(Byte.MAX_VALUE));			
		assertThat((Integer)list.get(2), is(Integer.MAX_VALUE));			
		assertThat((Long)list.get(3), is(Long.MAX_VALUE));			
		assertThat((Double)list.get(4), is(Double.MAX_VALUE));			
		assertThat((Float)list.get(5), is(Float.MAX_VALUE));			
		assertThat(((BigDecimal)list.get(6)).longValue(), is(Long.MAX_VALUE));			
		assertThat(((BigDecimal)list.get(7)).doubleValue(), is(Double.MAX_VALUE));			
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testArray() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBList dbList = new BasicDBList();
		dbList.add(new Object[]{"abc", 1});
		BasicDBObject dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbList);
	
		List list = (List)decoder.toCollection(dbObject);
		
		assertThat(list.size(), is(1));
		Object[] array = (Object[])list.get(0);
		assertThat((String)array[0], is("abc"));
		assertThat((Integer)array[1], is(1));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testCollection() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException{
		BasicDBList dbList = new BasicDBList();
		BasicDBList dbNestedList = new BasicDBList();
		dbNestedList.add(1);
		dbNestedList.add(2);
		BasicDBObject dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbNestedList);
		dbList.add(dbObject);
		
		dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbList);
		
		List list = (List)decoder.toCollection(dbObject);

		assertThat(list.get(0), instanceOf(java.util.ArrayList.class));
		List nestedList = (List)list.get(0);
		assertThat((Integer)nestedList.get(0), is(1));
		assertThat((Integer)nestedList.get(1), is(2));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BasicDBList dbList = new BasicDBList();
		Date now = new Date();
		EntityObject entityObject = new EntityObject();
		entityObject.setId(now.getTime());
		entityObject.setStringValue("foo");
		entityObject.setCreated(now);
		dbList.add(entityObject);

		BasicDBObject dbObject = new BasicDBObject();		
		dbObject.put(DBObjectConstants.COLLECTION_CLASS_NAME, "java.util.ArrayList");
		dbObject.put(DBObjectConstants.COLLECTION_VALUE, dbList);
		
		List list = (List)decoder.toCollection(dbObject);

		assertThat(list.get(0), instanceOf(EntityObject.class));
		EntityObject restoredEntityObject = (EntityObject)list.get(0);
		assertThat(restoredEntityObject.getId(), is(now.getTime()));
		assertThat(restoredEntityObject.getStringValue(), is("foo"));
		assertThat(restoredEntityObject.getCreated(), is(now));
	}
}
