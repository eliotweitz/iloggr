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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.iloggr.client.controllers.AccountController;

public class ForgotPasswordPanel extends Composite {

	private final AccountController accountController;
	private final ForgotPasswordPanelMessages messages;

	private final Label msg = new Label();
	private final VerticalPanel topPanel = new VerticalPanel();
	private final TextBox email = new TextBox();

	private final Button send =
		new Button("Send password reset email", new ClickHandler() {
			public void onClick(ClickEvent event) {
				accountController.resetPasswordEmail(email.getText());
			}
		});

	@Inject
	public ForgotPasswordPanel(AccountController accountController, ForgotPasswordPanelMessages messages) {
		this.accountController = accountController;
		this.messages = messages;

		initWidget(createLayout());
	}

	private Widget createLayout() {
		Label tlabel = new Label(messages.resetPasswordLabel());

		// TODO(jsirois): extract style names to public constants - they're really part of the public interface of the
		// widget
		topPanel.add(tlabel);

		FlowPanel fp = new FlowPanel();
		fp.add(new Label(messages.emailLabel()));
		fp.add(email);
		topPanel.add(fp);

		topPanel.add(send);
		topPanel.add(msg);
		msg.setStyleName("statusErrorMessage");
		
		// Listen for keyboard events in the password box.
		email.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	        	accountController.resetPasswordEmail(email.getText());
	        }
	      }
	    });


		return topPanel;
	}
}
