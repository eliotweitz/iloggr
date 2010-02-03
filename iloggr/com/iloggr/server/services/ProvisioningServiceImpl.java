/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.services;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.iloggr.client.VoidActionListener;
import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.Phone;
import com.iloggr.client.model.Provisioning;
import com.iloggr.client.model.ProvisioningParameter;
import com.iloggr.client.services.ProvisioningService;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.server.managers.AccountManager;
import com.iloggr.server.managers.EventManager;
import com.iloggr.server.managers.ProvisioningManager;
import com.iloggr.util.StringUtils;

/**
 * This class provides functionality to both account users via JSON web services to provisioning registered applications
 * and to iPhone clients to retrieve provisioning data.
 * 
 * @author eliot
 * @version 1.0
 */
public class ProvisioningServiceImpl implements ProvisioningService {
	
	// WRAP ALL RETURNED SIMPLE TYPES IN ProvisioningParameter Class   EW - 08/02/09
	
	/**
	 * Gets the provisioning object that contains all of the provisioning parameters for the account 
	 * application identified by the security token and application ID.
	 * 
	 * @param token Security Token
	 * @param appID Application ID
	 * @return Provisioning Object for this application
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProvisioningParameter> getProvisioningParameters(String token, String appID) throws Exception {
		AccountManager am = new AccountManager();
		// one long transaction
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		// Need to convert to a ArrayList so it can be serialized
		ArrayList<ProvisioningParameter>result = new ArrayList<ProvisioningParameter>();
		for (ProvisioningParameter param : app.getProvisioning().getParameters()) result.add(param);
		am.getSession().getTransaction().commit();
		Collections.sort(result);
		am.closeSession();
		return result;
	}

	/**
	 * Returns the provisioning parameter object for a given account application
	 * 
	 * @param token Security Token
	 * @param appID Application ID
	 * @param name Provisioning Parameter Name
	 * @return ProvisioningParameter Object
	 * @throws Exception
	 */
	public ProvisioningParameter getProvisioningParameter(String token, String appID, String name) throws Exception {
		List<ProvisioningParameter> parameters = getProvisioningParameters(token, appID);
		for (ProvisioningParameter param : parameters) if (param.getName().equalsIgnoreCase(name)) return param;
		throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
	}
	
