/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.controllers;


import java.util.Date;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iloggr.client.ActionListener;
import com.iloggr.client.ILoggrAsyncHandler;
import com.iloggr.client.ILoggrVoidAsyncHandler;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.ProvisioningParameter;
import com.iloggr.client.model.UserReportData;
import com.iloggr.client.services.ProvisioningServiceAsync;
import com.iloggr.client.services.ReportingServiceAsync;
import com.iloggr.gwt.util.client.AsyncCallbackFactory;
import com.iloggr.gwt.util.client.ILException;

/**
 * @author eliot
 *
 */
@Singleton
public class ApplicationController {

	private final ProvisioningServiceAsync provisioningService;
	private final AsyncCallbackFactory callbackFactory;
	private final StatusController statusController;
	private final ApplicationControllerMessages messages;
	private final AccountController accountController;
	private final ReportingServiceAsync reportingService;

	// Current application
	private Application currentApplication;

	public Application getCurrentApplication() {
		return currentApplication;
	}

	public void setCurrentApplication(Application currentApplication) {
		this.currentApplication = currentApplication;
	}
	


	@Inject
	public ApplicationController(ProvisioningServiceAsync provService, AsyncCallbackFactory callbackFactory,
			StatusController statusController, AccountController accountController, ApplicationControllerMessages messages,
			ReportingServiceAsync rs) {
		this.provisioningService = provService;
		this.callbackFactory = callbackFactory;
		this.statusController = statusController;
		this.accountController = accountController;
		this.reportingService = rs;
		this.messages = messages;
	}


	public void getProvisioningParameters(final ActionListener<List<ProvisioningParameter>> actionListener) {
		statusController.waitMessage();
		provisioningService.getProvisioningParameters(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(), 
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<List<ProvisioningParameter>>(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute(List<ProvisioningParameter> params) {
						statusController.doneMessage();
						actionListener.onSuccess(params);
					}}));
	}
	
	public void addProvisioningParameter(String paramName, String value, String type, boolean active, final VoidActionListener actionListener) {
		statusController.waitMessage();
		provisioningService.addProvisioningParameter(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(), 
				paramName, value,
				type, active, callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute() {
						statusController.doneMessage();
						actionListener.onSuccess();
					}}));
	}
	
	
	public void updateProvisioningParameter(ProvisioningParameter pp, String value, String type, boolean active, 
			final VoidActionListener actionListener) {
		statusController.waitMessage();
		provisioningService.updateProvisioningParameter(accountController.getCurrentAccount().getSecurityToken(), pp, value, type, active,
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute() {
						statusController.doneMessage();
						actionListener.onSuccess();
					}}));
	}
	

	

	public void deleteProvisioningParameter(ProvisioningParameter pp, final VoidActionListener actionListener) {
		statusController.waitMessage();
		provisioningService.deleteProvisioningParameter(accountController.getCurrentAccount().getSecurityToken(), pp,
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute() {
						statusController.doneMessage();
						actionListener.onSuccess();
					}}));
	}
	
	public void addApplicationCounter(String counterName, final VoidActionListener actionListener) {
		statusController.waitMessage();
		provisioningService.addApplicationCounter(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(), 
				counterName, callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute() {
						statusController.doneMessage();
						actionListener.onSuccess();
					}}));
	}
	
	public void resetApplicationCounter(String counterName, final VoidActionListener actionListener) {
		statusController.waitMessage();
		provisioningService.resetApplicationCounter(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(), 
				counterName, callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute() {
						statusController.doneMessage();
						actionListener.onSuccess();
					}}));
	}
	
	public void deleteApplicationCounter(String counterName, final VoidActionListener actionListener) {
		statusController.waitMessage();
		provisioningService.deleteApplicationCounter(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(), 
				counterName, callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute() {
						statusController.doneMessage();
						actionListener.onSuccess();
					}}));
	}


	
	public void getApplicationCounters(final ActionListener<List<Counter>> actionListener) {
		statusController.waitMessage();
		provisioningService.getApplicationCounters(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(), 
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<List<Counter>>(){

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute(List<Counter> params) {
						statusController.doneMessage();
						actionListener.onSuccess(params);
					}}));
	}


	
	public void fetchEventData(Date from, Date to, boolean desc, int limit, int offset, final ActionListener<List<Event>> actionListener) {
		statusController.waitMessage();
		reportingService.fetchEventData(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(),  
				from, to, desc, limit, offset,
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<List<Event>>(){
				public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute(List<Event>events) {
						statusController.doneMessage();
						actionListener.onSuccess(events);
					}
		}));
	}
	
	public void fetchEventDataAsCSV(Date from, Date to, boolean desc, int limit, int offset, final ActionListener<String> actionListener) {
		statusController.waitMessage();
		reportingService.fetchEventDataCSV(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(),  
				from, to, desc, limit, offset,
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<String>(){
				public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}

					public void execute(String result) {
						statusController.doneMessage();
						actionListener.onSuccess(result);
					}
					
					}));
	}
	
	public void fetchUniqueUserReportData(Date from, Date to, int unit, final ActionListener<List<UserReportData>>actionListener) {
		statusController.waitMessage();
		reportingService.fetchUniqueUserReport(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(),
				from, to, unit, callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<List<UserReportData>>(){

					public void onError(ILException exception) {
						failedAsyncCall(exception);
						actionListener.onFailure();
					}

					public void execute(List<UserReportData> result) {
						statusController.doneMessage();
						actionListener.onSuccess(result);
						
					}
				}));
	}
	
	public void fetchUserRegionReportData(Date from, Date to, final ActionListener<List<Event>>actionListener) {
		statusController.waitMessage();
		reportingService.fetchRegionReport(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(),
				from, to, callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<List<Event>>(){

					public void onError(ILException exception) {
						failedAsyncCall(exception);
						actionListener.onFailure();
					}

					public void execute(List<Event> result) {
						statusController.doneMessage();
						actionListener.onSuccess(result);
						
					}
				}));
	}
	
	public void fetchUniqueUserStats(Date from, Date to, int unit, final ActionListener<List<UserReportData>>actionListener) {
		statusController.waitMessage();
		reportingService.fetchUniqueUserStats(accountController.getCurrentAccount().getSecurityToken(), currentApplication.getAppID(),
				from, to, unit, callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<List<UserReportData>>(){

					public void onError(ILException exception) {
						failedAsyncCall(exception);
						actionListener.onFailure();
					}

					public void execute(List<UserReportData> result) {
						statusController.doneMessage();
						actionListener.onSuccess(result);
						
					}
				}));
	}
	
	

	public AccountController getAccountController() {
		return accountController;
	}

	public void failedAsyncCall(ILException e) {
		String errorMsg = e.getMessage();
		switch (e.getErrorCode()) {
		case ILException.APPLICATION_NOT_OWNED_BY_ACCOUNT:
			errorMsg = messages.applicationNotOwnedByAccount();
			break;
		case ILException.UNKNOWN_APPLICATION:
			errorMsg = messages.unknownApplication();
			break;
		default:
			break;
		}
		statusController.errorMessage(errorMsg);
	}
}




