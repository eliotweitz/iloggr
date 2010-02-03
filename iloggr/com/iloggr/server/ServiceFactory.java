/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

public interface ServiceFactory<T> {
	T getService(Class<? extends T> serviceClass);
}
