/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.services;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iloggr.gwt.util.client.ILException;

public interface RecordServiceAsync {
	/**
	 * Records an event for the given application identified by appID.  This method call will be called by the
	 * iLoggr objective-c library on the iPhone.
	 *
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique clientID for phone
	 * @param description A log description for the event
	 * @throws Exception
	 */
	public void record(String token, String appID, String clientID, String description, AsyncCallback<Void> async);

	/**
	 * Records an event for the given application identified by appID.  This method call will be called by the
	 * iLoggr objective-c library on the iPhone.
	 *
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique clientID for phone
	 * @param description A log description for the event
	 * @param data An associated data value for the event
	 * @throws Exception
	 */
	public void record(String token, String appID, String clientID, String description, double data, AsyncCallback<Void> async);

	/**
	 * Records an event for the given application identified by appID.  This method call will be called by the
	 * iLoggr objective-c library on the iPhone.
	 *
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique clientID for phone
	 * @param description A log description for the event
	 * @param data An associated data value for the event
	 * @param lat Latitude of the phone when the event occurred
	 * @param lon longitude of the phone when the event occurred
	 * @param accuracy Accuracy (in meters) of the location
	 * @param timeOfFix Time that the phone was located
	 * @param when When the event actually occurred
	 * @throws Exception
	 */
	public void record(String token, String appID, String clientID, String description, double data, double lat, double lon, double accuracy,  AsyncCallback<Void> async);
	
	public void launch(String token, String appID, String clientID, AsyncCallback<Void> async);
	public void incrementCounter(String token, String appID, String clientID, String counterName, AsyncCallback<Void> async);
	public void exit(String token, String appID, String clientID, long duration, AsyncCallback<Void> async);
	public void launchUpdateLocation(String token, String appID, String clientID, double lat, double lon, AsyncCallback<Void> async);


}
