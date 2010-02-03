/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.controllers;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * Provides localized messages for the AccountController.
 */
@DefaultLocale("en_US")
public interface AccountControllerMessages extends Messages {

	@DefaultMessage("An account has been created for user: \"{0}\". Look for an email sent to your account for activation instructions. Go to \"My Account\" to log in once activated.")
	String accountCreated(String email);

	@DefaultMessage("Your account has been activated, you can now create applications and use iLoggr.")
	String accountActivated();

	@DefaultMessage("Account not activated, please check your email for activation instructions or contact support@iloggr.com for assistance.")
	String accountNotActivated();

	@DefaultMessage("Password changed successfully, you are still logged in.")
	String passwordChanged();

	@DefaultMessage("Reset instructions sent to {0}")
	String passwordResetSent(String email);

	@DefaultMessage("Please enter a valid email address")
	String invalidEmailAddress();
	
	@DefaultMessage("Application created")
	String applicationCreated();

	@DefaultMessage("Application deleted")
	String applicationDeleted();


}
