/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.JSON;

import java.util.Map;

import net.sf.json.JSONObject;

import com.google.inject.internal.collect.Maps;
import com.iloggr.gwt.util.client.ILException;

public class JSONRPCResponse {
	private final int RESPONSE_OK = 0;
	private final int RESPONSE_NOK = -1;
	private final String RESPONSE_OK_MESSAGE = "Success";
	private final String RESPONSE_NOK_MESSAGE = "Operation failed";
	private Throwable e;
	private Object result;
	private int error;
	private String errorMessage;


	public JSONRPCResponse() {
		this.e = null;
		this.result = null;
		this.error = RESPONSE_NOK;
		this.errorMessage = RESPONSE_NOK_MESSAGE;
	}

	public JSONRPCResponse(Object result) {
		this.result = result;
		setException(null);
	}

	public JSONRPCResponse(ILException e) {
		setException(e);
	}

	public JSONRPCResponse(Throwable t) {
		setThrowable(t);
	}


	public void setResult(Object result) {
		this.result = result;
		this.error = RESPONSE_OK;
		this.errorMessage = RESPONSE_OK_MESSAGE;		
	}

	public void setThrowable(Throwable t) {
		if (t == null) {
			this.error = RESPONSE_OK;
			this.errorMessage = RESPONSE_OK_MESSAGE;
		}
		if (t instanceof ILException) {
			setException((ILException)t);
		} else {
			Throwable e = t.getCause();
			if (e instanceof ILException) {
				setException((ILException)e);
			} else if (e != null) {
				error = RESPONSE_NOK;
				errorMessage = e.getMessage();
			} else {
				error = RESPONSE_NOK;
				if (t.getMessage() != null) errorMessage = t.getMessage();
			}
		}
	}

	public void setException(ILException e) {
		if (e != null) {
			error = ((ILException)e).getErrorCode();
			errorMessage = e.getMessage();
		} else {
			this.error = RESPONSE_OK;
			this.errorMessage = RESPONSE_OK_MESSAGE;
		}	
	}

	public static JSONRPCResponse responseForGeneralFailure() {
		return new JSONRPCResponse();
	}

	public JSONObject encode() {
		Map<String, Object> response = Maps.newHashMap();
		response.put("__jsonclass__", "Response");
		if (error == RESPONSE_OK) {
			try {
				response.put("result", JSONRPC.encode(result));
			} catch (Exception e) {
				error = ILException.JSON_ENCODE_ERROR;
				errorMessage = ILException.lookupMessage(error);
				result = null;
			}
		}
		response.put("error", error);
		response.put("errorMessage", errorMessage);
		return JSONObject.fromObject(response);
	}



}
