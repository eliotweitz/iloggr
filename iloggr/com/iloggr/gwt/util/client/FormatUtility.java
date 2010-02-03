/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class FormatUtility {
	
	static final DateTimeFormat df = DateTimeFormat.getFormat("yyyyMMdd HH:mm");
	
	
	public static String formatDate(Date date) {
		return (date != null)?df.format(date):"Unknown";
	}

}
