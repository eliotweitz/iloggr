/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Singleton;

/**
 * The dynamic configuration for the Iloggr application.  This class encapsulates a javascript object that must be defined
 * in the host page.
 */
@Singleton
public class ILoggrConfig {
	private final Dictionary config;

	public ILoggrConfig() {
		config = Dictionary.getDictionary("_ILoggrConfig");
	}

	/**
	 * @return the relative path of the {@link com.iloggr.server.GWTServiceDispatcher} from the host page
	 */
	public String getGwtServiceDispatcherRelativePath() {
		return config.get("gwtDispatcherPath");
	}
}