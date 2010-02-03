/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Singleton;

/**
 * The dynamic configuration for the Ping application.  This class encapsulates a javascript object that must be defined
 * in the host page.
 */
@Singleton
public class PingConfig {
	private final Dictionary config;

	public PingConfig() {
		config = Dictionary.getDictionary("_PingConfig");
	}

	/**
	 * @return the relative path of the {@link com.iloggr.server.GWTServiceDispatcher} from the host page
	 */
	public String getGwtServiceDispatcherRelativePath() {
		return config.get("gwtDispatcherPath");
	}
}