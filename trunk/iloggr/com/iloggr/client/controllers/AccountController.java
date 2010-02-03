/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.controllers;


import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iloggr.client.ActionListener;
import com.iloggr.client.ILoggrAsyncHandler;
import com.iloggr.client.ILoggrVoidAsyncHandler;
import com.iloggr.client.controllers.ILoggrStateController.MainMenuState;
import com.iloggr.client.controllers.ILoggrStateController.SubMenuState;
import com.iloggr.client.controllers.ILoggrStateController.UserState;
import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.services.AccountServiceAsync;
import com.iloggr.gwt.util.client.AsyncCallbackFactory;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.gwt.util.lang.StringUtility;

/**
 * @author eliot
 *
 */
@Singleton
public class AccountController {


	public interface Hasher {
		String hash(String clearText);
	}

	private final Hasher hasher;
	private final AccountServiceAsync accountService;
	private final AsyncCallbackFactory callbackFactory;
	private final StatusController statusController;
	private final AccountControllerMessages messages;
	private final ILoggrStateController stateController;
	


	@Inject
	public AccountController(Hasher hasher, AccountServiceAsync accountService, AsyncCallbackFactory callbackFactory,
			StatusController statusController, AccountControllerMessages messages, ILoggrStateController stateController) {
		this.hasher = hasher;
		this.accountService = accountService;
		this.callbackFactory = callbackFactory;
		this.statusController = statusController;
		this.messages = messages;
		this.stateController = stateController;
	}

	private Account currentAccount;

	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	public Application getAppNamed(String name) {
		return currentAccount.getApplicationNamed(name);
	}
	
	

	public boolean isLoggedIn() {
		return (currentAccount != null);
	}

	public void logout() {
		currentAccount = null;
		stateController.setUserState(UserState.LoggedOut);
		stateController.setSubMenuState(SubMenuState.NoState);
		removeLoginCookie();
//		Cookies.setCookie("login", "LOGGEDOUT");
	}

	public void activateAccount(Long id, String emailToken) {
		// TODO
	}

