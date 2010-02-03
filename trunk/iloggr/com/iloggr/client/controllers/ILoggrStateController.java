package com.iloggr.client.controllers;
/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */


import com.google.inject.Singleton;

@Singleton
public class ILoggrStateController {
	
	// State of the UI
	private UserState userState;
	private MainMenuState mainMenuState;
	private SubMenuState subMenuState[];
	private String data;
	
	
	public ILoggrStateController() {
		userState = UserState.FirstLaunch;
		mainMenuState = MainMenuState.Information;
		subMenuState = new SubMenuState[MainMenuState.values().length];
		data = "";
		for (int i=0;i<MainMenuState.values().length;i++) subMenuState[i] = SubMenuState.NoState;
	}


	/**
	 * @author eliot
	 * The current state of the user
	 */
	public enum UserState {
		FirstLaunch,
		Authenticated,
		LoggedOut
	}

	public enum MainMenuState  {
		Information,
		Support,
		AccountManagement,
		SignUp;
	}

	public enum SubMenuState {
		NoState,
		NewApplication,
		EditApplication,
		ApplicationManagement,
		Provisioning,
		DeveloperDocumentation,
		ContactSupport,
		FAQ,
		Reports,
		ChangePassword,
		ForgotPassword,
		LogOut,
		SignUp,
		DownloadLibrary,
		APIDocumentation,
		WhatIs,
		ExamplesOfUse,
		Pricing,
		AccountSummary,
		AboutUs,
		WebServices;
		}
	
	
	
	public static MainMenuState getMainMenuStateFromHistoryToken(String token) {
		// in the form mainMenuState&subMenuState&operation&data
		String tokens[] = parseToken(token);
		if (tokens == null || tokens.length < 1) return null;
		return MainMenuState.valueOf(tokens[0]);
	}
	
	public static SubMenuState getSubMenuStateFromHistoryToken(String token) {
		// in the form m=mainMenuState&s=subMenuState&o=operation&d=data
		String tokens[] = parseToken(token);
		if (tokens == null || tokens.length < 2) return null;
		return SubMenuState.valueOf(tokens[1]);
	}
		
	public static String getDataFromHistoryToken(String token) {
		String tokens[] = parseToken(token);
		if (tokens == null || tokens.length < 3) return null;
		return tokens[2];
	}
	
	public static String[] parseToken(String token) {
		String tokens[] = token.split("&");
		if (tokens == null) return null;
		return tokens;
		
	}
	
	public String createHistoryToken() {
		StringBuffer token = new StringBuffer();
		token.append(mainMenuState.toString());
		token.append("&");
		token.append(getSubMenuState().toString());
		token.append("&");
		token.append(data);
		return token.toString();
	}
	
	public String createHistoryToken(SubMenuState subState, String d) {
		return createHistoryToken(mainMenuState, subState, d);
	}
	
	public static String createHistoryToken(MainMenuState mainState, SubMenuState subState, String d) {
		StringBuffer token = new StringBuffer();
		token.append(mainState.toString());
		token.append("&");
		token.append(subState.toString());
		token.append("&");
		token.append(d);
		return token.toString();
	}

	public void setUserState(UserState state) {
		this.userState = state;
	}

	public UserState getUserState() {
		return userState;
	}

	public MainMenuState getMainMenuState() {
		return mainMenuState;
	}


	/**
	 * Sets the main menu state
	 * @param mainMenuState
	 */
	public void setMainMenuState(MainMenuState state) {
		if (mainMenuState != state) {
			this.mainMenuState = state;
		}
	}

	public SubMenuState getSubMenuState() {
		return subMenuState[getMainMenuIndex(mainMenuState)];
	}

	// Sets the sub menu state for the current main menu item
	public void setSubMenuState(SubMenuState state) {
		subMenuState[getMainMenuIndex(mainMenuState)] = state;
	}
	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public static int getMainMenuIndex(MainMenuState state) {
		for (int i=0; i<MainMenuState.values().length; i++)
			if (state.equals(MainMenuState.values()[i])) return i;
		return 0;  // TODO eliot: what to do here?  Really an assert
	}
	
	public void recoverStateFromHistoryToken(String token) {
		// if token is empty, don't bother
		if ("".equals(token.trim())) return;
		MainMenuState mState = getMainMenuStateFromHistoryToken(token);
		if (mState != null) setMainMenuState(mState);
		SubMenuState sState = getSubMenuStateFromHistoryToken(token);
		if (sState != null) setSubMenuState(sState);
		String data = getDataFromHistoryToken(token);
		if (data != null) setData(data);
	}
	



}
