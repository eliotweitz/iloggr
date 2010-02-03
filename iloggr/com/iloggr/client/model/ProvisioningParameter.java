/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.NotNull;





/**
 * Model object used to store application provisioning name/value pairs. The string value is stored as a 
 * JSON string to handle various types for the object-c client code.
 * 
 * @author eliot
 * @version 1.0
 * @see Provisioning
 *
 */
@Entity
public class ProvisioningParameter extends iLoggrObject implements Comparable {
	private Long id;
	private String name;
	private String value;
	private boolean active;
	private String type;
	private Provisioning provisioning;

public ProvisioningParameter() {
		super();
	}

public ProvisioningParameter(Long id, String name, String value, String type, boolean active, Provisioning prov) {
	super();
	this.id = id;
	this.name = name;
	this.value = value;
	this.active = active;
	this.type = type;
	this.provisioning = prov;
}

// used to create transfer objects to client
public ProvisioningParameter(String name, String value) {
	this(0l, name, value, "String", true, null);
}

public ProvisioningParameter(String name, Long value) {
	this(0l, name, Long.toString(value), "Long", true, null);
}

public ProvisioningParameter(String name, Double value) {
	this(0l, name, Double.toString(value), "Double", true, null);
}

public ProvisioningParameter(String name, boolean value) {
	this(0l, name, value ? "true":"false", "Boolean", true, null);
}




@Id
@GeneratedValue(strategy = GenerationType.AUTO)
public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}

public boolean isActive() {
	return active;
}

public void setActive(boolean active) {
	this.active = active;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

@ManyToOne
public Provisioning getProvisioning() {
	return provisioning;
}

@NotNull
public void setProvisioning(Provisioning provisioning) {
	this.provisioning = provisioning;
}

public int compareTo(Object o) {
	ProvisioningParameter param = (ProvisioningParameter) o;
	return this.name.compareTo(param.name);
}

public boolean equals(Object o) {
	ProvisioningParameter param = (ProvisioningParameter) o;
	if (id.equals(param.getId()))
		return true;
	return false;
}



}
