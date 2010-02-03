/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * A remote endpoint for health checking from the client.
 */
public interface Ping extends RemoteService {

	/**
	 * Represents the server's current health.
	 */
	enum Status implements IsSerializable {

		/**
		 * Indicates the server is ok to service requests.
		 */
		OK,

		/**
		 * Indicates the server is {@link #OK} but in the process of shutting down.
		 */
		LAME_DUCK,

		/**
		 * Indicates the server is not healthy and should will likely serve errors to any further requests.
		 */
		ERROR;
	}

	/**
	 * Returns the current server status.
	 */
	Status ping();
}
