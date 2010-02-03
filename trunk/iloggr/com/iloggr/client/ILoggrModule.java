/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.StatusController;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.Phone;
import com.iloggr.client.panels.StatusPanel;
import com.iloggr.client.services.AccountService;
import com.iloggr.client.services.AccountServiceAsync;
import com.iloggr.client.services.ProvisioningService;
import com.iloggr.client.services.ProvisioningServiceAsync;
import com.iloggr.client.services.RecordService;
import com.iloggr.client.services.RecordServiceAsync;
import com.iloggr.client.services.ReportingService;
import com.iloggr.client.services.ReportingServiceAsync;
import com.iloggr.client.visualization.EventVisualizer;
import com.iloggr.gwt.util.client.LoggingErrorManager;
import com.iloggr.gwt.util.client.ServiceConnector;
import com.iloggr.gwt.util.client.AsyncCallbackFactory.ErrorManager;
import com.iloggr.gwt.util.client.ServiceConnector.RelativeTo;

public class ILoggrModule extends AbstractGinModule {

	@Provides @Singleton ServiceConnector provideServiceConnector(ILoggrConfig iLoggrConfig) {
		return new ServiceConnector(RelativeTo.HOST_PAGE, iLoggrConfig.getGwtServiceDispatcherRelativePath());
	}

	@Provides @Singleton AccountServiceAsync provideAccountServiceAsync(ServiceConnector serviceConnector) {
		return serviceConnector.connect(GWT.create(AccountService.class));
	}

	@Provides @Singleton ProvisioningServiceAsync provideProvisioningServiceAsync(ServiceConnector serviceConnector) {
		return serviceConnector.connect(GWT.create(ProvisioningService.class));
	}

	@Provides @Singleton ReportingServiceAsync provideReportingServiceAsync(ServiceConnector serviceConnector) {
		return serviceConnector.connect(GWT.create(ReportingService.class));
	}


	@Provides @Singleton RecordServiceAsync provideRecordServiceAsync(ServiceConnector serviceConnector) {
		return serviceConnector.connect(GWT.create(RecordService.class));
	}

	@Provides @Singleton SHA1 provideSHA1() {
		return SHA1.getInstance();
	}

	@Provides @Singleton AccountController.Hasher providePasswordHasher(final SHA1 sha1) {
		return new AccountController.Hasher(){
			public String hash(String clearText) {
				return sha1.hash(clearText);
			}
		};
	}

	@Provides @Singleton StatusController provideFeedback(final StatusPanel statusPanel) {
		return new StatusController(){
			public void statusMessage(String message) {
				statusPanel.setMessage(message);
			}

			public void errorMessage(String message) {
				statusPanel.setErrorMessage(message);
			}
			
			public void waitMessage() {
				statusPanel.setMessage("Please wait...");
			}
			
			public void doneMessage() {
				statusPanel.setMessage("Operation complete.");
			}
		};
	}

	@Provides @Singleton EventVisualizer provideEventVisualizer() {
		List<Event> events = new LinkedList<Event>();
		events.addAll(createEvents(2009, 5, 1, 1L));
		events.addAll(createEvents(2009, 5, 2, 1L));
		events.addAll(createEvents(2009, 5, 3, 1L, 2L));
		events.addAll(createEvents(2009, 5, 5, 1L, 1L, 2L, 3L));
		events.addAll(createEvents(2009, 5, 6, 2L, 3L, 4L, 5L));
		events.addAll(createEvents(2009, 5, 7, 2L, 3L, 5L));
		events.addAll(createEvents(2009, 5, 8, 1L, 2L, 4L, 5L));
		events.addAll(createEvents(2009, 5, 9, 1L, 2L, 3L, 4L, 5L, 6L, 6L, 7L));
		events.addAll(createEvents(2009, 5, 10, 6L, 7L, 3L, 4L, 5L));
		events.addAll(createEvents(2009, 5, 11, 6L, 7L));
		events.addAll(createEvents(2009, 5, 13, 2L, 3L, 6L, 7L));
		events.addAll(createEvents(2009, 5, 14, 1L, 2L, 5L));
		events.addAll(createEvents(2009, 5, 15, 1L, 2L, 3L, 4L, 5L));
		events.addAll(createEvents(2009, 5, 16, 3L, 4L, 5L));
		return new EventVisualizer(events);
	}

	@SuppressWarnings("deprecation")
	private static List<Event> createEvents(int year, int month, int day, long... phoneIds) {
		Date when = new Date(year, month, day);
		List<Event> events = new ArrayList<Event>(phoneIds.length);
		for (Long phoneId : phoneIds) {
			Event event = new Event();
			event.setRecordTime(when);
			Phone phone = new Phone();
			phone.setId(phoneId);
			event.setPhone(phone);
			events.add(event);
		}
		return events;
	}

	@Override
	protected void configure() {
		bind(ILoggrUI.class).in(Singleton.class);
		bind(ErrorManager.class).to(LoggingErrorManager.class).in(Singleton.class);
		bindConstant().annotatedWith(Names.named(LoggingErrorManager.SHOW_ALERTS)).to(true);
	}

}


