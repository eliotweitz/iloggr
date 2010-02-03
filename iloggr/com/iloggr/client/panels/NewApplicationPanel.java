/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;


import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.StatusController;
import com.iloggr.gwt.util.lang.StringUtility;

public class NewApplicationPanel extends ListenerPanel {
	
	private final AccountController accountController;
	private final NewApplicationPanelMessages messages;
	private final StatusController statusController;

	private final FlexTable	container = new FlexTable();
	private final TextBox nameTextBox = new TextBox();
	private final DatePicker releaseDatePicker = new DatePicker();
	private Date releaseDate;
	private Button saveButton;


 	@Inject
	public NewApplicationPanel(AccountController accountController, NewApplicationPanelMessages messages, StatusController statusController)
	{
		this.accountController = accountController;
		this.messages = messages;
		this.statusController = statusController;

		initWidget(createLayout());
	}
 	
 	

	private FlexTable createLayout() {

		//  Buttons
		 saveButton =
			new Button(messages.createApplication(), new ClickHandler() {
				public void onClick(ClickEvent event) {
					String name = nameTextBox.getText();
					if (StringUtility.isBlank(name)) {
						statusController.errorMessage("Application must have a name!");
						return;
					}
					saveButton.setEnabled(false);
					accountController.createNewApplication(name, releaseDate, new VoidActionListener() {
						public void onSuccess() {
							saveButton.setEnabled(true);
							// close this panel
							closePanel();
						}

						public void onFailure() {
							// messages sent to user in controller on failure
							saveButton.setEnabled(true);
						}
					});
				}});
		 saveButton.setWidth("200px");
		
		
		releaseDatePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				releaseDate = event.getValue(); 
			}
		});
		// Container.setSize("520px", "52px");

		final Label eLabel = new Label(messages.applicationName());
			container.setWidget(0, 0, eLabel);
			eLabel.setWidth("150px");

		container.setWidget(0, 3, nameTextBox);
		nameTextBox.setSize("150px", "23px");

		final Label rd = new Label(messages.releaseDate());
		container.setWidget(1, 0, rd);
		rd.setWidth("100px");

		container.setWidget(1, 3, releaseDatePicker);
		container.setWidget(2, 0, saveButton);

		nameTextBox.setTabIndex(0);
		saveButton.setTabIndex(1);
		saveButton.setTabIndex(2);

		return container;
	}


}
