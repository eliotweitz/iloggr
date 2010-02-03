/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;

import com.google.gwt.user.client.ui.Composite;
import com.iloggr.client.SubPanelListener;

public class ListenerPanel extends Composite {
	protected SubPanelListener subPanelListener;
	
	public void setSubPanelListener(SubPanelListener listener) {
		this.subPanelListener = listener;
	}

	public SubPanelListener getSubPanelListener() {
		return subPanelListener;
	}
	
	public void closePanel() {
		subPanelListener.onClose(this);
	}



}
