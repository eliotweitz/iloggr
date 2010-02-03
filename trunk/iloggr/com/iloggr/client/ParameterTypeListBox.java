/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.user.client.ui.ListBox;

public class ParameterTypeListBox extends ListBox {

	public ParameterTypeListBox() {
		super();
		this.addStyleName("iloggr-info-field");
		addItems();
		this.setSelectedIndex(0);
	}
	
	private void addItems() {
		this.addItem("String");
		this.addItem("Integer");
		this.addItem("Float");
		this.addItem("Boolean");
	}

}
