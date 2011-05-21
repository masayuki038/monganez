package net.wrap_trap.monganez;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.IsNull.*;
import static net.wrap_trap.monganez.DBObjectConstants.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
	public void testEncoeMapWithNullValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> list = new ArrayList<Object>();
		list.add(null);
		DBObject dbObject = encoder.encode(list);
		BasicDBList dbList = (BasicDBList)dbObject.get(COLLECTION_VALUE);
		assertThat(dbList.size(), is(1));
		assertThat(dbList.get(0), is(nullValue()));
	}
}
