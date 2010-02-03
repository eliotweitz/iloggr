/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;




/**
  * This class contains all of the information surrounding an event that has been recorded by an application.  Events
 * can contain several pieces of information: A unique clientID identifying the phone that the event occurred, a string 
 * (log) description for the event, a double value, and optionally location fix information.  
 * 
 * @author eliot
 * @version 1.0
 * 
 *
 */
/**
 * @author eliot
 *
 */
@Entity
public class Event extends iLoggrObject {
	
	public static final int EVENT_TYPE_RECORD = 0;
	public static final int EVENT_TYPE_LAUNCH = 1;
	public static final int EVENT_TYPE_EXIT = 2;
	
	public static final double NOLAT = 999d;
	public static final double NOLON = 999d;
	
	private Long id;
	private int eventType;
	private Application application;
	private Date recordTime;
	private String description;
	private double data;
	private Phone phone;
	private double latitude;
	private double longitude;
	
	
	public Event() {
		super();
	}

	public Event(Long id, int eventType, Application application, Date when,
			String description, double data, Phone phone, double latitude, double longitude) {
		super();
		this.id = id;
		this.eventType = eventType;
		this.latitude = latitude;
		this.longitude = longitude;
		this.application = application;
		this.recordTime = when;
		this.description = description;
		this.data = data;
		this.phone = phone;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
		
	@ManyToOne
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	
	@Index(name="RECORDTIME")
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date when) {
		this.recordTime = when;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne
	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	@Index(name="DATA")
	public double getData() {
		return data;
	}
	public void setData(double data) {
		this.data = data;
	}
	
	@Index(name="EVENTTYPE")
	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	

}
