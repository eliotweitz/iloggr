/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubMenuPanel extends Composite {
	VerticalPanel container = new VerticalPanel();

	public SubMenuPanel() {
		super();
		initWidget(container);
	}
	
	public void setMenu(MenuBar menu) {
		this.container.clear();
		this.container.add(menu);
	}
	
/*	public void setMenu(Tree tree) {
		this.container.clear();
		this.container.add(tree);
	}
	*/
	
	

}


