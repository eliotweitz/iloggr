/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.iloggr.gwt.util.client.AsyncCallbackFactory.ErrorManager;

/**
 * A simple {@code ErrorManager} for development that just logs via {@link GWT#log(String, Throwable)}.
 */
public class LoggingErrorManager implements ErrorManager {

	/**
	 * {@literal @Named} key to configure {@link #setShowAlerts(boolean)}.
	 */
	public static final String SHOW_ALERTS = "com.iloggr.gwt.util.client.LoggingErrorManager.SHOW_ALERTS";

	private boolean showAlerts;

	@Inject(optional = true)
	public void setShowAlerts(@Named(SHOW_ALERTS) boolean showAlerts) {
		this.showAlerts = showAlerts;
	}

	public void onError(Throwable caught) {
		GWT.log(caught.getMessage(), caught);
		if (showAlerts) {
			Window.alert(caught.getMessage());
		}
	}

}
