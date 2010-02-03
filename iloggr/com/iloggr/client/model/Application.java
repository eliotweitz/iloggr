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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;




/**
 * This class contains all of the information surrounding an application.  Multiple applications can be registered 
 * for a particular user account.  Each is referenced by its unique name and ID. 
 * 
 * Every application contains a provisioning object that contains all of the provisioning parameters
 * registered for the application.
 * 
 * @author eliot
 * @version 1.0
 * 
 *
 */
@Entity
public class Application extends iLoggrObject {
	private Long id;
	private String appID;
	private String name;
	private Account account;
	private Date releaseDate;
	private String announcement;
	private Provisioning provisioning;
	private Set<Phone> phonesInstalled = new HashSet<Phone>();
	private Set<Phone> phonesDisabled = new HashSet<Phone>();
	private Set<Counter> counters = new HashSet<Counter>();

	public Application() {
		super();
	}

	public Application(Long id, String appID, String name, Account account, Date releaseDate, Provisioning provisioning) {
		super();
		this.id = id;
		this.appID = appID;
		this.name = name;
		this.account = account;
		this.releaseDate = releaseDate;
		this.provisioning= provisioning;
		this.phonesInstalled = new HashSet<Phone>();
		this.phonesDisabled = new HashSet<Phone>();
		this.announcement = null;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Index(name="APPID")
	@NotNull
	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	@NotEmpty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@NotNull	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	@OneToOne(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)	
	public Provisioning getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(Provisioning provisioning) {
		this.provisioning = provisioning;
	}

	@ManyToMany(mappedBy="applicationsInstalled")
	public Set<Phone> getPhonesInstalled() {
		return phonesInstalled;
	}

	public void setPhonesInstalled(Set<Phone> phonesInstalled) {
		this.phonesInstalled = phonesInstalled;
	}

	@ManyToMany(mappedBy="applicationsDisabled")
	public Set<Phone> getPhonesDisabled() {
		return phonesDisabled;
	}

	public void setPhonesDisabled(Set<Phone> phonesDisabled) {
		this.phonesDisabled = phonesDisabled;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)	
	public Set<Counter> getCounters() {
		return counters;
	}

	public void setCounters(Set<Counter> counters) {
		this.counters = counters;
	}

	public boolean ownedBy(Account acct) {
		return account.equals(acct);
	}
	
	public void removeCounter(Counter counter) {
		this.counters.remove(counter);
	}
	
	public void addCounter(Counter counter) {
		this.counters.add(counter);
	}
	
	public Counter getCounterNamed(String name) {
		Counter result = null;
		for (Counter c : counters) if (c.getName().equalsIgnoreCase(name)) result = c;
		return result;
	}

}

