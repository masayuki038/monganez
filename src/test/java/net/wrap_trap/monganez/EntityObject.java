package net.wrap_trap.monganez;

import java.util.Date;
import java.util.List;

public class EntityObject implements java.io.Serializable{

	private static final long serialVersionUID = 430297731001477791L;
	
	private long id;
	private String name;
	private Date created;
	private AnotherEntityObject anotherEntity;
	private List<AnotherEntityObject> anotherEntities;
	private EntityObject entity;
	private List<EntityObject> entities;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public AnotherEntityObject getAnotherEntity() {
		return anotherEntity;
	}
	public void setAnotherEntity(AnotherEntityObject anotherEntity) {
		this.anotherEntity = anotherEntity;
	}
	public List<AnotherEntityObject> getAnotherEntities() {
		return anotherEntities;
	}
	public void setAnotherEntities(List<AnotherEntityObject> anotherEntities) {
		this.anotherEntities = anotherEntities;
	}
	public EntityObject getEntity() {
		return entity;
	}
	public void setEntity(EntityObject entity) {
		this.entity = entity;
	}
	public List<EntityObject> getEntities() {
		return entities;
	}
	public void setEntities(List<EntityObject> entities) {
		this.entities = entities;
	}	
}
