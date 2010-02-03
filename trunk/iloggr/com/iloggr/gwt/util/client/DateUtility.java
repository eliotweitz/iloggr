package com.iloggr.gwt.util.client;

import java.util.Date;

public class DateUtility {
	
	/**
	 * Midnight (beginning of day)
	 * 
	 * @param input Input date
	 * @return Date midnight that day
	 */
	public static Date getBOD(Date input) {
		Date eod = new Date(input.getTime());
		eod.setHours(0);
		eod.setMinutes(0);
		eod.setSeconds(0);
		return eod;
	}
	
	/**
	 * Returns 23:59:59 for the day given (End of day)
	 * @param input  Input date
	 * @return Date 11:59:59 PM that day
	 */
	public static Date getEOD(Date input) {
		Date eod = new Date(input.getTime());
		eod.setHours(23);
		eod.setMinutes(59);
		eod.setSeconds(59);
		return eod;
	}
	
	/**
	 * Returns the given date in UTC time
	 * 
	 * @param input Input date
	 * @return Date In UTC time
	 */
	public static Date getUTC(Date input) {
		int utcOffsetMs = input.getTimezoneOffset() * 60 * 1000;
		Date utc = new Date(input.getTime()+utcOffsetMs);
		return utc;
	}


}
