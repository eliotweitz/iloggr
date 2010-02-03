/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.controllers;

public interface StatusController {
	void statusMessage(String message);
	void errorMessage(String message);
	void waitMessage();
	void doneMessage();
	
}