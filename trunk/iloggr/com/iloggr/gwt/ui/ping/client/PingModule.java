/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.iloggr.gwt.util.client.LoggingErrorManager;
import com.iloggr.gwt.util.client.ServiceConnector;
import com.iloggr.gwt.util.client.AsyncCallbackFactory.ErrorManager;
import com.iloggr.gwt.util.client.ServiceConnector.RelativeTo;

/**
 * The client-side Guice bindings for the Ping UI.
 */
public class PingModule extends AbstractGinModule {

	@Provides @Singleton ServiceConnector provideServiceConnector(PingConfig pingConfig) {
		return new ServiceConnector(RelativeTo.HOST_PAGE, pingConfig.getGwtServiceDispatcherRelativePath());
	}

	@Provides @Singleton PingAsync providePingAsync(ServiceConnector serviceConnector) {
		return serviceConnector.connect(GWT.create(Ping.class));
	}

	@Override
	protected void configure() {
		bind(PingUI.class).in(Singleton.class);

		bind(ErrorManager.class).to(LoggingErrorManager.class).in(Singleton.class);
		bindConstant().annotatedWith(Names.named(LoggingErrorManager.SHOW_ALERTS)).to(true);

		bindConstant().annotatedWith(Names.named(PingUI.STATUS_FADE_DELAY_MS)).to(5 * 1000);
	}

}