	public void createAccount(final String code, final String name, final String email, String password, String phone, final ActionListener<Void> actionListener) {
			statusController.waitMessage();
			accountService.createAccount(code, name, email, hash(password), phone,
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler() {
					@Override
					protected void execute() {
						statusController.statusMessage(messages.accountCreated(email));
						stateController.setMainMenuState(MainMenuState.AccountManagement);
						actionListener.onSuccess(null);
					}

					public void onError(ILException e) {
						failedAsyncCall(e);
//						statusController.errorMessage(exception.getMessage());
						actionListener.onFailure();
					}
				}));
	}

	public void authenticateAccount(String email, String password, final ActionListener<Void> actionListener) {
		statusController.waitMessage();
		accountService.authenticateAccount(email, hash(password),
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<Account>(){
					public void execute(Account account) {
						statusController.doneMessage();
						currentAccount = account;
						stateController.setUserState(UserState.Authenticated);
						setLoginCookie(currentAccount.getSecurityToken());
						actionListener.onSuccess(null);
					}

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}}));
	}
	
	private String getLoginCookie() {
		return Cookies.getCookie("login");
	}
	
	private void setLoginCookie(String cookie) {
		Cookies.setCookie("login", cookie);
//	Cookies.setCookie("login", cookie, new Date(System.currentTimeMillis()+10*24*60*60*1000), Location.getHostName(), "/", false);
	}
	
	private void removeLoginCookie() {
		Cookies.removeCookie("login");
//		Cookies.setCookie("login", "XXXXXX", new Date(System.currentTimeMillis()+10*24*60*60*1000), Location.getHostName(), "/", false);
		
	}

	// Uses current account security token
	public void authenticateAccount(final ActionListener<Void> actionListener) {
		String securityToken = getLoginCookie();
		authenticateAccount(securityToken, actionListener);
	}
	
	public void authenticateAccount(String securityToken, final ActionListener<Void> actionListener) {
		if (securityToken != null) {
			statusController.waitMessage();
			accountService.authenticateAccount(securityToken,
					callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<Account>(){
						public void execute(Account account) {
//							statusController.doneMessage();
							currentAccount = account;
							stateController.setUserState(UserState.Authenticated);
							setLoginCookie(currentAccount.getSecurityToken());
							if (actionListener != null) actionListener.onSuccess(null);
						}

						public void onError(ILException e) {
							failedAsyncCall(e) ;
							if (actionListener != null) actionListener.onFailure();
						}}));
		} else {
			actionListener.onFailure();
		}
	}

	
	public void getApplications(final ActionListener<Void> actionListener) {
		statusController.waitMessage();
		checkAuthenticated();
		accountService.getApplications(currentAccount.getSecurityToken(),
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<Set<Application>>(){
					public void execute(Set<Application> item) {
						statusController.doneMessage();
						actionListener.onSuccess(null);
						// populate the account object
						currentAccount.setApplications(item);
					}
	
					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}
				}));
	}
	
	private boolean checkAuthenticated() {
		if (currentAccount != null && currentAccount.getSecurityToken() != null) return true;
		statusController.errorMessage("Not logged in!, please log in or create a new account.");
		return false;
	}
	
	public void failedAsyncCall(ILException e) {
		String errorMsg = e.getMessage();
		switch (e.getErrorCode()) {
			case ILException.BAD_TOKEN:
				removeLoginCookie();
				break;
			case ILException.ACCOUNT_NOT_ACTIVATED:
				errorMsg = messages.accountNotActivated();
				break;
			default:
				break;
		}
		statusController.errorMessage(errorMsg);
	}


	public void emailSecurityToken(String acctName, String password) {
		// TODO
	}

	public void emailApplicationID(String applicationName) {
		// TODO
	}

	public void createNewApplication(final String appName, Date releaseDate, final ActionListener<Void> actionListener) {
		statusController.waitMessage();
		checkAuthenticated();
		accountService.createNewApplication(currentAccount.getSecurityToken(), appName, releaseDate, 
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<Application>() {
					public void execute(Application app) {
						statusController.statusMessage(appName + " " + messages.applicationCreated());
						stateController.setSubMenuState(SubMenuState.NoState);
						currentAccount.addApplication(app);  // Add it for the menu
						actionListener.onSuccess(null);
					}

					public void onError(ILException e) {
						failedAsyncCall(e);
//						statusController.errorMessage(e.getMessage());
						actionListener.onFailure();
					}
				}));
	}
	
	public void deleteApplication(final Application app, final ActionListener<Void> actionListener) {
		statusController.waitMessage();
		checkAuthenticated();
		accountService.deleteApplication(currentAccount.getSecurityToken(), app.getAppID(), 
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler() {
					@Override
					public void execute() {
						statusController.statusMessage(messages.applicationDeleted());
						stateController.setSubMenuState(SubMenuState.NoState);
						currentAccount.removeApplication(app);
						actionListener.onSuccess(null);
					}

					public void onError(ILException e) {
						failedAsyncCall(e);
//						statusController.errorMessage(e.getMessage());
						actionListener.onFailure();
					}
				}));
	}

	public void updateApplication(String name, Date releaseDate, String announcement, String appID, 
			final ActionListener<Application> actionListener) {
		statusController.waitMessage();
		checkAuthenticated();
		accountService.updateApplication(currentAccount.getSecurityToken(), name, releaseDate, announcement, appID, 
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrAsyncHandler<Application>(){
					public void execute(Application app) {
						actionListener.onSuccess(app);
					}

					public void onError(ILException e) {
						failedAsyncCall(e);
						actionListener.onFailure();
					}}));
	}


	
	public void resetPassword(String token, /*String oldPassword, */String newPassword) {
		statusController.waitMessage();
		accountService.resetPassword(token, /*hash(oldPassword),*/ hash(newPassword),
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler() {
					@Override
					public void execute() {
						statusController.statusMessage(messages.passwordChanged());
					}

					public void onError(ILException e) {
						failedAsyncCall(e);
//						statusController.errorMessage(e.getMessage());
					}
				}));
	}

	public void resetPasswordEmail(final String email) {
		if (!StringUtility.isValidEmail(email)) {
			statusController.errorMessage(messages.invalidEmailAddress());
			return;
		}
		statusController.waitMessage();
		accountService.resetPasswordEmail(email,
				callbackFactory.createAsyncCallback(ILException.class, new ILoggrVoidAsyncHandler() {
					@Override
					public void execute() {
						statusController.statusMessage(messages.passwordResetSent(email));
					}

					public void onError(ILException e) {
						statusController.errorMessage(e.getMessage());
					}
				}));
	}



	private String hash(String password) {
		return hasher.hash(password);
	}

	


}