	/**
	 * Adds a new provisioning parameter to the application identified by appID.  Parameter name must be unique for the application.
	 * Parameter values are always stored as Strings.
	 * 
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @param value The string value to set
	 * @throws Exception
	 */
	public void addProvisioningParameter(String token, String appID, String paramName, String value, String type, boolean active) throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		Account acct = app.getAccount();
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		pm.addProvisioningParameter(acct,  app, paramName, value, type, active);
		am.getSession().getTransaction().commit();
		am.closeSession();
	}
	
	public void deleteProvisioningParameter(String token, ProvisioningParameter param) throws Exception {
		if (param == null || param.getId() <= 0) throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		am.checkToken(token);
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		ProvisioningParameter sParam = pm.getProvisioningParameter(param.getId());
		if (sParam == null) throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		Provisioning parent = sParam.getProvisioning();
		if (parent == null ) throw new ILException(ILException.UNKNOWN_APPLICATION_PROVISIONING);
		parent.removeParameter(sParam);
		pm.saveOrUpdateProvisioning(parent);
		pm.getSession().getTransaction().commit();
		pm.closeSession();
	}
	
	/* (non-Javadoc)
	 * @see com.iloggr.client.ProvisioningService#deleteProvisioningParameter(java.lang.String, java.lang.long, java.lang.String)
	 */
	public void deleteProvisioningParameter(String token, String appID, String paramName) throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		Provisioning prov = app.getProvisioning();
		ProvisioningParameter param = prov.getParameterNamed(paramName);
		if (param == null)  throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		pm.deleteProvisioningParameter(prov, param);
		pm.saveOrUpdateProvisioning(prov);
		am.getSession().getTransaction().commit();
		am.closeSession();		
	}

	
	/**
	 *  Updates the value of the specified application provisioning parameter to the new String value.
	 * 
	 * @param token Security Token
	 * @param appID Application ID
	 * @param paramName Provisioning Parameter Name
	 * @param value The string value to set
	 * @throws Exception
	 */
	public void updateProvisioningParameter(String token, String appID, String paramName, String value, String type, boolean active) throws Exception  {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		Provisioning prov = app.getProvisioning();
		ProvisioningParameter param = prov.getParameterNamed(paramName);
		if (param == null)  throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		param.setValue(value);
		param.setType(type);
		param.setActive(active);
		pm.saveOrUpdateProvisioningParameter(param);
		am.getSession().getTransaction().commit();
		am.closeSession();
	}
	
	/* (non-Javadoc)
	 * @see com.iloggr.client.ProvisioningService#updateProvisioningParameter(java.lang.String, com.iloggr.client.ProvisioningParameter, java.lang.String)
	 */
	public void updateProvisioningParameter(String token, ProvisioningParameter param, String value, String type, boolean active) throws Exception {
		if (param == null || param.getId() <= 0) throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Account acct = am.checkToken(token);
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		ProvisioningParameter sParam = pm.getProvisioningParameter(param.getId());
		if (sParam == null) throw new ILException(ILException.UNKNOWN_PROVISIONING_PARAMETER);
		Provisioning parent = sParam.getProvisioning();
		if (parent == null) throw new ILException(ILException.UNKNOWN_APPLICATION_PROVISIONING);
		//  See if the application belongs to the account
		Application app = parent.getApplication();
		if (!app.ownedBy(acct)) {
			am.closeSession();
			throw new ILException(ILException.APPLICATION_NOT_OWNED_BY_ACCOUNT);
		}
		sParam.setValue(value);
		sParam.setType(type);
		sParam.setActive(active);
		pm.saveOrUpdateProvisioningParameter(sParam);
		pm.getSession().getTransaction().commit();
		pm.closeSession();
	}
	
	/**
	 * Adds a new counter to the application identified by appID.  Counter name must be unique for the application.
	 * 
	 * @param token Security Token
	 * @param appID String ID
	 * @param counterName Counter  Name
	 * @throws Exception
	 */
	public void addApplicationCounter(String token, String appID, String counterName) throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		pm.addApplicationCounter(app, counterName);
		am.getSession().getTransaction().commit();
		am.closeSession();
	}
	
	/**
	 * Gets a counter for the application identified by appID.  Counter name must be unique for the application.
	 * 
	 * @param token Security Token
	 * @param appID String ID
	 * @param counterName Counter  Name
	 * @throws Exception
	 *//*
	public Counter getApplicationCounter(String token, String appID, String counterName) throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		Counter result = app.getCounterNamed(counterName);
		for (Counter c : app.getCounters()) if (c.getName().equalsIgnoreCase(counterName)) result = c;
		am.getSession().getTransaction().commit();
		am.closeSession();
		return result;
	}*/


	/**
	 * Resets a counter to the application identified by appID to zero.  Counter name must be unique for the application.
	 * 
	 * @param token Security Token
	 * @param appID String ID
	 * @param counterName Counter  Name
	 * @throws Exception
	 */
	public void resetApplicationCounter(String token, String appID, String counterName) throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		ProvisioningManager pm = new ProvisioningManager(am.getSession());
		Counter counter = null;
		for (Counter c : app.getCounters()) if (c.getName().equalsIgnoreCase(counterName)) counter = c;
		if (counter == null) throw new ILException(ILException.UNKNOWN_APPLICATION_COUNTER_NAME);
		counter.setCount(0l);
		pm.saveOrUpdateApplicationCounter(counter);
		am.getSession().getTransaction().commit();
		am.closeSession();
	}
	
	/**
	 * Resets a counter to the application identified by appID to zero.  Counter name must be unique for the application.
	 * 
	 * @param token Security Token
	 * @param appID String ID
	 * @param counterName Counter  Name
	 * @throws Exception
	 */
	public void deleteApplicationCounter(String token, String appID, String counterName) throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		Counter counter = null;
		for (Counter c : app.getCounters()) if (c.getName().equalsIgnoreCase(counterName)) counter = c;
		if (counter == null) throw new ILException(ILException.UNKNOWN_APPLICATION_COUNTER_NAME);
		app.removeCounter(counter);
		am.saveOrUpdateApplication(app);
		am.getSession().getTransaction().commit();
		am.closeSession();
	}


	
	/**
	 * Checks to see if the particular phone identified by the unique clientID has been blocked for this application.
	 * 
	 * @param token Security token
	 * @param clientID Unique client ID for phone
	 * @param appID Application ID
	 * @return boolean true if this clientID is approved for the application (wrapped in provisioning parameter object)
	 * @throws Exception
	 */
	public ProvisioningParameter isAuthorized(String token, String clientID, String appID) throws Exception {
		if (StringUtils.isEmpty(clientID)) return new ProvisioningParameter("isAuthorized", true);
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		Phone phone = em.getPhoneByClientID(clientID);
		if (phone == null) return new ProvisioningParameter("isAuthorized", true);
		am.closeSession();
		return new ProvisioningParameter("isAuthorized", am.isAuthorized(phone, app));
	}
	
	/**
	 * Registers the application identified by appID as blocked for the phone identified by the unique clientID.
	 * 
	 * @param token Security token
	 * @param appID Application ID
	 * @param clientID Unique client ID for phone
	 * @throws Exception
	 */
	public void disableApplicationOnPhone(String token, String appID, String clientID) throws Exception {
		if (StringUtils.isEmpty(clientID)) return;
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		EventManager em = new EventManager(am.getSession());
		Phone phone = em.getPhoneByClientID(clientID);
		if (phone == null) return;
		phone.disableApplication(app);
		em.saveOrUpdatePhone(phone);
		am.closeSession();
	}
	
	public ProvisioningParameter getAnnouncement(String token, String appID) throws Exception {
		AccountManager am = new AccountManager(); 
		Application app = am.checkAppOwnedByAcct(token, appID);
		return new ProvisioningParameter("Announcement", app.getAnnouncement());
	}
	
	public List<Counter> getApplicationCounters(String token, String appID)
			throws Exception {
		AccountManager am = new AccountManager();
		am.getSession().beginTransaction();
		Application app = am.checkAppOwnedByAcct(token, appID);
		// Need to convert to a ArrayList so it can be serialized
		ArrayList<Counter>result = new ArrayList<Counter>();
		for (Counter c : app.getCounters()) result.add(c);
		am.getSession().getTransaction().commit();
		Collections.sort(result);
		am.closeSession();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.iloggr.client.services.ProvisioningService#associateIDWithPhone(java.lang.String, java.lang.String, java.lang.String)
	 */
/*	public void associateIDWithPhone(String token, String userID, String appID, String phoneClientID) throws Exception {
		AccountManager am = new AccountManager();
		Accountam.checkToken(token);
		
	}*/

	


}
