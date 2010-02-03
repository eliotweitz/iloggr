/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Index;




/**
 * This class abtracts a phone that has an application which uses the iLoggr service. It is identified by the
 * clientID which is a unique string assigned to all iPhones.
 *
 * @author eliot
 * @version 1.0
 *
 *
 */
@Entity
public class Phone extends iLoggrObject {
	private Long id;
	private String clientID;
	private String version;
	Set<LocationFix> fixes;
	Set<Application> applicationsInstalled = new HashSet<Application>();
	Set<Application> applicationsDisabled = new HashSet<Application>();
	Set<Event> events = new HashSet<Event>();


	public Phone() {
		// preserve default constructor
	}

	public Phone(Long id, String clientID, String version) {
		super();
		this.id = id;
		this.clientID = clientID;
		this.version = version;
		this.fixes = new HashSet<LocationFix>();
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Index(name="CLIENTID")
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@OneToMany
	public Set<LocationFix> getFixes() {
		return fixes;
	}

	public void setFixes(Set<LocationFix> fixes) {
		this.fixes = fixes;
	}


	@OneToMany
	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@ManyToMany
	public Set<Application> getApplicationsInstalled() {
		return applicationsInstalled;
	}

	public void setApplicationsInstalled(Set<Application> applicationsInstalled) {
		this.applicationsInstalled = applicationsInstalled;
	}

	@ManyToMany
	public Set<Application> getApplicationsDisabled() {
		return applicationsDisabled;
	}

	public void setApplicationsDisabled(Set<Application> applicationsDisabled) {
		this.applicationsDisabled = applicationsDisabled;
	}

	public boolean isDisabled(Application app) {
		return applicationsDisabled.contains(app);
	}

	public boolean isInstalled(Application app) {
		return applicationsInstalled.contains(app);
	}

	public void disableApplication(Application app) {
		applicationsDisabled.add(app);
	}

	public void addApplication(Application app) {
		applicationsInstalled.add(app);
	}

	public void addEvent(Event event) {
		events.add(event);
	}
}
