/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.controllers;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;



/**
 * Provides localized messages for the ApplicationController.
 */
@DefaultLocale("en_US")
public interface ApplicationControllerMessages extends Messages {

	@DefaultMessage("Unknown application.")
	String unknownApplication();

	@DefaultMessage("Application not owned by this account.")
	String applicationNotOwnedByAccount();

}

