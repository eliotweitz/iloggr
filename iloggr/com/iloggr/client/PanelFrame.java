/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PanelFrame extends Composite {
	VerticalPanel container = new VerticalPanel();
	Label panelLabel = new Label();
	VerticalPanel main = new VerticalPanel();

	public PanelFrame() {
		super();
		container.add(panelLabel);
		panelLabel.setStyleName("PanelFrameTitle");
		container.setSpacing(5);
		container.setCellHeight(panelLabel, "10%");
		container.setCellWidth(panelLabel, "100%");
		container.add(main);
		container.setCellWidth(main, "100%");
		container.setCellHeight(main, "100%");
		container.setCellWidth(panelLabel, "100%");
		container.setBorderWidth(0);
		initWidget(container);
	}
	
	public void setTitle(String title) {
		panelLabel.setText(title);
	}
	
	public void setMain(Widget w) {
		main.clear();
		main.add(w);
//		main.setHeight("100%");
//		main.setWidth("100%");
		main.setCellWidth(w, "100%");
		main.setCellHeight(w, "100%");
		// Set these sizes in the widget (w) container
//		main.setHeight("600px");
//		main.setWidth("1000px");
	}
	

}
