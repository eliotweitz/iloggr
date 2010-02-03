/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ILoggrModule.class)
public interface ILoggrGinjector extends Ginjector {

	/**
	 * Returns the main ILoggr UI widget.
	 */
	ILoggrUI getUI();

}
