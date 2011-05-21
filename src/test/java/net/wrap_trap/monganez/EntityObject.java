package net.wrap_trap.monganez;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EntityObject implements java.io.Serializable{

	private static final long serialVersionUID = 430297731001477791L;
	
	private long id;
	
	private Object nullValue;
	private String stringValue;
	private Short shortValue;
	private Byte byteValue;
	private Integer integerValue;
	private Long longValue;
	private Double doubleValue;
	private Float floatValue;
	private BigDecimal longDecimalValue;
	private BigDecimal doubleDecimalValue;
	private Object[] objectArray;
	private List<Integer> integerList;
	
	private Date created;
	private AnotherEntityObject anotherEntity;
	private List<AnotherEntityObject> anotherEntities;
	private EntityObject entity;
	private List<EntityObject> entities;
	private AnotherEntityObject[] anotherEntitObjectArray;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Object getNullValue() {
		return nullValue;
	}
	public void setNullValue(Object nullValue) {
		this.nullValue = nullValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public Short getShortValue() {
		return shortValue;
	}
	public void setShortValue(Short shortValue) {
		this.shortValue = shortValue;
	}
	public Byte getByteValue() {
		return byteValue;
	}
	public void setByteValue(Byte byteValue) {
		this.byteValue = byteValue;
	}
	public Integer getIntegerValue() {
		return integerValue;
	}
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}
	public Long getLongValue() {
		return longValue;
	}
	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public Float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}
	public BigDecimal getLongDecimalValue() {
		return longDecimalValue;
	}
	public void setLongDecimalValue(BigDecimal longDecimalValue) {
		this.longDecimalValue = longDecimalValue;
	}
	public BigDecimal getDoubleDecimalValue() {
		return doubleDecimalValue;
	}
	public void setDoubleDecimalValue(BigDecimal doubleDecimalValue) {
		this.doubleDecimalValue = doubleDecimalValue;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}	
	public Object[] getObjectArray() {
		return objectArray;
	}
	public void setObjectArray(Object[] objectArray) {
		this.objectArray = objectArray;
	}
	public List<Integer> getIntegerList() {
		return integerList;
	}
	public void setIntegerList(List<Integer> integerList) {
		this.integerList = integerList;
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
	public AnotherEntityObject[] getAnotherEntitObjectArray() {
		return anotherEntitObjectArray;
	}
	public void setAnotherEntitObjectArray(
			AnotherEntityObject[] anotherEntitObjectArray) {
		this.anotherEntitObjectArray = anotherEntitObjectArray;
	}
}
