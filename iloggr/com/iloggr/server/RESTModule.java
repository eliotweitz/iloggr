/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.iloggr.server.RESTServiceDispatcher.DELETEService;
import com.iloggr.server.RESTServiceDispatcher.GETService;
import com.iloggr.server.RESTServiceDispatcher.POSTService;
import com.iloggr.server.RESTServiceDispatcher.PUTService;
import com.iloggr.server.services.AccountRESTService;

public class RESTModule extends AbstractModule {

	@Provides @Singleton ImmutableSet<? extends GETService> provideGetServices(AccountRESTService accountService) {
		return ImmutableSet.of(accountService);
	}
	
	@Provides @Singleton ImmutableSet<? extends POSTService> providePostServices(AccountRESTService accountService) {
		return ImmutableSet.of(accountService);
	}
	
	@Provides @Singleton ImmutableSet<? extends PUTService> providePutServices() {
		return ImmutableSet.of();
	}
	
	@Provides @Singleton ImmutableSet<? extends DELETEService> provideDeleteServices() {
		return ImmutableSet.of();
	}
	
	@Override
	protected void configure() {
		bind(AccountRESTService.class).in(Singleton.class);
	}

}
