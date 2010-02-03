/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.managers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.InvitationCode;
import com.iloggr.client.model.Phone;
import com.iloggr.client.model.Provisioning;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.server.actionengine.ActionManager;
import com.iloggr.server.util.TokenGenerator;
import com.iloggr.util.HibernateUtil;


/**
 * @author eliot
 *
 */
public class AccountManager {

	// Set up a simple configuration that logs on the console.
	static final Logger log = Logger.getLogger(AccountManager.class);

	private Session session;


	public AccountManager() {
		this.session = HibernateUtil.getSessionFactory().openSession();
	}

	public AccountManager(Session session) {
		if (this.session != null && this.session != session) this.closeSession();
		this.session = session;
	}

	@Override
	protected void finalize() throws Throwable {
		closeSession();
		super.finalize(); // not necessary if extending Object.
	}

	public void closeSession() {
		if (session.isOpen())
			session.close();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		if (session != null) session.close();
		this.session = session;
	}


	/**
	 * Get the account by ID first, if defined, then by name.
	 * @param acct The account object
	 * @return Account object
	 */
	public Account getAccount(Account acct) {
		Account findPerson = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {

			if (acct.getId() != null)
				findPerson = (Account) session.get(Account.class, acct.getId());
			if (findPerson != null && acct.getEmail() != null) {
				findPerson = getAccountByEmail(acct.getEmail());
			}
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log
			.info("AccountManager: getAccount(Account) failed: "
					+ e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
		}
		return findPerson;
	}


	public Account getAccount(long id) {
		Account acct = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			acct = (Account) session.get(Account.class, id);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: getAccount (id) failed: " + e.toString());
		}
		return acct;
	}

