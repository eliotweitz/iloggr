/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author eliot
 * Used by sub panels to get notified when controller async tasks have completed.  also
 * used by panels to trigger panel onClose listeners so main panel can refresh or other
 * panels can be notified.
 *
 */
public interface SubPanelListener {
	/**
	 * Called when sub panel has completed its task successfully.
	 * 
	 * @param subPanel The sub panel that has closed.
	 */
	void onClose(Composite subPanel);

}
