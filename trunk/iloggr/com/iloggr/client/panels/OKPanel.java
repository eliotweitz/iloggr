package com.iloggr.client.panels;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class OKPanel extends DialogBox {

	public String text;
	public ClickHandler OKHandler;
	public ClickHandler cancelHandler;
	public Button ok;
	public Button cancel;


	public OKPanel(String OKText, String CancelText, String text) {
		super();
		// Set the dialog box's caption.

		// DialogBox is a SimplePanel, so you have to set its widget property to
		// whatever you want its contents to be.
		ok = new Button(OKText);
		cancel = new Button(CancelText);
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label(text));
		panel.add(new Label(" "));
		panel.add(ok);
		panel.add(cancel);
		this.text = text;
//		setText(text);
		setWidget(panel);
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		setText(text);
		this.text = text;
	}


	public ClickHandler getOKHandler() {
		return OKHandler;
	}


	public void setOKHandler(ClickHandler handler) {
		ok.addClickHandler(handler);
		OKHandler = handler;
	}


	public ClickHandler getCancelHandler() {
		return cancelHandler;
	}


	public void setCancelHandler(ClickHandler cancelHandler) {
		cancel.addClickHandler(cancelHandler);
		this.cancelHandler = cancelHandler;
	}


}
