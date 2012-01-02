package net.wrap_trap.monganez;

import java.util.List;

import org.bson.BSONObject;

public interface BSONObjectFactory {

	BSONObject createBSONObject();
	
	List<Object> createBSONList();
	
}
