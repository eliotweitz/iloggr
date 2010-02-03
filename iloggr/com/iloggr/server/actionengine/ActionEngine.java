/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.actionengine;


import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.iloggr.client.model.Account;
import com.iloggr.client.model.Action;
import com.iloggr.util.HibernateUtil;


/**
 * The ActionEngine runs on a separate thread and processes actions stored in the action table periodically.  
 * The actions represent email that are sent out to users.
 * 
 * @author eliot
 * @version 1.0
 * @see ActionManager
 * @see Action
 *
 */
/**
 * @author eliot
 *
 */
class ActionEngine implements Runnable {

	static int statCount = 0;
	// TODO: Change to False to disable entire engine
	private static boolean enableActionEngine = true;
	/* TODO: Change to "False" to disable messaging for testing */
	private static final boolean enableMsgs = true;

	// Set up a simple configuration that logs on the console.
	private static final Logger log = Logger.getLogger(ActionEngine.class);
	private static final String mailHost = "mail.authsmtp.com";
	private static javax.mail.Session mailSession;
	public static final String DOMAINNAME = "iloggr.net"; // flag

	    private static final String user = "ac38284";
	    private static final String password = "Bump911";
	  
	    static {  
	 //      Security.addProvider(new org.apache.harmony.xnet.provider.jsse.JSSEProvider());  
	 //   	Security.addProvider(com.sun.net.ssl.internal.ssl.Provider());
	//        Security.addProvider(new sun.security.provider.Sun()); 	
	 
	    	
	 // TODO   	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
  
	  
	        Properties props = new Properties();  
	        props.setProperty("mail.transport.protocol", "smtp");  
	        props.setProperty("mail.host", mailHost);  
	        props.put("mail.smtp.auth", "true");  
//  TODO      props.put("mail.smtp.port", "465");  
//	TODO        props.put("mail.smtp.socketFactory.port", "465");  
	        props.put("mail.smtp.port", "2525");  
	        props.put("mail.smtp.socketFactory.port", "2525");  
//TODO	        props.put("mail.smtp.socketFactory.class",  
//TODO	                "javax.net.ssl.SSLSocketFactory");  
//TODO	        props.put("mail.smtp.socketFactory.fallback", "false");  
	        props.setProperty("mail.smtp.quitwait", "false");  
//	        props.put("mail.smtp.starttls.enable","true");
	     	  

	//		mailSession = javax.mail.Session.getInstance(props);
	        mailSession = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password);
			}
			});
		log.info("Action Engine Initializing");
	}

	public void run() {
		while (enableActionEngine) {
			try {
				Thread.sleep(5000);
				ActionEngine.processActions();
			} catch (Exception e) {
				log.info("ActionEngine - Exception in run loop: "
						+ e.toString());
			}
		}
	}

	/**
	 * Execute actions that have not yet been executed.
	 * 
	 */
	public static void processActions() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			Criteria crit = session.createCriteria(Action.class);
			crit.add(Restrictions.eq("executed", false));
			crit.addOrder(Property.forName("date").asc());
			crit.setMaxResults(10);
			@SuppressWarnings("unchecked")
			List<Action> actions = crit.list();
			session.getTransaction().commit();
			if (actions.isEmpty()) {
				// log.info("ActionEngine: No actions to process");
			}
			// session.getTransaction().commit();
			for (Action a : actions) {
				processAction(a);
			}
		} catch (Exception e) {
			log.error("ActionEngine: processActions failure: " + e.toString());
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	private static void processAction(Action action) {
		try {
			switch (action.getType()) {
			case Action.TYPE_EMAIL:
				sendEmail(action);
				log.info("Processed email action: " + action.getSubject());
				break;
			case Action.TYPE_TEXT:
//				sendText(action);
				log.info("Processed text action: " + action.getMessage());
				break;
			case Action.TYPE_STATUS:
//				updateStatus(action);
//				log.info("Processed status action: " + action.getEvent());
			default:
			}
		} catch (Exception e) {
			log.error("ActionEngine: processAction failure: " + e.toString());
		}
	}

	
	private static void sendEmail(Action action) {
		try {
			String msg = action.getMessage();
			String subject = action.getSubject();
			Account person = action.getPerson();
			if (subject == null || subject.length() == 0)
				subject = person.getName()+" Update";
			if (msg == null || msg.length() == 0)
				return;
			sendEmail(person, subject, msg);
			ActionManager am = new ActionManager();
			am.markExecuted(action);
			am.closeSession();
			} catch (Exception e) {
			log.error("ActionEngine: sendEmail failure: " + e.toString());
			} 
	}
	
	
	/**
	 * Sends and email to an account.
	 * 
	 * @param acct Account object
	 * @param subject Email subject
	 * @param msg The message body
	 */
	private static void sendEmail(Account acct, String subject, String msg) {
		if (acct == null)
			return;
		try {
			MimeMessage message = new MimeMessage(mailSession);
			// message.setContent("Hello", "text/plain");
			message.setText(msg);
			message.setSubject(subject);
			String from = "notification@iloggr.com"; //+DOMAINNAME;
			Address address[] = { new InternetAddress(from, "From iLoggr")};
			message.setFrom(address[0]);
			message.setReplyTo(address);
			// Add recipients
			Address addr = new InternetAddress(acct.getEmail());
			message.addRecipient(Message.RecipientType.TO, addr);
			/* SEND EMAIL */
			if (enableMsgs)
				Transport.send(message);
			log.info("ActionEnginer: sendEmail success: " + message.toString());
		} catch (Exception e) {
			log.error("ActionEngine: sendEmail failure: " + e.toString());
		}

	}

	

}
