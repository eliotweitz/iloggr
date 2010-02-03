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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.StatusController;


public class LoginPanel extends ListenerPanel {

	/*	public interface LoginController {
		void showChangePasswordPanel();
		void showForgotPasswordPanel();
	}*/

	private final AccountController accountController;
	private final LoginPanelMessages messages;
	private final StatusController statusController;

	private final FlexTable	container = new FlexTable();
	private final PasswordTextBox passwordTextBox = new PasswordTextBox();
	private final TextBox eTextBox = new TextBox();
	private final String textBoxStyle = eTextBox.getStyleName();

	//  Buttons
	ClickHandler loginClickHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			doLogin();
		}};
		
	private void doLogin() {
		if (!validate()) return;
		String password = passwordTextBox.getText();
		String email = eTextBox.getText();
		loginButton.setEnabled(false);
		accountController.authenticateAccount(email, password, new VoidActionListener() {
			public void onSuccess() {
				loginButton.setEnabled(true);
				// close this panel
				closePanel();
			}

			public void onFailure() {
				// messages sent to user in controller on failure
				loginButton.setEnabled(true);
			}


		});
	}
		
	private final Button loginButton = new Button("Log in", loginClickHandler);


	@Inject
	public LoginPanel(AccountController accountController, /*LoginController loginController, */LoginPanelMessages messages,
			StatusController statusController)
	{
		this.accountController = accountController;
		//		this.loginController = loginController;
		this.messages = messages;
		this.statusController = statusController;

		initWidget(createLayout());
	}

	private FlexTable createLayout() {
		// Container.setSize("520px", "52px");

		final Label eLabel = new Label(messages.usernameLabel());
		container.setWidget(0, 0, eLabel);
	//	eLabel.setWidth("81px");

		container.setWidget(0, 3, eTextBox);
//		eTextBox.setSize("150px", "23px");

		final Label passwordLabel = new Label(messages.passwordLabel());
		container.setWidget(1, 0, passwordLabel);

		container.setWidget(1, 3, passwordTextBox);
//		passwordTextBox.setWidth("105px");

		container.setWidget(2, 0, loginButton);
//		loginButton.setWidth("74px");


		eTextBox.setTabIndex(0);
		passwordTextBox.setTabIndex(1);
		loginButton.setTabIndex(2);
		
		// Listen for keyboard events in the password box.
		passwordTextBox.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	          doLogin();
	        }
	      }
	    });


		return container;
	}
	
	public boolean validate() {
		eTextBox.setStyleName(textBoxStyle);
		passwordTextBox.setStyleName(textBoxStyle);
		
		if ("".equals(eTextBox.getText().trim())) {
			statusController.errorMessage("User email required");
			eTextBox.setStyleName("fieldrequired");
			return false;
		}

		if ("".equals(passwordTextBox.getText().trim())) {
			statusController.errorMessage("User password required");
			passwordTextBox.setStyleName("fieldrequired");
			return false;
		}

		
		return true;
	}
	
	

	
}
