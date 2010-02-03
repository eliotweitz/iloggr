/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

	@DefaultLocale("en_US")
	public interface ApplicationManagementMessages extends Messages {

		@DefaultMessage("Application ID:")
		String appIDLabel();

		@DefaultMessage("Release Date:")
		String releaseDateLabel();

	}

