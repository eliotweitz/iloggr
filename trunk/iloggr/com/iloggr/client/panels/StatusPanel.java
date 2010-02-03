/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Singleton;

@Singleton
public class StatusPanel extends Composite {
    private final Label messageLabel = new Label();
   	private final AbsolutePanel container = new AbsolutePanel();

    public StatusPanel() {
        container.add(messageLabel);
        initWidget(container);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyleName("statusMessage");
    }

    public void setErrorMessage(String message) {
    	messageLabel.setText(message);
        messageLabel.setStyleName("statusErrorMessage");
    }


    public void clear() {
        messageLabel.setText("");
    }

 }
