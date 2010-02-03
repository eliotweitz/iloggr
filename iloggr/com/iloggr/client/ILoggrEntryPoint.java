/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class ILoggrEntryPoint implements EntryPoint {

	private final ILoggrGinjector injector = GWT.create(ILoggrGinjector.class);

	public void onModuleLoad() {
		RootPanel.get().add(injector.getUI());
	}
}

