/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en_US")
public interface NewUserPanelMessages extends Messages {

	@DefaultMessage("Enter your invitation code:")
	String codeLabel();
	
	@DefaultMessage("Your email will be used as your login name.")
	String headerMessage();

	@DefaultMessage("Your name/business: ")
	String nameLabel();

	@DefaultMessage("Email: ")
	String usernameLabel();

	@DefaultMessage("Email Again: ")
	String username2Label();

	@DefaultMessage("Password: ")
	String passwordLabel();

	@DefaultMessage("Password Again: ")
	String password2Label();

	@DefaultMessage("Mobile number (optional): ")
	String mobilePhoneNumberLabel();

	@DefaultMessage("Please enter a name.")
	String promptRequiredName();

	@DefaultMessage("Email addresses do not match!")
	String promptEmailMismatch();

	@DefaultMessage("Passwords do not match!")
	String promptPasswordMismatch();
	
	@DefaultMessage("Not a valid email address!")
	String promptEmailInvalid();
	
	@DefaultMessage("Please enter a valid invitation code.")
	String promptCodeInvalid();

}
