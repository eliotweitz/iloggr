/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.managers;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.Provisioning;
import com.iloggr.client.model.ProvisioningParameter;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.util.HibernateUtil;

public class ProvisioningManager {
	// Set up a simple configuration that logs on the console.
	static final Logger log = Logger.getLogger(ProvisioningManager.class);

	private Session session;


	public ProvisioningManager() {
		if (this.session != null) this.closeSession();
		this.session = HibernateUtil.getSessionFactory().openSession();
	}

	public ProvisioningManager(Session session) {
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
		this.session = session;
	}

	public Provisioning getProvisioning(long id) throws Exception {
		Provisioning prov = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			prov = (Provisioning) session.get(Provisioning.class, id);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("ProvisioningManager: getProvisioning (id) failed: " + e.toString());
			throw new ILException(ILException.UNKNOWN_APPLICATION_PROVISIONING);
		} 
		return prov;
	}
	
	
	public ProvisioningParameter getProvisioningParameter(long id) throws Exception {
		ProvisioningParameter param = null;
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction) session.beginTransaction();
		try {
			param = (ProvisioningParameter) session.get(ProvisioningParameter.class, id);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("ProvisioningManager: getProvisioningParameter (id) failed: " + e.toString());
			throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		} 
		return param;
	}


	public void saveProvisioning(Provisioning prov) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.save(prov);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("ProvisioningManager saveProvisioning failed: " + e.toString());
			session.getTransaction().rollback();
			throw e;
		}

	}
	
	
	public void saveOrUpdateProvisioning(Provisioning prov) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.saveOrUpdate(prov);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("ProvisioningManager saveOrUpdateProvisioning: "
							+ e.toString());
			throw e;
		}

	}
	
	public void saveOrUpdateProvisioningParameter(ProvisioningParameter pp) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.saveOrUpdate(pp);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("ProvisioningManager saveOrUpdateProvisioningParameter: "
							+ e.toString());
			throw e;
		}

	}
	
	public void deleteProvisioningParameter(Provisioning prov, ProvisioningParameter pp) throws Exception {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			prov.removeParameter(pp);
			session.saveOrUpdate(prov);
//			session.delete(pp);
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
			log.info("ProvisioningManager deleteProvisioningParameter: "
							+ e.toString());
			throw e;
		}

	}

	
	public void addProvisioningParameter(Account acct, Application app, String paramName, String value, String type, boolean active) throws Exception  {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			Provisioning prov = app.getProvisioning();
			ProvisioningParameter param = prov.getParameterNamed(paramName);
			//  Allow overwrite of existing parameters
			if (param != null)  {//throw new ILException(ILException.PROVISIONING_PARAMETER_ALREADY_EXISTS);
				// just set the value
				param.setValue(value);
				session.update(param);
			} else {
				param = new ProvisioningParameter(0l, paramName, value, type, active, prov);
				session.save(param);
				prov.addParameter(param);
				session.saveOrUpdate(prov);
			}
			if (!existsTransaction)
				session.getTransaction().commit();
		} catch (Exception e) {
				log.info("ProvisioningManager addProvisioningParameter: " + e.toString());
				session.getTransaction().rollback();
				throw e;
		}
	}
	
	public void addApplicationCounter(Application app, String counterName) throws Exception  {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			Counter counter = app.getCounterNamed(counterName);
			//  warn if already exists
			if (counter != null) throw new ILException(ILException.APPLICATION_COUNTER_ALREADY_EXISTS);
			counter = new Counter(0l, counterName, app, new Date(), 0l);
			session.save(counter);
			app.addCounter(counter);
			session.saveOrUpdate(app);
			if (!existsTransaction) {
				session.getTransaction().commit();
			}
		} catch (Exception e) {
				log.info("ProvisioningManager addApplicationCounter: " + e.toString());
				session.getTransaction().rollback();
				throw e;
		}
	}
	
		
	public void saveOrUpdateApplicationCounter(Counter c) throws Exception  {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			session.saveOrUpdate(c);
			if (!existsTransaction) {
				session.getTransaction().commit();
			}
		} catch (Exception e) {
				log.info("ProvisioningManager saveOrUpdateApplicationCounter: " + e.toString());
				session.getTransaction().rollback();
				throw e;
		}
	}
	

		
	public void deleteApplicationCounter(Application app, Counter c) throws Exception  {
		boolean existsTransaction = session.getTransaction().isActive();
		if (!existsTransaction)
			session.beginTransaction();
		try {
			app.removeCounter(c);
			session.saveOrUpdate(app);
			if (!existsTransaction) {
				session.getTransaction().commit();
			}
		} catch (Exception e) {
				log.info("ProvisioningManager addApplicationCounter: " + e.toString());
				session.getTransaction().rollback();
				throw e;
		}
	}
		

	
}
