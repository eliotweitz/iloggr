/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Label;

public class InformationField extends Label {

	public InformationField() {
		super();
		this.setStyleName("iloggr-info-field");
	}

	public InformationField(Element element) {
		super(element);
		this.setStyleName("iloggr-info-field");
	}

	public InformationField(String text, boolean wordWrap) {
		super(text, wordWrap);
		this.setStyleName("iloggr-info-field");
	}

	public InformationField(String text) {
		super(text);
		this.setStyleName("iloggr-info-field");
	}
	
}
