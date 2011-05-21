package net.wrap_trap.monganez;

import java.util.Set;

public class AnotherEntityObject implements java.io.Serializable{

	private static final long serialVersionUID = -5509073957105155997L;

	private byte[] id;
	private char[] name;
	private long created;
	private EntityObject entity;
	private Set<EntityObject> entities;
	private AnotherEntityObject anotherEntity;
	private Set<AnotherEntityObject> anotherEntites;
	
	public byte[] getId() {
		return id;
	}
	public void setId(byte[] id) {
		this.id = id;
	}
	public char[] getName() {
		return name;
	}
	public void setName(char[] name) {
		this.name = name;
	}
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public EntityObject getEntity() {
		return entity;
	}
	public void setEntity(EntityObject entity) {
		this.entity = entity;
	}
	public Set<EntityObject> getEntities() {
		return entities;
	}
	public void setEntities(Set<EntityObject> entities) {
		this.entities = entities;
	}
	public AnotherEntityObject getAnotherEntity() {
		return anotherEntity;
	}
	public void setAnotherEntity(AnotherEntityObject anotherEntity) {
		this.anotherEntity = anotherEntity;
	}
	public Set<AnotherEntityObject> getAnotherEntites() {
		return anotherEntites;
	}
	public void setAnotherEntites(Set<AnotherEntityObject> anotherEntites) {
		this.anotherEntites = anotherEntites;
	}
}
