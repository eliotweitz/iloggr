/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.functional;


/**
 * Encapsulates a unit of work.
 *
 * @param <T> the type of the enclosed environment
 */
public interface Closure<T> {

	/**
	 * Performs a unit of work against the environment enclosed in {@code item}.
	 */
	public void execute(T item);

}
