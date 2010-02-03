/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.iloggr.gwt.ui.ping.client.Ping.Status;

/**
 * Provides localized messages for the Ping client UI.
 */
@DefaultLocale("en_US")
public interface PingMessages extends Messages {

	@DefaultMessage("Check Health")
	String pingButtonLabel();

	@DefaultMessage("OK")
	String okStatusMessage();

	@DefaultMessage("Uhealthy ({0})")
	String notOkStatusMessage(Status status);

	@DefaultMessage("Unknown ({0})")
	String unknownStatusMessage(Status status);
}
