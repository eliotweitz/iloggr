/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ILException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 1L;

	int errorCode;

	// Return codes to webservice caller (< 0 is error)
	//  MUST THROW THESE VALUES AS EXCEPTIONS FOR JSONRPCRequest TO PROPERLY ENCODE RESPONSE
	public final static int OPERATION_SUCCESS = 0;
	public final static int OPERATION_FAILED = -1;
    public final static int UNKNOWN_ACCOUNT = -2;
    public final static int BAD_PASSWORD = -3;
    public final static int BAD_TOKEN = -4;
    public final static int ACCOUNT_LOCKED = -5;
    public final static int APPLICATION_NOT_OWNED_BY_ACCOUNT = -6;
    public final static int JSON_ENCODE_ERROR = -7;
    public final static int INVALID_SERVICE_REQUEST = -8;
    public final static int RECEIVED_EMPTY_MESSAGE_PAYLOAD = -9;
    public final static int MESSAGE_PARSE_ERROR = -10;
    public final static int IMPROPERLY_FORMED_REQUEST = -11;
    public final static int BAD_PARAMETER_TYPE = -12;
    public final static int UNKNOWN_PROVISIONING_PARAMETER = -13;
    public final static int UNKNOWN_APPLICATION = -14;
    public final static int ACCOUNT_ALREADY_EXISTS = -15;
    public final static int AUTHENTICATION_FAILED = -16;
    public final static int ACCOUNT_NOT_ACTIVATED = -17;
    public final static int UNKNOWN_APPLICATION_PROVISIONING = -18;
    public final static int DUPLICATE_APPLICATION_NAME = -19;
    public final static int UNKNOWN_APPLICATION_COUNTER_NAME = -20;
    public final static int APPLICATION_COUNTER_ALREADY_EXISTS = -21;
    public final static int INVITATION_CODE_INVALID = -22;
    public final static int INVITATION_CODE_IN_USE = -23;
 
    public final static int eIndices[] = {0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11,
    	-12, -13, -14, -15, -16, -17, -18, -19, -20, -21, -22, -23};

    // TODO(jsirois): these need to be localized - consider an ILExceptionFactory that gets a message bundle injected
    public final static String eMessages[] = {"Operation success", "Operation failed", "Unknown account",
    	"Bad password", "Bad account token", "Account locked", "Application not owned by account",
    	"JSON encode error", "Invalid service request", "Received empty message payload",
    	"Message parse error", "Improperly formatted request", "Bad pameter type",
    	"Unknown provisioning parameter", "Unknown application", "Account already exists",
    	"Authentication failed", "Account not activated", "Application provisioning Error",
    	"Duplicate application name for account", "Application counter does not exist", 
    	"Application counter already exists", "The invitation code is not valid, please contact support@iloggr.com",
    	"The invitation code has already been used, please contact support@iloggr.com"};


	public ILException() {
		// TODO Auto-generated constructor stub
	}

	public ILException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ILException(int message) {
//		super(Integer.toString(message));
		super(lookupMessage(message));
		errorCode = message;
	}

	public ILException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ILException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public static String lookupMessage(int errorCode) {
		for (int i=0; i<eIndices.length; i++) if (eIndices[i] == errorCode) return eMessages[i];
		// General failure
		return eMessages[1];
	}
	
	@Override
	public String getMessage() {
		return lookupMessage(errorCode);
	}

}
