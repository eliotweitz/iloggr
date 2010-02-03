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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.StatusController;
import com.iloggr.gwt.util.lang.StringUtility;


public class SignUpPanel extends ListenerPanel {

	private final AccountController accountController;
	private final StatusController statusController;
	private final NewUserPanelMessages messages;

	private final VerticalPanel container = new VerticalPanel();
	private final FlexTable table = new FlexTable();
	private final TextBox invitationCode = new TextBox();
	private final TextBox uname1 = new TextBox();
	private final TextBox uname2 = new TextBox();
	private final PasswordTextBox p1 = new PasswordTextBox();
	private final PasswordTextBox p2 = new PasswordTextBox();
	private final TextBox uName = new TextBox();
	private final TextBox phone = new TextBox();
	private final String textBoxStyle = phone.getStyleName();
//	private final Label msg = new Label();

	private String username;
	private String email;
	private String password;
	private String mobile;
	private String code;

	private final Button signUp = new Button("Sign Up", new ClickHandler() {
		public void onClick(ClickEvent event) {
			doSignup();
		}
	});


	@Inject
	public SignUpPanel(AccountController accountController, NewUserPanelMessages messages, StatusController statusController) {
		this.accountController = accountController;
		this.statusController = statusController;
		this.messages = messages;

		initWidget(createLayout());
	}


	private VerticalPanel createLayout() {
		Label header = new Label(messages.headerMessage());
		header.setStyleName("panelHeader");
		container.add(header);

		int r = 0;
		int c = 0;
		final Label codeLabel = new Label(messages.codeLabel());
		table.setWidget(r, c++, codeLabel);
		table.setWidget(r++, c, invitationCode);
		
		c = 0;
		final Label name = new Label(messages.nameLabel());
		table.setWidget(r, c++, name);
		table.setWidget(r++, c, uName);
		
		c = 0;
		final Label unamelabel = new Label(messages.usernameLabel());
		table.setWidget(r, c++, unamelabel);
		table.setWidget(r, c++, uname1);

		final Label unamelabel2 = new Label(messages.username2Label());
		table.setWidget(r, c++, unamelabel2);
		table.setWidget(r++, c, uname2);
		
		c = 0;
		final Label plabel = new Label(messages.passwordLabel());
		table.setWidget(r, c++, plabel);
		table.setWidget(r, c++, p1);
		final Label plabel2 = new Label(messages.password2Label());
		table.setWidget(r, c++, plabel2);
		table.setWidget(r++, c, p2);
		
		c = 0;
		final Label mlabel = new Label(messages.mobilePhoneNumberLabel());
		table.setWidget(r, c++, mlabel);
		table.setWidget(r, c++, phone);

		r++;

		table.setWidget(r++, 0, signUp);
//		table.setWidget(r++, 0, msg);

		container.add(table);

		return container;
	}

	public boolean validateUser() {
		uname1.setStyleName(textBoxStyle);
		uname2.setStyleName(textBoxStyle);
		invitationCode.setStyleName(textBoxStyle);
		p1.setStyleName(textBoxStyle);
		p2.setStyleName(textBoxStyle);
		
			
		// TODO(jsirois): factor out data entry widgets that can have a validator injected into them to centralise
		// validation logic, stylenames and isolate validation logic for easy testing

		code = invitationCode.getText().trim();
		if ("".equals(code)) {
			statusController.errorMessage(messages.promptCodeInvalid());
			invitationCode.setStyleName("fieldrequired");
			return false;
		}
		
		username = uName.getText().trim();
		if (username.equals("")) {
			statusController.errorMessage(messages.promptRequiredName());
			uName.setStyleName("fieldrequired");
			return false;
		}
		
		email = uname1.getText().trim();
		// Just validate the first email
		if (!StringUtility.isValidEmail(email)) {
			statusController.errorMessage(messages.promptEmailInvalid());
			uname1.setStyleName("fieldrequired");
			return false;
		}
		
		String userEmail2 = uname2.getText().trim();
		if (!StringUtility.isNonBlankMatch(email, userEmail2, true)) {
			statusController.errorMessage(messages.promptEmailMismatch());
			uname1.setStyleName("fieldrequired");
			uname2.setStyleName("fieldrequired");
			return false;
		}

		password = p1.getText().trim();
		String sp2 = p2.getText().trim();

		if (!StringUtility.isNonBlankMatch(password, sp2, false)) {
			statusController.errorMessage(messages.promptPasswordMismatch());
			p1.setStyleName("fieldrequired");
			p2.setStyleName("fieldrequired");
			return false;
		}

		mobile = phone.getText().trim();

		// Listen for keyboard events in the password box.
		phone.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	        	doSignup();
	        }
	      }
	    });

		return true;
	}

public void doSignup() {
	if (validateUser()) {
		signUp.setEnabled(false);
		accountController.createAccount(code, username, email, password, mobile, new VoidActionListener() {
			public void onSuccess() {
				signUp.setEnabled(true);
				// close this panel
//					closePanel();
			}

			public void onFailure() {
				// messages sent to user in controller on failure
				signUp.setEnabled(true);
			}
		});
	}
}


}
