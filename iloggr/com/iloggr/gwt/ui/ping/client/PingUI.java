/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.iloggr.gwt.ui.ping.client.Ping.Status;
import com.iloggr.gwt.util.client.AsyncCallbackFactory;
import com.iloggr.gwt.util.functional.Closure;

public class PingUI extends Composite {

	public static final String STYLE_PREFIX = "iLoggr-PingUI-";
	public static final String STATUS_PRIMARY_STYLE = STYLE_PREFIX + "Status";
	public static final String OK_MESSAGE = "OkMessage";
	public static final String NOT_OK_MESSAGE = "NotOkMessage";
	public static final String UNKOWN_MESSAGE = "UnknownMessage";

	static final String STATUS_FADE_DELAY_MS = "com.iloggr.gwt.ui.ping.client.PingUI.STATUS_FADE_DELAY_MS";

	private final PingAsync pingService;
	private final PingMessages messages;
	private final AsyncCallbackFactory callbackFactory;
	private final int statusFadeDelayMs;

	private Label statusMessage;
	private AbsolutePanel mainPanel;

	@Inject
	public PingUI(PingAsync pingService, AsyncCallbackFactory callbackFactory, PingMessages messages,
		@Named(STATUS_FADE_DELAY_MS) int statusFadeDelayMs) {

		this.pingService = pingService;
		this.callbackFactory = callbackFactory;
		this.messages = messages;
		this.statusFadeDelayMs = statusFadeDelayMs;

		this.initWidget(createLayout());
	}

	private Widget createLayout() {
		mainPanel = new AbsolutePanel();
		// Full screen
		mainPanel.setWidth("100%");
		mainPanel.setHeight("100%");

		DockPanel dockPanel = new DockPanel();
		// Full screen
		dockPanel.setWidth("100%");
		dockPanel.setHeight("100%");

		// Setup the health check button dead-center
		Button button = new Button(messages.pingButtonLabel(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				pingService.ping(callbackFactory.createAsyncCallback(new Closure<Status>(){
					public void execute(Status status) {
						displayStatus(status);
					}
				}));
			}
		});
		dockPanel.add(button, DockPanel.CENTER);
		dockPanel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_CENTER);
		dockPanel.setCellVerticalAlignment(button, HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.add(dockPanel, 0, 0);

		// Setup a top status area with an initially hidden status message that overlays the dockPanel
		statusMessage = new Label("");
		statusMessage.setWordWrap(false);
		statusMessage.setVisible(false);
		statusMessage.setStylePrimaryName(STATUS_PRIMARY_STYLE);
		mainPanel.add(statusMessage, -1, -1); // Enable static positioning for statusMessage
		positionStatus();

		Window.addResizeHandler(new ResizeHandler(){
			public void onResize(ResizeEvent event) {
				positionStatus();
			}
		});

		return mainPanel;
	}

	private void displayStatus(Status status) {
		statusMessage.removeStyleDependentName(OK_MESSAGE);
		statusMessage.removeStyleDependentName(NOT_OK_MESSAGE);
		statusMessage.removeStyleDependentName(UNKOWN_MESSAGE);

		switch (status) {
			case OK:
				statusMessage.addStyleDependentName(OK_MESSAGE);
				statusMessage.setText(messages.okStatusMessage());
				break;

			case LAME_DUCK:
			case ERROR:
				statusMessage.addStyleDependentName(NOT_OK_MESSAGE);
				statusMessage.setText(messages.notOkStatusMessage(status));
				break;

			default:
				statusMessage.addStyleDependentName(UNKOWN_MESSAGE);
				statusMessage.setText(messages.unknownStatusMessage(status));
				break;
		}

		showStatusAndScheduleHide();
	}

	private void showStatusAndScheduleHide() {
		statusMessage.setVisible(true);
		positionStatus();
		new Timer() {
			@Override public void run() {
				statusMessage.setVisible(false);
			}
		}.schedule(statusFadeDelayMs);
	}

	private void positionStatus() {
		int left = Window.getClientWidth() - statusMessage.getOffsetWidth();
		mainPanel.setWidgetPosition(statusMessage, left, 0);
	}
}
