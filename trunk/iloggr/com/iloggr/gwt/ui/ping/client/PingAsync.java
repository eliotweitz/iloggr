/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.ui.ping.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iloggr.gwt.ui.ping.client.Ping.Status;

public interface PingAsync {
	void ping(AsyncCallback<Status> callback);
}
