/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.services;



import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.ProvisioningParameter;
import com.iloggr.gwt.util.client.ILException;

public interface ProvisioningServiceAsync {
	/**
	 * Gets the provisioning object that contains all of the provisioning parameters for the account
	 * application identified by the security token and application ID.
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @throws Exception
	 */
	public void getProvisioningParameters(String token, String appID,  AsyncCallback<List<ProvisioningParameter>> async);

	/**
	 * Returns the provisioning parameter object for a given account application
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @param name Provisioning Parameter Name
	 * @throws Exception
	 */
	public void getProvisioningParameter(String token, String appID, String name, AsyncCallback<ProvisioningParameter> async);

	/**
	 * Adds a new provisioning parameter to the application identified by appID.  Parameter name must be unique for the
	 * application.  Parameter values are always stored as Strings.
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @param value The string value to set
	 * @throws Exception
	 */
	public void addProvisioningParameter(String token, String appID, String paramName, String value, String type, boolean active, AsyncCallback<Void>async);

	/**
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @throws Exception
	 */
	public void deleteProvisioningParameter(String token, String appID, String paramName, AsyncCallback<Void>async);

	/**
	 * @param param 
	 * @throws Exception
	 */
	public void deleteProvisioningParameter(String token, ProvisioningParameter param, AsyncCallback<Void>async);

	/**
	 *  Updates the value of the specified application provisioning parameter to the new String value.
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @param value The string value to set
	 * @throws Exception
	 */
	public void updateProvisioningParameter(String token, String appID, String paramName, String value, String type, boolean active, AsyncCallback<Void>async);


	/**
	 * @param param
	 * @param value
	 * @param async
	 */
	public void updateProvisioningParameter(String token, ProvisioningParameter param, String value, String type, boolean active, AsyncCallback<Void>async);

	/**
	 * @param app
	 * @param counterName
	 * @param actionListener
	 */
	public void addApplicationCounter(String token, String appID, String counterName,  AsyncCallback<Void>async);
	
	/**
	 * @param token
	 * @param appID
	 * @param counterName
	 * @param async
	 */
	public void resetApplicationCounter(String token, String appID, String counterName,  AsyncCallback<Void>async);
	
	/**
	 * @param token
	 * @param appID
	 * @param counterName
	 * @param async
	 */
	public void deleteApplicationCounter(String token, String appID, String counterName,  AsyncCallback<Void>async);

	/**
	 * @param token
	 * @param appID
	 * @param async
	 * @return
	 * @throws Exception
	 */
	public void getApplicationCounters(String token, String appID, AsyncCallback<List<Counter>> async);

	/**
	 * Checks to see if the particular phone identified by the unique clientID has been blocked for this application.
	 *
	 * @param token Security token
	 * @param clientID Unique client ID for phone
	 * @param appID Application ID
	 * @throws Exception
	 */
	public void isAuthorized(String token, String clientID, String appID, AsyncCallback<Boolean>async);

	/**
	 * Registers the application identified by appID as blocked for the phone identified by the unique clientID.
	 *
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique client ID for phone
	 * @throws Exception
	 */
	public void disableApplicationOnPhone(String token, String appID, String clientID, AsyncCallback<Void>async);

	/**
	 * Get MOTD announcement for application.
	 * 
	 * @param token Security token
	 * @param appID Application ID
	 * @param async
	 */
	public void getAnnouncement(String token, String appID, AsyncCallback<String>async);
	
	/**
	 * @param token
	 * @param userID
	 * @param phoneClientID
	 * @param async
	 */
//	public void associateIDWithPhone(String token, String userID, String phoneClientID, AsyncCallback<Void>async);


}
