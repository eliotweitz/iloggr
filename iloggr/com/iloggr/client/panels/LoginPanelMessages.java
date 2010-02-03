/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

@DefaultLocale("en_US")
public interface LoginPanelMessages extends Messages {

	@DefaultMessage("Email:")
	String usernameLabel();

	@DefaultMessage("Password:")
	String passwordLabel();

}
