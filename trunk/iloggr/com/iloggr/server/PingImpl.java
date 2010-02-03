/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import java.util.Random;

import com.google.inject.Inject;
import com.iloggr.gwt.ui.ping.client.Ping;

public class PingImpl implements Ping {

	private final Random random;

	@Inject
	public PingImpl(Random random) {
		this.random = random;
	}

	public Status ping() {
		return Status.values()[random.nextInt(Status.values().length)];
	}

}
