/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.lang;

public class Objects {

	public static boolean isInstance(Class<?> type, Object object) {
		if (type == null) {
			throw new NullPointerException("type cannot be null");
		}
		if (type.isInterface()) {
			throw new IllegalArgumentException("type must be a class, passed an interface: " +
					type.getName());
		}

		if (object == null) {
			return false;
		}

		Class<? extends Object> instanceType = object.getClass();
		if (instanceType == null) {
			throw new IllegalStateException("impossible: null type for a concrete object: " + object);
		}

		do {
			if (instanceType.equals(type)) {
				return true;
			}
		} while ((instanceType = instanceType.getSuperclass()) != null);
		return false;
	}

	private Objects() {
		// utility
	}
}
