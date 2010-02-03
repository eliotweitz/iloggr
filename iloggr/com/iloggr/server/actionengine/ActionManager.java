/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.actionengine;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.iloggr.client.model.Account;
import com.iloggr.client.model.Action;
import com.iloggr.server.managers.AccountManager;
import com.iloggr.util.HibernateUtil;


/**
 * The ActionManager class generates actions (email notifications) and stores them in the action table which
 * is a queue for the ActionEngine to process.
 * 
 * @author eliot
 * @version 1.0
 * @see Action
 * @see ActionEngine
 *
 */
public class ActionManager {

	static ActionEngine ae = new ActionEngine();
	static Thread aeThread ;
	// Set up a simple configuration that logs on the console.
	private static final Logger log = Logger.getLogger(ActionManager.class);

	// TODO: Fix for production
//	public static String webURL = "http://www.iloggr.net"; 
	public static String restUrl = "http://iloggr.com/rest/"; 
	private Session session;

	// start the action engine on a separate thread.  It will send the emails
	// by searching the database periodically for action that need to be executed
	static {
		aeThread = new Thread(ae);
		aeThread.start();
}

	public ActionManager(Session session) {
		if (this.session != null && this.session != session) this.closeSession();
		this.session = session;
	}
	
	public ActionManager() {
		this.session = 	 HibernateUtil.getSessionFactory().openSession();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		closeSession();
	  super.finalize(); //not necessary if extending Object.
	} 

	public void closeSession() {
		if (session.isOpen()) session.close();
	}


	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Generates an email action.
	 * 
	 * @param person Account object that the email will be sent to.
	 * @param subject The subject for the email.
	 * @param msg The email message body.
	 */
	public void emailAcct(Account person, String subject, String msg) {
		if (person == null || msg == null || msg.length() == 0) return;
		Date now = new Date();
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			Action action = new Action(Action.TYPE_EMAIL, now, person, subject, msg);
			session.save(action);
			if (!existsTransaction) session.getTransaction().commit();
		} catch (Exception e) {
			log.error("ActionManager: emailEventParticipants failure: " + e.toString());
			if (session.getTransaction().isActive()) session.getTransaction().rollback();
		}
	}
	
	/**
	 * Sends a reset password email to the owner of an account.
	 * 
	 * @param person The account object to send the reset email.
	 */
	public void sendResetPasswordEmail(Account acct) {
		// Generate a unique security token
		String token = AccountManager.generateEmailToken(acct);
		acct.setEmailToken(token);
		String subject = "Request to reset your iLoggr account password";
		String aURL = getResetTokenURL(token, acct.getId());
		StringBuffer msg = new StringBuffer();
		msg.append("You have indicated to us that you forgot your password.  This email will help you");
		msg.append(" reset it.  \n\n");
		msg.append("Please click on the following link to reset your iLoggr account password:\n\n");
		msg.append(aURL);
		Date now = new Date();
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			session.update(acct);
			Action action = new Action(Action.TYPE_EMAIL, now, acct, subject, msg.toString());
			session.save(action);
			if (!existsTransaction) session.getTransaction().commit();
		} catch (Exception e) {
			log.error("ActionManager: sendResetPasswordEmail failure: " + e.toString());
			if (session.getTransaction().isActive()) session.getTransaction().rollback();
		}
	}
	

	/**
	 * Sends an activation email to the account owner.
	 * 
	 * @param person Account object.
	 */
	public void sendActivationEmail(Account person) {
		if (person == null) return;
		Date now = new Date();
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			person.setEmailToken(AccountManager.generateEmailToken(person));
			session.saveOrUpdate(person);
			String msg = createActivationMessage(person);
			Action action = new Action(Action.TYPE_EMAIL, now, person,
					"Activate your new iLoggr account", msg);
			session.save(action);
			if (!existsTransaction) session.getTransaction().commit();
		} catch (Exception e) {
			log.error("ActionManager: sendActivationEmail failure: " + e.toString());
			if (session.getTransaction().isActive()) session.getTransaction().rollback();
		}
	}
	
	private static String createActivationMessage(Account who) {
		String token = who.getEmailToken();
		String aURL = getActivationTokenURL(token, who.getId());
		StringBuffer msg = new StringBuffer();
		msg.append("Hello " + who.getName() + ",\n");
		msg.append('\n');
		msg.append("Thanks for signing up to use iLoggr.\n\nPlease click the link below to activate your account.\n");
		msg.append('\n');
		msg.append("Click: ");
		msg.append(aURL);
		msg.append(" \nto activate your account.  If you are having problems activating your account, please send email to support@iloggr.com for assistance.");
		return msg.toString();
	}
	
	/**
	 * Generates the activation URL.
	 * 
	 * @param token Email token that identifies the account
	 * @param id The ID of the account
	 * @return URL for activation
	 */
	public static String getActivationTokenURL(String token, long id) {
		if (token == null || token.length() == 0) return ""; // TODO - this isn't good
		return restUrl + "account?cmd=activate&id=" + id + "&token=" + token;
		}
	
		
	/**
	 * Generates a password reset URL for an email to an account.
	 * 
	 * @param token The email token that identifies the account
	 * @return URL for password reset
	 */
	public static String getResetTokenURL(String token, long id) {
		if (token == null || token.length() == 0) return ""; // TODO - this isn't good
		return restUrl + "account?cmd=resetpassword&id=" + id + "&token=" + token;
	}
	

	/**
	 * Marks an action as executed so it does not execute again.
	 * 
	 * @param action The action object.
	 */
	public void markExecuted(Action action) {
		try {
			session.beginTransaction();
			action.setExecuted(true);
			session.saveOrUpdate(action);
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Exception in ActionManager:markExecuted: " + e.toString());
			// Need to suspend remove the action or it will be executed over and over
			// TODO : what is best way?
		} 
	}




}
