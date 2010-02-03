/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;


public abstract class ILoggrVoidAsyncHandler implements ILoggrAsyncHandler<Void> {

	public final void execute(Void item) {
		execute();
	}

	protected abstract void execute();

}