	public Account getAccountByEmail(String email) {
		Account acct = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<Account> results = session.createCriteria(Account.class).add(
					Restrictions.eq("email", email)).list();
			if (results.size() > 1)
				log.info("**** more than two accts with email: " + email);
			for (Account p : results)
				if (p.getStatus() != Account.STATUS_INACTIVE)
					acct = p;
			if (acct == null && results.size() > 0)
				acct = results.get(0);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: getAccountByEmail failed: " + e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
		}
		return acct;
	}

	/**
	 * Get the account identified by the email token.  Make sure it matches the account of the given name.
	 * This method is used for sign up.
	 *
	 * @param email Email for the account
	 * @param emailToken Email token sent to account for activation
	 * @return The account object
	 */
	public Account getAccountWithToken(String email, String emailToken) {
		if (email == null || emailToken == null)
			return null;
		Account acct = getAccountByEmail(email);
		if (acct == null)
			return null;
		if (!emailToken.equalsIgnoreCase(acct.getEmailToken()))
			return null;
		return acct;
	}

	// Must return null if account not found
	/**
	 * Get the account idendified by the given security token.
	 * @param token Security token for account.
	 */
	public Account authenticateAccount(String token) {
		Account acct = null;
		if (token == null || token.equalsIgnoreCase("null"))
			return null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<Account> results = session.createCriteria(Account.class).add(
					Restrictions.eq("securityToken", token)).list();
			if (!results.isEmpty())
				acct = results.get(0);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: getSecurityTokenAccount failed: "
					+ e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
		}
		return acct;
	}
	
	public Application checkAppOwnedByAcct(String token, String appID) throws Exception {
		Account acct = checkToken(token);
		if (appID == null) return null;
		//  See if the application belongs to the account
		Application app = getApplication(appID);
		if (app == null) throw new ILException(ILException.UNKNOWN_APPLICATION);
		if (!app.ownedBy(acct)) {
			closeSession();
			throw new ILException(ILException.APPLICATION_NOT_OWNED_BY_ACCOUNT);
		}
		return app;
	}
	
	public Account checkToken(String token) throws Exception {
		Account acct = authenticateAccount(token);
		if (acct == null) {
			closeSession();
			throw new ILException(ILException.BAD_TOKEN);
		}
		return acct;
	}



	// Must return null if account not found
	/**
	 * Get an account using the provided security token.
	 *
	 * @param token Security token.
	 * @return The account object.
	 */
	public Account getTokenAccount(String token) {
		Account acct = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<Account> results = session.createCriteria(Account.class).add(
					Restrictions.eq("emailToken", token)).list();
			if (!results.isEmpty())
				acct = results.get(0);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: getTokenPerson failed: " + e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
		}
		return acct;
	}

	/**
	 * Get an account identified by account name and password.
	 *
	 * @param accountEmail Account email (unique identifier for account)
	 * @param password Password in the clear
	 * @return The account object
	 * @throws Exception
	 */
	public Account authenticateAccount(String accountEmail, String password) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			SimpleExpression loginSearch = Restrictions.eq("email", accountEmail);
			@SuppressWarnings("unchecked")
			List<Account> results = session.createCriteria(Account.class).add(
					loginSearch).list();
			if (results.isEmpty()) {
				if (!existsTransaction)
					if (session.getTransaction().isActive())
						session.getTransaction().rollback();
				throw new ILException(ILException.UNKNOWN_ACCOUNT);
			} else {
				Account acct = results.get(0);
				if (acct.getPassword() != null) {
					// Check password
					if (acct.getPassword() == null || !acct.getPassword().equals(password)) {
						throw new ILException(ILException.BAD_PASSWORD);
						// generate a security token if they don't already have one
					}
					return acct;
				}
			}
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: authenticateUser (name) failed: "
					+ e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
			throw e; // throw up
		}
		return null;
	}

	public void saveAccount(Account acct) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.save(acct);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager saveAccount failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}


	public void saveOrUpdateAccount(Account acct) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.saveOrUpdate(acct);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log
			.info("AccountManager saveOrUpdateAccountfailed: "
					+ e.toString());
			throw e;
		}

	}

	public void saveOrUpdateApplication(Application app) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.saveOrUpdate(app);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log
			.info("AccountManager saveOrUpdateApplicationfailed: "
					+ e.toString());
			throw e;
		}

	}



	/**
	 * Created a new account given a name, password and contact information.
	 *
	 * @param name Unique name for the account
	 * @param password A password (in the clear)
	 * @param email Primary contact email for the account.
	 * @param phone Primary contact phone number for the account.
	 * @return The account object
	 * @throws Exception
	 */
	public Account createAccount(InvitationCode code, String name, String password, String email, String phone) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		Account account = new Account(code, name, password, email, phone);
		try {
			// Look up the invite code
			// Generate a security token for the person
			account.setSecurityToken(generateSecurityToken(account));
			// Generate an email token for the person
			account.setEmailToken(generateEmailToken(account));
			// Set status to inactive
			account.setStatus(Account.STATUS_INACTIVE);
			account.setEmailToken(generateEmailToken(account));
			account.setInviteCode(code);
			code.setAccount(account);
			session.save(account);
//			session.saveOrUpdate(code); should not be necessary with cascade
			if (!existsTransaction) session.getTransaction().commit();
			ActionManager am = new ActionManager(session);
			am.sendActivationEmail(account);
			Account me = getAccountByEmail("eweitz@gmail.com");
			//  send email to eliot that someone actually signed up
			if (me != null) am.emailAcct(me, "Someone registered for iLoggr!!  username: ", account.getName() +  ", email: "+ account.getEmail());
		} catch (Exception e) {
			if (session.getTransaction().isActive()) session.getTransaction().rollback();
			throw e;
		}
		return account;
	}
	
	public InvitationCode getInvite(String code)  throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		InvitationCode result = null;
		if (!existsTransaction) session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<InvitationCode> results = session.createCriteria(InvitationCode.class).add(Restrictions.eq("code", code)).list();
			if (results.size() == 0) return null;
			if (results.size() > 1)
				log.info("**** more than one invite identical invite codes: " + code);
			result = results.get(0);
			if (!existsTransaction) session.getTransaction().commit();
		} catch (Exception e) {
			log
			.info("AccountManager getInvite exception: "
					+ e.toString());
			throw e;
		}
		return result;
	}


	/**
	 * Creates an new application for a given account.  Provisioning information is optional
	 * @param acct Account object
	 * @param appName New name for the application
	 * @param creationDate Date of creation
	 * @param prov An optional provisiong object for the application.
	 * @return The application object
	 * @throws Exception
	 */
	public Application createApplication(Account acct, String appName, Date creationDate, Provisioning prov) throws Exception {
		if (acct == null || appName == null) return null;
		// Generate a unique application ID
		String appID = TokenGenerator.generateToken(new Date().toString());
		Application app = new Application(0l, appID, appName, acct, creationDate, prov);
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			acct.addApplication(app);
			session.save(app);
			if (prov == null) {
				prov = new Provisioning(0l, app, null, null);
				session.save(prov);
			}
			app.setProvisioning(prov);
			session.saveOrUpdate(app);
			session.saveOrUpdate(acct);
			if (!existsTransaction) session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
			throw e;
		}
		return app;
	}

	public void addApplication(Account acct, Application app)  throws Exception {
		if (acct == null || app == null)
			return;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			//			session.refresh(acct);
			acct.addApplication(app);
			session.saveOrUpdate(acct);
			if (!existsTransaction) session.getTransaction().commit();
		} catch (Exception e) {
			log.error("AccountManager - addApplication Failed: " + e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
			throw e;
		}
	}



	/**
	 * A utility method to send a broadcast announcement to a group of users.
	 *
	 * @param people A set of account objects to receive the email
	 * @param subject The subject for the email
	 * @param message The message body
	 * @throws Exception
	 */
	public void sendEmailAnnouncement(HashSet<Account>people, String subject, String message) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("select * from person group by email");
		SQLQuery qs = session.createSQLQuery(query.toString()).addEntity("person", Account.class);
		qs.setMaxResults(1000);
		ActionManager am = new ActionManager(session);
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<Account> result = qs.list();
			for (Account person : result) {
				if (people == null || people.contains(person)) {
					am.emailAcct(person, subject, message);
				}
			}
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.error("AccountManager - peopleSearch failed: " + e.toString());
			if (session.getTransaction().isActive())
				session.getTransaction().rollback();
			throw e;
		}
	}

	public Application getApplication(String appID) {
		Application app = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Application.class);
			crit.add(Restrictions.eq("appID", appID));
			@SuppressWarnings("unchecked")
			List<Application> results = crit.list();
			if (results.size() == 0) return null;
			if (results.size() > 1) 
				log.info("**** more than two applications with same id: " + appID);
			app = results.get(0);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: getApplication (appID) failed: " + e.toString());
		}
		return app;
	}


	public void removeAppFromAccount(Application app, Account acct) {
		if (app == null) return;
		session.beginTransaction();
		try {
			if (!app.getAccount().equals(acct)) return;
			acct.removeApplication(app);
			session.saveOrUpdate(acct);
			session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: removeAppFromAccount failed: " + e.toString());
		}
	}



	/**
	 * Gets the application by itsname for the account.
	 *
	 * @param acct Account object
	 * @param name Application name
	 * @return Application object
	 */
	public Application getApplication(Account acct, String name)  {
		Application app = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Application.class);
			crit.add(Restrictions.eq("account", acct));
			crit.add(Restrictions.eq("name", name));
			@SuppressWarnings("unchecked")
			List<Application> results = crit.list();
			if (results.size() > 1)
				log.info("**** more than two applications with same name: " + name + " for account: " + acct.getId());
			else if (results.size() == 1)
				app = results.get(0);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("AccountManager: getApplication (acct, name) failed: " + e.toString());
		}
		return app;
	}

	/**
	 * Returns true if the application is authorized for a particular phone.
	 *
	 * @param phone Phone object.
	 * @param app The application object.
	 * @return true if authorized
	 */
	public boolean isAuthorized(Phone phone, Application app) {
		if (phone == null) return true;
		return !phone.isDisabled(app);
	}

	public static String generateSecurityToken(Account acct) {
		Random random = new Random();
		return TokenGenerator.generateToken(acct.getName()+random.nextLong());
	}

	public static String generateEmailToken(Account acct) {
		Random random = new Random();
		return TokenGenerator.generateToken(acct.getName()+acct.getEmail()+random.nextLong());
	}

}