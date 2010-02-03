/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.services;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.UserReportData;
import com.iloggr.gwt.util.client.ILException;

public interface ReportingServiceAsync {
	
	public void fetchEventDataCSV(String token, String appID, Date from, Date to, boolean desc, int limit, int offset, AsyncCallback<String> async);
	public void fetchEventData(String token, String appID, Date from, Date to, boolean desc, int limit, int offset, AsyncCallback<List<Event>>async);

	/**
	 * Produces the data to drive the unique user report.
	 * 
	 * @param token Security token
	 * @param appID Application identifier
	 * @param from Date to start report
	 * @param to End date for report
	 * @return A list of Data for each day (number of unique phones)
	 */
	public void fetchUniqueUserReport(String token, String appID, Date from, Date to, int unit, AsyncCallback<List<UserReportData>>async);

	/**
	 * Produces the data to drive the unique user map report.
	 * 
	 * @param token Security token
	 * @param appID Application identifier
	 * @param from Date to start report
	 * @param to End date for report
	 * @return A list of location fixes 
	 */
	public void fetchRegionReport(String token, String appID, Date from, Date to, AsyncCallback<List<Event>>async);

		
	/**
	 * @param token Security token
	 * @param appID Application ID
	 * @param from Start date
	 * @param to End date
	 * @param unit Units for the report
	 * @param async
	 */
	public void fetchUniqueUserStats(String token, String appID, Date from, Date to, int unit, AsyncCallback<List<UserReportData>>async);




}
