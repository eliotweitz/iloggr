/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.services;

import java.util.Date;
import java.util.List;

import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.UserReportData;
import com.iloggr.client.services.ReportingService;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.server.managers.AccountManager;
import com.iloggr.server.managers.EventManager;


/**
 * This class provides user-facing services to retrieve aggregate data collected from instrumented applications.  
 * All data logged by the recording service can be accessed as comma-delimited strings that contain data collected
 * for a particular application over a period of time.
 * 
 * @author eliot
 * @version 1.0
 */
public class ReportingServiceImpl implements ReportingService {
	

	
	/**
	 * Gets all of the events for a given application identified by appID on a particular phone identified 
	 * between the dates to and from.  The events are returned as an excel friendly, comma 
	 * delimited string containing all of the event data.
	 * 
	 * @param token Security token
	 * @param AppID Application ID
	 * @param from The start time for the report
	 * @param to The end time for the report
	 * @return The comma delimited report
	 */
	public String fetchEventDataCSV(String token, String appID, Date from, Date to, boolean desc, int limit, int offset) throws Exception {
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		if (from == null || to == null) {
			from = new Date();
			to = new Date();
		} 
		//  If we have the same dates or within a second, default to get the last 24 hours
		// DONT NEED TO DO THIS SINCE BOD, EOD IS HANDLED ON CLIENT LEVEL
/*		if (from.equals(to) || (to.getTime() - from.getTime()) < 1000) {
			// 24 hours previous
			from.setTime(to.getTime()-24*60*60*1000);
		}*/
		String result = em.getEventsCSV(app, from, to, desc, limit, offset);
		em.closeSession();
		return result;
	}
	
	/**
	 * @param token Security token
	 * @param AppID Application ID
	 * @param from Start date for event search
	 * @param to End date for event search
	 * @param desc Date Decending sort
	 * @param limit Maximum number of records to fetch
	 * @return
	 * @throws Exception 
	 */
	public List<Event> fetchEventData(String token, String appID, Date from, Date to, boolean desc, int limit, int offset) throws Exception {
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		if (from == null || to == null) {
			from = new Date();
			to = new Date();
		} 
		//  If we have the same dates or within a second, default to get the last 24 hours
/*		if (from.equals(to) || (to.getTime() - from.getTime()) < 1000) {
			// 24 hours previous
			from.setTime(to.getTime()-24*60*60*1000);
		}*/
		List<Event>results = em.getEvents(app, from, to, desc, limit, offset);
		em.closeSession();
		return results;
	}
	
	public List<UserReportData> fetchUniqueUserReport(String token, String appID, Date from, Date to, int unit) throws Exception {
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		if (from == null || to == null) {
			from = new Date();
			to = new Date();
		} 
		//  If we have the same dates or within a second, default to get the last 24 hours
/*		if (from.equals(to) || (to.getTime() - from.getTime()) < 1000) {
			// 24 hours previous
			from.setTime(to.getTime()-24*60*60*1000);
		}*/
		List<UserReportData>results = em.fetchUniquePhoneReport(app, from, to, unit);
		am.closeSession();
		return results;
	}
	
	public List<Event> fetchRegionReport(String token, String appID, Date from, Date to) throws Exception {
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		if (from == null || to == null) {
			from = new Date();
			to = new Date();
		} 
		//  If we have the same dates or within a second, default to get the last 24 hours
/*		if (from.equals(to) || (to.getTime() - from.getTime()) < 1000) {
			// 24 hours previous
			from.setTime(to.getTime()-24*60*60*1000);
		}*/
		List<Event>results = em.fetchRegionReport(app, from, to);
		am.closeSession();
		return results;
	}

	
	public List<UserReportData> fetchUniqueUserStats(String token, String appID, Date from, Date to, int unit) throws Exception {
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		if (from == null || to == null) {
			from = new Date();
			to = new Date();
		} 
		//  If we have the same dates or within a second, default to get the last 24 hours
/*		if (from.equals(to) || (to.getTime() - from.getTime()) < 1000) {
			// 24 hours previous
			from.setTime(to.getTime()-24*60*60*1000);
		}*/
		List<UserReportData>results = em.fetchUniquePhoneStats(app, from, to, unit);
		List<UserReportData>resultsD = em.fetchUniquePhoneDurationStats(app, from, to, unit);
		
		am.closeSession();
		
		results.addAll(resultsD);
		return results;
	}

	

}
