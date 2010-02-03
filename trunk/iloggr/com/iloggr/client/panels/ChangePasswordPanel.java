/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.StatusController;



public class ChangePasswordPanel extends Composite  {
//	private PasswordTextBox oldPassword = new PasswordTextBox();
	private PasswordTextBox newPassword = new PasswordTextBox();
	private PasswordTextBox newPassword2 = new PasswordTextBox();
	private VerticalPanel topPanel = new VerticalPanel();
	private AccountController accountController;
	private StatusController statusController;
//	private boolean requireOldPassword = true;

	private final Button save =    
		new Button("Change Password", new ClickHandler() {
			public void onClick(ClickEvent event) {
				changePassword();
			}});

	@Inject
	public ChangePasswordPanel(final AccountController accountController, final StatusController statusController) {

		this.accountController = accountController;
		this.statusController = statusController;
		
		initWidget(topPanel);
/*
		if (requireOldPassword) {  NEVER IMPLEMENTED - requre old password
			FlowPanel fp = new FlowPanel();
			topPanel.setSize("300px", "200px");
			fp.add(new Label("Current Password:"));
			fp.add(oldPassword);
			topPanel.add(fp);
		}*/

		FlowPanel fp2 = new FlowPanel();
		fp2.add(new Label("New Password:"));
		fp2.add(newPassword);
		topPanel.add(fp2);

		FlowPanel fp3 = new FlowPanel();
		fp3.add(new Label("Repeat New Password:"));
		fp3.add(newPassword2);
		topPanel.add(fp3);

		// Listen for keyboard events in the password box.
		newPassword2.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	        	changePassword();
	        }
	      }
	    });

		topPanel.add(save);


	}
	
	private void changePassword() {
		if (!newPassword.getText().equalsIgnoreCase(newPassword2.getText())) {
		statusController.errorMessage("Password not changed. New Passwords must match!");
		return;
		}
		accountController.resetPassword(accountController.getCurrentAccount().getSecurityToken(), newPassword.getText());
	}

	

}
