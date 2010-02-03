/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(PingModule.class)
public interface PingGinjector extends Ginjector {

	/**
	 * Returns the main Ping UI widget.
	 */
	PingUI getUI();
}
