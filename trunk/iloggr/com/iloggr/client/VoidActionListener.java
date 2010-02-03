/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

public abstract class VoidActionListener implements ActionListener<Void> {

	public void onSuccess(Void object) {
		onSuccess();
	}
	
	public abstract void onSuccess();

}
