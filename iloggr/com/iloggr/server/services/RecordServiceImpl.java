/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.services;

import java.util.Date;

import org.apache.log4j.Logger;

import com.iloggr.client.model.Application;
import com.iloggr.client.model.ApplicationDuration;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.Phone;
import com.iloggr.client.services.RecordService;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.server.managers.AccountManager;
import com.iloggr.server.managers.EventManager;
import com.iloggr.util.StringUtils;


/**
 * This class provides services to applications on the iPhone that record logging information.  Applications call
 * this service via a local object-c native API that is translated to a JSON RPC call and dispatched to 
 * these methods.  Strings and double data values are collected along with the unique clientID of the device and
 * positional information if available.
 * 
 * @author eliot
 * @version 1.0
 */
public class RecordServiceImpl implements RecordService  {

	static final Logger log = Logger.getLogger(RecordServiceImpl.class);

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
	public void record(String token, String appID, String clientID, String description) throws Exception {
		record(token, appID, Event.EVENT_TYPE_RECORD, clientID, description, -1.0, Event.NOLAT, Event.NOLON, -1.0);
	}


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
	public void record(String token, String appID, String clientID, String description, double data) throws Exception {
		record(token, appID, Event.EVENT_TYPE_RECORD, clientID, description, data, Event.NOLAT, Event.NOLON, -1.0);
	}

	/**
	 * Records an event for the given application identified by appID.  This method call will be called by the 
	 * iLoggr objective-c library on the iPhone.
	 * 
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique clientID for phone
	 * @param description A log description for the event
	 * @param data An associated data value for the event
	 * @param lat latit
	 * @param when When the event actually occurred
	 * @throws Exception
	 */
	public void record(String token, String appID, String clientID, String description, double data, double lat, double lon) throws Exception {
		record(token, appID, Event.EVENT_TYPE_RECORD, clientID, description, data, Event.NOLAT, Event.NOLON, -1d);
	}


	/**
	 * Records an event for the given application identified by appID.  This method call will be called by the 
	 * iLoggr objective-c library on the iPhone.
	 * 
	 * @param token Security token
	 * @param appID Application ID
	 * @param int Event type
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
	public void record(String token, String appID, int eventType, String clientID, String description, double data, double lat, double lon, double accuracy) throws Exception {
		AccountManager am = new AccountManager();
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		Phone phone = null;
		//  See if we already have the phone, if not, add it
		if (!StringUtils.isEmpty(clientID)) {
			phone = em.getPhoneByClientID(clientID);
			if (phone == null) {
				phone = new Phone(0l, clientID, "2");  // TODO - need to get real version
				phone.addApplication(app);
				em.savePhone(phone);
			}
		}
		Date when = new Date();
		Event event = em.recordEvent(phone, eventType, app, when, description, data, lat, lon);
		if (lat > 0 || lon > 0) {
			LocationFix lf = em.recordLocationFix(phone, lat, lon, accuracy, when);
			em.saveLocationFix(lf);
		}
		if (event.getId() == 0l)  {
			am.closeSession();
			throw new ILException(ILException.OPERATION_FAILED);
		}
		am.closeSession();
	}

	public void incrementCounter(String token, String appID, String clientID, String counterName) throws Exception {
		AccountManager am = new AccountManager();
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		Phone phone = null;
		//  See if we already have the phone, if not, add it
		if (!StringUtils.isEmpty(clientID)) {
			phone = em.getPhoneByClientID(clientID);
			if (phone == null) {
				phone = new Phone(0l, clientID, "2");  // TODO - need to get real version
				phone.addApplication(app);
				em.savePhone(phone);
			}
		}
		Counter counter  = app.getCounterNamed(counterName);
		if (counter == null) throw new ILException(ILException.UNKNOWN_APPLICATION_COUNTER_NAME);
		em.incrementCounter(counter);
		am.closeSession();
	}

	public void launch(String token, String appID, String clientID) throws Exception {
		record(token, appID, Event.EVENT_TYPE_LAUNCH, clientID, "Application launched", -1d, Event.NOLAT, Event.NOLON, -1d);
	}

	public void launchUpdateLocation(String token, String appID, String clientID, double lat, double lon) throws Exception {
		record(token, appID, Event.EVENT_TYPE_LAUNCH,  clientID, "Launch Position Update", -1.0d,  lat,  lon, -1.0d);
	}

	public void exit(String token, String appID, String clientID, long duration)  throws Exception {
		AccountManager am = new AccountManager();
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		Phone phone = null;
		//  See if we already have the phone, if not, add it
		if (!StringUtils.isEmpty(clientID)) {
			phone = em.getPhoneByClientID(clientID);
			if (phone == null) {
				phone = new Phone(0l, clientID, "2");  // TODO - need to get real version
				phone.addApplication(app);
				em.savePhone(phone);
			}
		}
		// DONT RECORD EVENT EXIT
		ApplicationDuration appDuration = new ApplicationDuration(new Date(), app, duration, phone);
		em.saveApplicationDuration(appDuration);
	}

	public void record(String token, String appID, String clientID,
			String description, double data, double lat, double lon,
			double accuracy) throws Exception {
		record(token, appID, Event.EVENT_TYPE_RECORD, clientID, description, data, lat, lon, accuracy);

	}




}
