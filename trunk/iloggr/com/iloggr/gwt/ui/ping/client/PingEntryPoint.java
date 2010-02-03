/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class PingEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		PingGinjector pingGinjector = GWT.create(PingGinjector.class);
		RootPanel.get().add(pingGinjector.getUI());
	}

}
