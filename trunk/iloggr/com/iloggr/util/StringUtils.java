/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.util;

public class StringUtils {
	
/*   URL reserved characters
	Dollar ("$") 24
	 Ampersand ("&") 26
	 Plus ("+") 2B
	 Comma (",") 2C
	 Forward slash/Virgule ("/") 2F
	 Colon (":") 3A
	 Semi-colon (";") 3B
	 Equals ("=") 3D
	 Question mark ("?") 3F
	 'At' symbol ("@") 40
*/
	public static String encodeURL(String withSpaces) {
		
		// Remove spaces
        java.util.StringTokenizer t = new java.util.StringTokenizer(withSpaces, " ");
        StringBuffer result = new StringBuffer();
        while (t.hasMoreTokens()) {
            result.append(t.nextToken());
            result.append("%20");
        }
        String noSpaces = result.toString();
        
        // Now remove commas
        java.util.StringTokenizer t2 = new java.util.StringTokenizer(noSpaces, ",");
        StringBuffer result2 = new StringBuffer();
        while (t2.hasMoreTokens()) {
        	result2.append(t2.nextToken());
        	result2.append("%2C");
        }
        
        return result2.toString();
       
    }
	
	public static String cleanSQLString(String param) {
		// Remove spaces 
        java.util.StringTokenizer t = new java.util.StringTokenizer(param, " ");
        StringBuffer result = new StringBuffer();
        while (t.hasMoreTokens()) {
            result.append(t.nextToken());
        }
        String r = result.toString();
        
        // trim to 30 characters max
        if (r.length() > 30) r = r.substring(0, 29);
 		return r;
	}
	
	public static boolean isEmpty(String string) {
		return (string == null || string.trim().length() == 0);
	}
	
	public static int intValue(String string) {
		if (isEmpty(string)) return 0;
		return Integer.parseInt(string);
	}
		
	public static boolean isValidEmail(String email) {
		// TODO(jsirois): robustify, break out a GWT module for this stuff, test
		return !"".equals(email.trim()) && email.contains("@") && email.contains(".");
	}




}
