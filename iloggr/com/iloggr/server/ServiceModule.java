/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import java.util.Random;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.iloggr.client.services.AccountService;
import com.iloggr.client.services.ProvisioningService;
import com.iloggr.client.services.RecordService;
import com.iloggr.client.services.ReportingService;
import com.iloggr.gwt.ui.ping.client.Ping;
import com.iloggr.server.services.AccountServiceImpl;
import com.iloggr.server.services.ProvisioningServiceImpl;
import com.iloggr.server.services.RecordServiceImpl;
import com.iloggr.server.services.ReportingServiceImpl;

public class ServiceModule extends AbstractModule {

	/**
	 * Provides GWT service bindings
	 */
	@Provides @Singleton ServiceFactory<RemoteService> provideRemoteServiceFactory(final Injector injector) {
		return new ServiceFactory<RemoteService>(){
			public RemoteService getService(Class<? extends RemoteService> serviceClass) {
				return injector.getInstance(serviceClass);
			}
		};
	}

	/**
	 * Provides JSON service bindings
	 */
	@Provides @Singleton ServiceFactory<Object> provideServiceFactory(final Injector injector) {
		return new ServiceFactory<Object>(){
			public Object getService(Class<?> serviceClass) {
				return injector.getInstance(serviceClass);
			}
		};
	}

	@Override
	protected void configure() {
		// Core services
		bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
		bind(ProvisioningService.class).to(ProvisioningServiceImpl.class).in(Singleton.class);
		bind(RecordService.class).to(RecordServiceImpl.class).in(Singleton.class);
		bind(ReportingService.class).to(ReportingServiceImpl.class).in(Singleton.class);

		bind(Ping.class).to(PingImpl.class);
		bind(Random.class).toInstance(new Random(System.currentTimeMillis()));
	}

}
