/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.services;


import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.ProvisioningParameter;
import com.iloggr.gwt.util.client.ILException;

public interface ProvisioningService extends RemoteService {

	/**
	 * Gets the provisioning object that contains all of the provisioning parameters for the account
	 * application identified by the security token and application ID.
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @return Set Set of provisioning parameters
	 * @throws Exception
	 */
	public List<ProvisioningParameter> getProvisioningParameters(String token, String appID) throws Exception;

	/**
	 * Returns the provisioning parameter object for a given account application
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @param name Provisioning Parameter Name
	 * @return ProvisioningParameter Object
	 * @throws Exception
	 */
	public ProvisioningParameter getProvisioningParameter(String token, String appID, String name) throws Exception;

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
	public void addProvisioningParameter(String token, String appID, String paramName, String value, String type, boolean active) throws Exception;

	/**
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @throws Exception
	 */
	public void deleteProvisioningParameter(String token, String appID, String paramName) throws Exception;

	/**
	 * @param token
	 * @param param
	 * @throws Exception
	 */
	public void deleteProvisioningParameter(String token, ProvisioningParameter param) throws Exception;


	/**
	 *  Updates the value of the specified application provisioning parameter to the new String value.
	 *
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @param value The string value to set
	 * @throws Exception
	 */
	public void updateProvisioningParameter(String token, String appID, String paramName, String value, String type, boolean active)
		throws Exception;

	/**
	 * @param token
	 * @param param
	 * @param value
	 * @throws Exception
	 */
	public void updateProvisioningParameter(String token, ProvisioningParameter param, String value, String type, boolean active) throws Exception;

	/**
	 * Adds a new counter for the application.
	 * 
	 * @param app  Application
	 * @param counterName Name of the new counter
	 * @param actionListener
	 */
	public void addApplicationCounter(String token, String appID, String counterName) throws Exception;

	/**
	 * @param token
	 * @param appID
	 * @param counterName
	 * @throws Exception
	 */
	public void resetApplicationCounter(String token, String appID, String counterName) throws Exception;

	/**
	 * @param token
	 * @param appID
	 * @param counterName
	 * @throws Exception
	 */
	public void deleteApplicationCounter(String token, String appID, String counterName) throws Exception;
	/**
	 * @param token
	 * @param appID
	 * @return
	 * @throws Exception
	 */
	public List<Counter> getApplicationCounters(String token, String appID) throws Exception;
	
	/**
	 * Checks to see if the particular phone identified by the unique clientID has been blocked for this application.
	 *
	 * @param token Security token
	 * @param clientID Unique client ID for phone
	 * @param appID Application ID
	 * @return boolean true if this clientID is approved for the application
	 * @throws Exception
	 */
	public ProvisioningParameter isAuthorized(String token, String clientID, String appID) throws Exception;

	/**
	 * Registers the application identified by appID as blocked for the phone identified by the unique clientID.
	 *
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique client ID for phone
	 * @throws Exception
	 */
	public void disableApplicationOnPhone(String token, String appID, String clientID) throws Exception;
	
	/**
	 * Get MOTD announcement for application.
	 * 
	 * @param token Security token
	 * @param appID Application ID
	 * @return
	 * @throws Exception
	 */
	public ProvisioningParameter getAnnouncement(String token, String appID) throws Exception;
	
	/** TODO - Make it so you can have multiple apps with different
	 * @param token Security token
	 * @param userID User generated id
	 * @param clientID Unique phone ID
	 * @throws Exception
	 */
//	public void associateIDWithPhone(String token, String userID, String phoneClientID) throws Exception;

}