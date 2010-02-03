/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.JSON;

import java.lang.reflect.Method;




/**
 * Used by all service servlets to decode and dispatch incoming JSON encoded RPC method calls.  Uses JSONRPC to
 * encode and decode.
 *
 * @author eliot
 * @version 1.0
 * @see JSONRPC
 */
public class JSONRPCRequest {

	/**
	 * The method for this request.
	 */
	private final Method method;

	/**
	 * The parameters for this request.
	 */
	private final Object[] parameters;

	public JSONRPCRequest(Class<?> serviceClass, String methodName,
			Class<?>[] parameterTypes, Object[] parameters) {
		this.method = findMethod(serviceClass, methodName, parameterTypes);
		this.parameters = parameters;
	}

	private Method findMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the request's method.
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Get the request's parameters.
	 */
	public Object[] getParameters() {
		return parameters;
	}

}
