/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

/**
 * Binds up all modules needed to support all servlets in a single VM.
 */
public class BindingsModule extends AbstractModule {

	@Override
	public void configure() {
		install(new ServletModule()); // Support @RequestScoped and @SessionScoped
		install(new ServiceModule()); // Install our core services
		install(new RESTModule()); // Install our REST service layer
	}

}
