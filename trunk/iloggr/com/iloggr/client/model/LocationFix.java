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
 * This class contains information for a phone generated location fix.  It has the position of the phone as latitude
 * and longitude, and the accuracy and time of when the fix occurs. 
 * 
 * On the iPhone, these fixes are done only when the phone's owner opts into providing it.....
 * 
 * @author eliot
 * @version 1.0
 * @see Event
 * 
 *
 */
@Entity
public class LocationFix extends iLoggrObject {
    private Long id;
    private double latitude;
    private double longitude;
    private double accuracy;
    private Date timeOfFix;
    private Phone phone;
  
    public LocationFix() {
    	// preserve
    }
  
    public LocationFix(Long id, double latitude, double longitude,
			double accuracy, Date timeOfFix, Phone phone) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.accuracy = accuracy;
		this.timeOfFix = timeOfFix;
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

    /**
	 * @return the latitude
	 */
    @Index(name="LATITUDE")
    public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	@Index(name="LONGITUDE")
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the accuracy
	 */
	public double getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the timeOfFix
	 */
	@Index(name="TIMEOFFIX")
	public Date getTimeOfFix() {
		return timeOfFix;
	}

	/**
	 * @param timeOfFix the timeOfFix to set
	 */
	public void setTimeOfFix(Date timeOfFix) {
		this.timeOfFix = timeOfFix;
	}
	
  @ManyToOne
  public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

public boolean equals(LocationFix loc) { // todo - what is the best way to say two places are equivalent?
        return false;
    }


}
