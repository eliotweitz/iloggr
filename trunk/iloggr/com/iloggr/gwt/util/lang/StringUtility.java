/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.lang;

public class StringUtility {
	public static boolean isBlank(String string) {
		return (string == null) || "".equals(string.trim());
	}
	
	public static boolean isNonBlankMatch(String string1, String string2, boolean ignoreCase) {
		return !isBlank(string1) && (ignoreCase ? string1.equalsIgnoreCase(string2) : string1.equals(string2));
	}
	
	public static boolean isValidEmail(String email) {
		// TODO(jsirois): robustify, break out a GWT module for this stuff, test
		return !"".equals(email.trim()) && email.contains("@") && email.contains(".");
	}



}
