/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.InvitationCode;
import com.iloggr.client.services.AccountService;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.server.actionengine.ActionManager;
import com.iloggr.server.managers.AccountManager;

/**
 * This class provides the functionality for the Account service.  The account service does everything pertaining
 * to user accounts.  It signs up new users, allows them to add applications and change passwords.
 *
 * This service is intended to be used through JSON web services from the internet, not the phone application.
 *
 * @author eliot
 * @version 1.0
 */
public class AccountServiceImpl implements AccountService {

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
	public void activateAccount(long id, String emailToken) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = null;
		acct = am.getAccount(id);
		if (acct == null) {
			am.closeSession();
			throw new ILException(ILException.UNKNOWN_ACCOUNT);
		}
		String stoken = acct.getEmailToken();
		if (!stoken.equalsIgnoreCase(emailToken)) {
			am.closeSession();
			throw new ILException(ILException.BAD_TOKEN);
		}
		// activate
		acct.setStatus(Account.STATUS_ACTIVE);
		am.saveOrUpdateAccount(acct);
		// send them their security token
	//	emailSecurityToken(acct);
		am.closeSession();
	}

 	/**
 	 * Creates a new account given a unique name, password and contact information
 	 *
	 * @param name Unique name for the account
	 * @param email Account email where all tokens, requests will be send
	 * @param encryptedPassword A password for the account (encrypted)
	 * @param phone A contact phone number
	 * @throws Exception
	 */
	public void createAccount(String code, String name, String email, String encryptedPassword, String phone) throws Exception {
		name = name.trim();
		AccountManager am = new AccountManager();
		Account acct = null;
		// get the code
		InvitationCode ic = am.getInvite(code);
		// First see if user exists
		acct = am.getAccountByEmail(email);
		if (acct != null) {
			if (acct.getStatus() == Account.STATUS_ACTIVE) {
				am.closeSession();
				throw new ILException(ILException.ACCOUNT_ALREADY_EXISTS);
			} else if (ic == null || ic.getAccount() != acct) {
				am.closeSession();
				throw new ILException(ILException.INVITATION_CODE_INVALID);
			} else { // Not active, reuse existing account with email
				acct.setName(name);
				acct.setPassword(encryptedPassword);
				acct.setPhoneNumber(phone);
				am.saveOrUpdateAccount(acct);
				ActionManager actm = new ActionManager(am.getSession());
				// Send them another email to activate
				actm.sendActivationEmail(acct);
			}
		} else if (ic == null) {
			am.closeSession();
			throw new ILException(ILException.INVITATION_CODE_INVALID);
		} else if (ic.getAccount() != null) {
			am.closeSession();
			throw new ILException(ILException.INVITATION_CODE_IN_USE);
		} else {
			acct = am.createAccount(ic, name,  encryptedPassword,  email,  phone);
		}
		am.closeSession();
	}

	/**
	 * Checks the account name against the password and if correct return the account object
	 *
	 * 	TODO: This method should be switched to private after testing is completed.
	 *
	 * @param accountEmail Unique name for the account
	 * @param encryptedPassword The password for the account (encrypted)
	 * @return Account The account object
	 * @throws Exception
	 */
	public Account authenticateAccount(String accountEmail, String encryptedPassword) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = am.authenticateAccount(accountEmail, encryptedPassword);
		if (acct == null) {
			am.closeSession();
			throw new ILException(ILException.UNKNOWN_ACCOUNT);
		}
		if (acct.getStatus() == Account.STATUS_INACTIVE) throw new ILException(ILException.ACCOUNT_NOT_ACTIVATED);
		return acct;
	}

	/**
	 * Checks the security token to see if it matches an account then returns the account object
	 * representing that account.
	 *
	 * @param token Security token
	 * @return Account The account object
	 * @throws Exception
	 */
	public Account authenticateAccount(String token) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = am.authenticateAccount(token);
		if (acct == null) {
			am.closeSession();
			throw new ILException(ILException.UNKNOWN_ACCOUNT);
		}
		if (acct.getStatus() == Account.STATUS_INACTIVE) throw new ILException(ILException.ACCOUNT_NOT_ACTIVATED);
		return acct;
	}

	/**
	 * Generates an email containing an account's security token and send it to the email registered
	 * for the account.
	 *
	 * @param accountEmail The unique name for the account
	 * @param encryptedPassword The password for the account (encrypted)
	 * @throws Exception
	 */
	/*
	public void emailSecurityToken(String accountEmail, String encryptedPassword) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = am.authenticateAccount(accountEmail, encryptedPassword);
		if (acct == null) {
			am.closeSession();
			throw new ILException(ILException.OPERATION_FAILED);
		}
		if (acct.getStatus() == Account.STATUS_INACTIVE) throw new ILException(ILException.ACCOUNT_NOT_ACTIVATED);
		emailSecurityToken(acct);
		am.closeSession();
	}*/

	/**
	 * Generates an email containing the application ID corresponding to an application name
	 * associated with the account identified by the security token.
	 *
	 * @param securityToken Security token
	 * @param applicationName Name of application
	 * @throws Exception
	 *//*
	public void emailApplicationID(String securityToken, String applicationName) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = am.authenticateAccount(securityToken);
		if (acct == null) {
			am.closeSession();
			throw new ILException(ILException.OPERATION_FAILED);
		}
		Application app = am.getApplication(acct, applicationName);
		emailApplicationID(acct, app);
		am.closeSession();
	}
*/
	/**
	 * Sends an email with the application ID for the given account and application object.
	 *
	 * @param acct The Account object
	 * @param app The Application object
	 *//*
	void emailApplicationID(Account acct, Application app) {
		ActionManager actionManager = new ActionManager();
		StringBuffer msg = new StringBuffer();
		msg.append("Hello ");
		msg.append(acct.getName());
		msg.append(",\n\n");
		msg.append("Here is the ID number for your application ");
		msg.append(app.getName());
		msg.append("\nID: ");
		msg.append(app.getAppID());
		actionManager.emailAcct(acct, "Application ID", msg.toString());
		actionManager.closeSession();
	}*/

	/**
	 * Send an email the security token for the account.
	 *
	 * @param acct The account object to send the email to.
	 * @throws Exception
	 *//*
/*	void emailSecurityToken(Account acct)  throws Exception {
		if (acct == null) throw new ILException(ILException.UNKNOWN_ACCOUNT);
		ActionManager am = new ActionManager();
		StringBuffer msg = new StringBuffer();
		msg.append("Hello ");
		msg.append(acct.getName());
		msg.append(",\n\n");
		msg.append("Thanks for signing up for iLoggr.  Your account is now active.");
		msg.append("\n\n");
		msg.append("Here is your security token that you must use when calling the service from your iPhone applications:");
		msg.append("\n\n");
		msg.append(acct.getSecurityToken());
		msg.append("\n\n");
		msg.append("Since changing this token can cause iPhone applications already deployed not to access the iLoggr service, you can only change it by making a requirest through support: support@iLoggr.com");
		msg.append("\n");
		msg.append("Please email support with any question or issues you may have with the service.");
		am.emailAcct(acct, "Your account is now activated", msg.toString());
		am.closeSession();
	}*/

	/**
	 * Resets the password for the account identified by the security token.  Old password must match the existing password.
	 *
	 * @param token Security token
	 * @param oldPassword  Old password (encrypted)
	 * @param newPassword New password (encrypted)
	 * @throws Exception
	 */
	public void resetPassword(String token, String oldPassword, String newPassword) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = am.checkToken(token);
		if (!oldPassword.equalsIgnoreCase(acct.getPassword())) {
			am.closeSession();
			throw new ILException(ILException.BAD_PASSWORD);
		}
		acct.setPassword(newPassword);
		acct.setEmailToken("");
		am.saveOrUpdateAccount(acct);
		am.closeSession();
	}
	
	/**
	 * Resets the password for the account identified by the security token.  
	 * 
	 * @param token Security token
\	 * @param newPassword New password (encrypted)
	 * @throws Exception
	 */
	public void resetPassword(String token, String newPassword) throws Exception {
		AccountManager am = new AccountManager();
		// will throw exception if does not authenticate
		Account acct = am.checkToken(token);
		acct.setPassword(newPassword);
		acct.setEmailToken("");
		am.saveOrUpdateAccount(acct);
		am.closeSession();
	}


	/**
	 * Send a reset password email IF the email is registered otherwise returns an ILException
	 *
	 * @param email The email of the account holder
	 * @throws Exception
	 */
	public void resetPasswordEmail(String email) throws Exception {
		AccountManager am = new AccountManager();
		try {
			Account acct = am.getAccountByEmail(email);
			if (acct == null) {
				am.closeSession();	
				return;
			}
			ActionManager acm = new ActionManager(am.getSession());
			acm.sendResetPasswordEmail(acct);
		} catch (Exception e) {
			throw e;
		} finally {
			am.closeSession();
		}
	}

	// TODO need additional parameters
	/**
	 * Creates a new application for the account identified by the security token.
	 *
	 * @param securityToken Security token
	 * @param appName Application name
	 * @throws Exception
	 */
	public Application createNewApplication(String securityToken, String appName, Date releaseDate) throws Exception {
		AccountManager am = new AccountManager();
		Account acct = am.checkToken(securityToken);
		if (acct.getApplicationNamed(appName) != null) throw new ILException(ILException.DUPLICATE_APPLICATION_NAME);
		Application app = am.createApplication(acct, appName, releaseDate, null);
	    // send new application id to user
		if (app == null) throw new ILException(ILException.OPERATION_FAILED);
		if (acct.getStatus() == Account.STATUS_INACTIVE) throw new ILException(ILException.ACCOUNT_NOT_ACTIVATED);
//		emailApplicationID(acct, app);
		am.closeSession();
		return app;
	}


	/**
	 * Gets the set of applications registered for the account identified by the security token.
	 *
	 * @param token Security token
	 * @return Set The set of applications
	 * @throws Exception
	 */
	public Set<Application> getApplications(String token) throws Exception {
		AccountManager am = new AccountManager();
		Set<Application> result = null;
		Account acct = am.checkToken(token);
		result = acct.getApplications();
		if (acct.getStatus() == Account.STATUS_INACTIVE) throw new ILException(ILException.ACCOUNT_NOT_ACTIVATED);
		am.closeSession();
		return result;
	}

	public void updateAccount() {
		// TODO placeholder
	}

	public void updateApplication() {
		// TODO plaeholder
	}
	
	/**
	 * @param token Security token for account
	 * @param appID Unique ID of Application to remove
	 */
	public void deleteApplication(String token, String appID) throws Exception {
		AccountManager am = new AccountManager();
		Application app = am.checkAppOwnedByAcct(token, appID);
		Account acct = app.getAccount();
		am.removeAppFromAccount(app, acct);
		am.closeSession();
	}

	/*	private String name;
	private Account account;
	private Date releaseDate;
	private Provisioning provisioning;
*/
	public Application updateApplication(String token, String name, Date releaseDate, String announcement, String appID) throws Exception {
		AccountManager am = new AccountManager();
		Application app = am.checkAppOwnedByAcct(token, appID);
		app.setName(name);
		app.setReleaseDate(releaseDate);
		app.setAnnouncement(announcement);
		am.saveOrUpdateApplication(app);
		am.closeSession();
		return app;
	}
	
	// people is comma delimited set of user names. If "All" then send email to everyone
	/**
	 * Performs an email announcement to all account holders.
	 *
	 * @param code Security code
	 * @param people Space delimited list of account names
	 * @param subject The email subject
	 * @param message The message body
	 * @throws Exception
	 */
	public void sendEmailAnnouncement(String code, String people, String subject, String message) throws Exception {
		if (code == null || !code.equals("*jH8SD28HD#$BKJ229ujdjd2929c")) return;
		AccountManager am = new AccountManager();
		if (people.trim().equalsIgnoreCase("all")) { // Send to everyone
			am.sendEmailAnnouncement(null, subject, message);
		} else {
			HashSet<Account>peeps = new HashSet<Account>();
			String[] emails = people.split(" ");
			for (String email : emails) {
				Account acct = am.getAccountByEmail(email);
				if (acct != null) peeps.add(acct);
			}
			am.sendEmailAnnouncement(peeps, subject, message);
		}
		am.closeSession();
	}
	
	public String getApplicationIDFileXML(String token, String appID) throws Exception {
		StringBuffer result = new StringBuffer();
		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		result.append("<plist version=\"1.0\">");
		result.append("<dict>");
		result.append("<key>AppID</key><string>");
		result.append(appID);
		result.append("</string>");
		result.append("<key>AccountSecurityToken</key><string>");
		result.append(token);
		result.append("</string>");
		result.append("</dict>");
		result.append("</plist>");
		return result.toString();
	}


	
}

