/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.util;

public class GeodeticCoord {
	double latitude;
	double longitude;
	long error;
	
	
	public GeodeticCoord() {
		super();
	}


	public GeodeticCoord(double latitude, double longitude, long error) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.error = error;
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
	
	public double getLat() {
		return this.latitude*180.0/3.14159265358979323;
	}
	
	public double getLon() {
		return this.longitude*180.0/3.14159265358979323;
	}


	public long getError() {
		return error;
	}


	public void setError(long error) {
		this.error = error;
	}
	
	

}


