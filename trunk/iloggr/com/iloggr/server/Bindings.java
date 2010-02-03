/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Supports sharing of bindings by providing a singleton global injector for use by many servlets running in the same
 * vm.
 */
public class Bindings {
	
	// Threadsafe holder idiom - ensure created just once with no locks
	private static class InjectorHolder {
		private static final Injector injector = createInjector();
	}
	
	/**
	 * Provides the global bindings.
	 */
	public static final Injector getBindings() {
		return InjectorHolder.injector;
	}
	
	private static Injector createInjector() {
		return Guice.createInjector(new BindingsModule());
	}
	
	private Bindings() {
		// utility
	}
}
