package com.iloggr.client.services;
/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */

import java.util.Date;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.gwt.util.client.ILException;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface AccountServiceAsync {

	/**
	 * Activates an account after sign up.  This is used internally when user clicks on link verifying
	 * their email account.
	 *
	 * TODO: This method should be switched to private after testing is completed.
	 *
	 * @param id The ID for the account
	 * @param emailToken The email token sent via email to identify this account holder
	 * @throws Exception
	 */
	public void activateAccount(long id, String emailToken, AsyncCallback<Void> async);

	/**
	 * Creates a new account given a unique name, password and contact information
	 *
	 * @param name Unique name for the account
	 * @param email Account email where all tokens, requests will be send
	 * @param password A password for the account (currently sent in the clear)
	 * @param phone A contact phone number
	 * @throws Exception
	 */
	public void createAccount(String code, String name, String email, String password, String phone, AsyncCallback<Void> async);

	/**
	 * Checks the account name against the password and if correct return the account object
	 *
	 * 	TODO: This method should be switched to private after testing is completed.
	 *
	 * @param accountEmail Unique name for the account
	 * @param password The password for the account sent in the clear
	 * @throws Exception
	 */
	public void authenticateAccount(String accountEmail, String password, AsyncCallback<Account> callback);

	/**
	 * Checks the security token to see if it matches an account then returns the account object
	 * representing that account.
	 *
	 * @param token Security token
	 * @throws Exception
	 */
	public void authenticateAccount(String token, AsyncCallback<Account> callback);

	/**
	 * Generates an email containing an account's security token and send it to the email registered
	 * for the account.
	 *
	 * @param acctName The unique name for the account
	 * @param password The password for the account sent in the clear
	 * @throws Exception
	 */
//	public void emailSecurityToken(String acctName, String password, AsyncCallback<Void> async);

	/**
	 * Generates an email containing the application ID corresponding to an application name
	 * associated with the account identified by the security token.
	 *
	 * @param securityToken Security token
	 * @param applicationName Name of application
	 * @throws Exception
	 */
//	public void emailApplicationID(String securityToken, String applicationName, AsyncCallback<Void> async);

	/**
	 * Creates a new application for the account identified by the security token.
	 *
	 * @param securityToken Security token
	 * @param appName The application name
	 * @param releaseDate Release date for the application
	 * @throws Exception
	 */
	public void createNewApplication(String securityToken, String appName, Date releaseDate, AsyncCallback<Application> async);

	/**
	 * @param securityToken
	 * @param appID
	 * @param async
	 */
	public void deleteApplication(String securityToken, String appID, AsyncCallback<Void> async);


	/**
	 * @param token
	 * @param name
	 * @param releaseDate
	 * @param announcement
	 * @param appID
	 * @param callback
	 */
	public void updateApplication(String token, String name, Date releaseDate, String announcement, String appID, AsyncCallback<Application> callback);

	/**
	 * Gets the set of applications registered for the account identified by the security token.
	 *
	 * @param token Security token
	 * @throws Exception
	 */
	public void getApplications(String token, AsyncCallback<Set<Application>> callback);


	/**
	 * Performs an email announcement to all account holders.
	 *
	 * @param code Security code
	 * @param people Space delimited list of account names
	 * @param subject The email subject
	 * @param message The message body
	 * @throws Exception
	 */
	public void sendEmailAnnouncement(String code, String people, String subject, String message,
			AsyncCallback<Void> async);

	/**
	 * Resets the password for the account identified by the security token.  Old password must match the existing
	 * password.
	 *
	 * @param token Security token
	 * @param oldPassword  Old password (clear text)
	 * @param newPassword New password (clear text)
	 * @throws Exception
	 */
	public void resetPassword(String token, String oldPassword, String newPassword, AsyncCallback<Void> async);

	/**
	 * Resets the password for the account identified by the security token.  
	 *
	 * @param token Security token
	 * @param newPassword New password (clear text)
	 * @throws Exception
	 */
	public void resetPassword(String token, String newPassword, AsyncCallback<Void> async);

	
	/**
	 * Send a reset password email IF the email is registered otherwise returns an ILException
	 *
	 * @param email The email of the account holder
	 * @throws Exception
	 */
	public void resetPasswordEmail(String email, AsyncCallback<Void> async);
	
	/**
	 * Generates an iloggr.plist file for download and client usage
	 * @param token Security token
	 * @param appID Unique application ID
	 */
	public void getApplicationIDFileXML(String token, String appID, AsyncCallback<String> async);

}


