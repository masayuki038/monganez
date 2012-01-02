package net.wrap_trap.monganez;

import java.util.List;

import org.bson.BSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class DBObjectFactory implements BSONObjectFactory {

	public BSONObject createBSONObject() {
		return new BasicDBObject();
	}

	public List<Object> createBSONList() {
		return new BasicDBList();
	}

}
