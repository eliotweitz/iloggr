/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;







/**
 * This class represents a collection of parameters that provision a particular application. The provisioning parameters
 * are all stored as name, string value pairs and can be set through JSON web services by an account user and
 * accessed by a registered application via the iPhone.
 *
 * @author eliot
 * @version 1.0
 * @see Application
 * @see ProvisioningParameter
 *
 *
 */
@Entity
public class Provisioning extends iLoggrObject {
	private Long id;
	private Set<ProvisioningParameter> parameters = new HashSet<ProvisioningParameter>();
	private String version;
	private Date lastUpdate;
	private Application application;

	public Provisioning()  {
		super();
	}

	public Provisioning(Long id, Application application, String version, Date lastUpdate) {
		super();
		this.id = id;
		this.application = application;
		this.version = version;
		this.lastUpdate = lastUpdate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public Set<ProvisioningParameter> getParameters() {
		return parameters;
	}

	public void setParameters(Set<ProvisioningParameter> params) {
		this.parameters = params;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public ProvisioningParameter getParameterNamed(String name) {
		for (ProvisioningParameter param : parameters) if (param.getName().equalsIgnoreCase(name)) return param;
		return null;
	}

	public void addParameter (ProvisioningParameter param) {
		parameters.add(param);
	}
	
	public void removeParameter (ProvisioningParameter param) {
		parameters.remove(param);
	}

}
