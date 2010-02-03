/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MainMenu extends MenuBar {

	MenuItem selectedItem;

	public MainMenu() {
		super();
		this.selectedItem = null;
	}
	

	public MainMenu(boolean vertical) {
		super(vertical);
		this.selectedItem = null;
	}


	public void showSelected() {
		MenuItem selected = this.getSelectedItem();
		if (selectedItem != null) {
			// return to normal
			selectedItem.removeStyleName("menuItemSelected");
		}
		this.selectedItem = selected;
		if (selectedItem != null) selected.addStyleName("menuItemSelected");
	}

	@Override 
	public MenuItem addItem(MenuItem item) {
		item.addStyleName("iloggr-MenuItem");
		return super.addItem(item);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
			case Event.ONCLICK: {
				showSelected();
			}
		}
	}

	/*private MenuItem findItem(Element hItem) {
		for (MenuItem item : getItems()) {
			if (DOM.isOrHasChild(item.getElement(), hItem)) {
				return item;
			}
		}
		return null;
	}*/


}
