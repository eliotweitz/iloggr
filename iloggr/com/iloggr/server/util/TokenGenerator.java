/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.util;


import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Generates MD5 hashed tokens for URLs embedded in emails to users.
 * 
 * @author eliot
 *
 */
public class TokenGenerator {

	public static String  BADTOKEN = "BADBADBAD";


	/**
	 * Generates a unique MD5 hash based token from a long value
	 * 
	 * @param id A long value
	 */
	public static String generateToken(long id) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte a[] = BigInteger.valueOf(id).toByteArray();
			byte o[] = md.digest(a);
			long t = new BigInteger(o).longValue();
			return Long.toString(t, Character.MAX_RADIX);
		} catch (Exception e)  {
			return BADTOKEN;
		}
	}

	/**
	 * Generates a unique MD5 has based token from a string value	
	 * @param s A string value
	 */
	public static String generateToken(String s) {
		// Add some uniqueness for iLoggr
		String token = s + "y92yc2b9y3229cba99abdf";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			char c[] = token.toCharArray();
			byte a[] = new byte[100];
			for (int i=0;i<c.length; i++)  {
				if (i>=100) break;
				a[i] = (byte)c[i];
			}
			byte o[] = md.digest(a);
			long t = new BigInteger(o).longValue();
			return Long.toString(t, Character.MAX_RADIX);
		} catch (Exception e)  {
			return BADTOKEN;
		}
	}
}
