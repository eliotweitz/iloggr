/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.ApplicationController;
import com.iloggr.client.controllers.ILoggrStateController;
import com.iloggr.client.controllers.ILoggrStateController.SubMenuState;
import com.iloggr.client.model.Account;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.InformationField;
import com.iloggr.gwt.util.client.FormatUtility;

public class AccountSummaryPanel extends ListenerPanel /*implements ValueChangeHandler<String> */ {
	private VerticalPanel container = new VerticalPanel();
	private final AccountController accountController;
	private final FormPanel XMLForm = new FormPanel("_blank");
	private final ILoggrStateController stateController;

	@Inject
	public AccountSummaryPanel(AccountController accountController, ApplicationController applicationController, ILoggrStateController stateController) {
		this.accountController = accountController;
		this.stateController = stateController;
	    // Add history listener
//	    History.addValueChangeHandler(this);
		initWidget(createLayout());
	}

	private VerticalPanel createLayout() {
		return container;
	}
	
	public void getXMLFile(String appID) {
		XMLForm.setMethod(FormPanel.METHOD_POST);
		XMLForm.setAction("http://iloggr.com/account");
		XMLForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		Hidden JSONCommand = new Hidden();
		JSONCommand.setName("cmd");  // must have a name or not submitted by form
		String query = buildJSONQuery(accountController.getCurrentAccount().getSecurityToken(),appID);
		JSONCommand.setValue(query);
		XMLForm.setWidget(JSONCommand);
		XMLForm.submit();
	}
	
	//  TODO - EW: Probably a nicer way to do this with the GWT JSON capabilities
	private String buildJSONQuery(String securityToken, String appID) {
		StringBuffer query = new StringBuffer();
		query.append("{\"method\":\"getApplicationIDFileXML\", \"parameters\":[{\"__jsonclass__\":\"String\", \"value\":\"");
		query.append(securityToken);
		query.append("\"},{\"__jsonclass__\":\"String\", \"value\":\"");
		query.append(appID);
		query.append("\"}]}");
		return query.toString();
	}


	public void refresh() {
		container.clear();
		FlexTable infoTable = new FlexTable();
		infoTable.setWidth("60em");
		Account account = accountController.getCurrentAccount();
		if (account == null) {
			container.add(new HTML("<h1>Please log in</h1>"));
		} else {
			int row = 0;
			infoTable.setWidget(0, 0, new Label("Account Name:"));
			infoTable.setWidget(row++, 1, new InformationField(accountController.getCurrentAccount().getName()));
			infoTable.setWidget(row, 0, new Label("Account Token:"));
			infoTable.setWidget(row++, 1, new InformationField(accountController.getCurrentAccount().getSecurityToken()));
			infoTable.setWidget(row, 0, new Label("Primary Email:"));
			infoTable.setWidget(row++, 1, new InformationField(accountController.getCurrentAccount().getEmail()));
			infoTable.setWidget(row++, 0, new HTML("&nbsp"));
			infoTable.setWidget(row++, 0, new HTML("&nbsp"));
			Label ch1 = new Label("Application Name");
			ch1.setStyleName("column-header");
			infoTable.setWidget(row, 0, ch1);
			Label ch2 = new Label("ID");
			ch2.setStyleName("column-header");
			infoTable.setWidget(row, 3, ch2);
			Label ch3 = new Label("Release Date");
			ch3.setStyleName("column-header");
			infoTable.setWidget(row, 6, ch3);
			Label ch4 = new Label("ID File");
			ch4.setStyleName("column-header");
			infoTable.setWidget(row++, 9, ch4);
///			CellFormatter cf = infoTable.getCellFormatter();
			for (final Application a : account.getApplications()) {
				String historyToken = stateController.createHistoryToken(SubMenuState.EditApplication, a.getName());
			    Hyperlink linkToApp = new Hyperlink(a.getName(), historyToken);
				linkToApp.setStyleName("iloggr-application-link");
				infoTable.setWidget(row, 0, linkToApp);
				infoTable.setWidget(row, 3, new InformationField(a.getAppID()));
				infoTable.setWidget(row, 6, new InformationField(FormatUtility.formatDate(a.getReleaseDate())));
				Button downloadFile = new Button("Download iloggr.plist File", new ClickHandler() {
					public void onClick(ClickEvent event) {
						getXMLFile(a.getAppID());
					}});
				infoTable.setWidget(row, 9, downloadFile);
				
				row++;
			}
		}
		container.add(infoTable);
		String historyToken = stateController.createHistoryToken(SubMenuState.NewApplication, null);
	    Hyperlink linkToApp = new Hyperlink("Add new application", historyToken);
		linkToApp.setStyleName("iloggr-application-link");
		container.add(linkToApp);

		container.add(XMLForm);

	}

	/*
	public void onValueChange(ValueChangeEvent<String> event) {
	}*/





}
