/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.services;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.UserReportData;
import com.iloggr.gwt.util.client.ILException;

public interface ReportingService extends RemoteService {
	
	// MySql date formats that will be used for grouping to produce
	// time-based statistics
	public final int YEAR_REPORT_UNIT = 0;
	public final int MONTH_REPORT_UNIT = 1;
	public final int WEEK_REPORT_UNIT = 2;
	public final int DAY_REPORT_UNIT = 3;
	public final int HOUR_REPORT_UNIT = 4;
	public final int MINUTE_REPORT_UNIT = 5;
	public final int SECOND_REPORT_UNIT = 6;
	public final String[] UNIT_FORMATS = {"%y", "%y-%m","%y-%U","%y-%m-%d","%y-%m-%d %H","%y-%m-%d %H:%i",  "%y-%m-%d %H:%i:%s"};
    public final String[] UNIT_NAMES = {"Year", "Month", "Week", "Day", "Hour", "Minute", "Second"};
    public final String[] UNIT_JAVA_FORMATS = {"y", "yy-MM", "", "yy-MM-dd", "yy-MM-dd HH", "yy-MM-dd HH:mm", "yy-MM-dd HH:mm:ss"};
    public final String[] UNIT_JAVA_DISPLAY_FORMATS = {"y", "MM/yy", "", "MM/dd", "MM/dd hh a", "hh:mm a", "hh:mm:ss a"};
       
	/**
	 * Gets all of the events for a given application identified by appID on a particular phone identified
	 * between the dates to and from.  The events are returned as an excel friendly, comma
	 * delimited string containing all of the event data.
	 *
	 * @param token Security token
	 * @param appID Application ID
	 * @param from The start time for the report
	 * @param to The end time for the report
	 * @return The comma delimited report
	 */
	public String fetchEventDataCSV(String token, String appID, Date from, Date to, boolean desc, int limit, int offset) throws Exception;

	/**
	 * @param token Security token
	 * @param appID Application ID
	 * @param from Start date for event search
	 * @param to End date for event search
	 * @param desc Date Decending sort
	 * @param limit Maximum number of records to fetch
	 * @return The raw events for the given criteria
	 * @throws Exception
	 */
	public List<Event> fetchEventData(String token, String appID, Date from, Date to, boolean desc, int limit, int offset) throws Exception;

	/**
	 * Produces the data to drive the unique user report.
	 * 
	 * @param token Security token
	 * @param appID Application identifier
	 * @param from Date to start report
	 * @param to End date for report
	 * @return A list of Data for each day (number of unique phones)
	 * @throws Exception
	 */
	public List<UserReportData> fetchUniqueUserReport(String token, String appID, Date from, Date to, int unit) throws Exception;
	
	/**
	 * @param token SecurityToken
	 * @param appID Application identifier
	 * @param from Start date
	 * @param to End date
	 * @return A list of user location fixes for the time period
	 * @throws Exception
	 */
	public List<Event> fetchRegionReport(String token, String appID, Date from, Date to) throws Exception;

	/**
	 * @param token Security token
	 * @param appID Application ID
	 * @param from Date to start report
	 * @param to End date for report
	 * @param unit Units for stats
	 * @return
	 * @throws Exception
	 */
	public List<UserReportData> fetchUniqueUserStats(String token, String appID, Date from, Date to, int unit) throws Exception;


}