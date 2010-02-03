/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;


public class UserReportData extends iLoggrObject {
	private String description;
	private long longdata;
	private float floatdata;
	private int type;
	public final static String DATE_FORMAT = "yy-MM-dd";
	public final static int LONG = 0;
	public final static int FLOAT = 1;
	
	public UserReportData() {
		super();
	}
	

	public UserReportData(String description, long longdata) {
		this.description = description;
		this.longdata = longdata;
		this.type = LONG;
	}
	
	public UserReportData(String description, float floatdata) {
		this.description = description;
		this.floatdata = floatdata;
		this.type = FLOAT;
	}
	

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public long getLongdata() {
		return longdata;
	}


	public void setLongdata(long longdata) {
		this.longdata = longdata;
		this.type = LONG;
	}


	public float getFloatdata() {
		return floatdata;
	}


	public void setFloatdata(float floatdata) {
		this.floatdata = floatdata;
		this.type = FLOAT;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	
		

}
