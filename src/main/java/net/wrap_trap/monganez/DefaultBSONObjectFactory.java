package net.wrap_trap.monganez;

import java.util.List;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

public class DefaultBSONObjectFactory implements BSONObjectFactory {

	public BSONObject createBSONObject() {
		return new BasicBSONObject();
	}

	public List<Object> createBSONList() {
		return new BasicBSONList();
	}

}
