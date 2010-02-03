/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

public interface ActionListener<T> {
	public void onSuccess(T object);
	public void onFailure();

}
