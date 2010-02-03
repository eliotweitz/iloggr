/**
 * Copyright (C) 2009 Bump Mobile, Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Connects a service to its entrypoint.
 */
public class ServiceConnector {

	public enum RelativeTo {
		HOST_PAGE() {
			@Override String getBaseUrl() {
				return GWT.getHostPageBaseURL();
			}
		},

		MODULE() {
			@Override String getBaseUrl() {
				return GWT.getModuleBaseURL();
			}
		};

		abstract String getBaseUrl();
	}

	private final String serviceEntryPoint;

	public ServiceConnector(RelativeTo relativeTo, String gwtServiceDispatcherRelativePath) {
		this.serviceEntryPoint = relativeTo.getBaseUrl() + gwtServiceDispatcherRelativePath;
	}

	/**
	 * @param <A> the type of the async service
	 * @param <S> the type of the service
	 * @param service the service to connect
	 * @return the connected async service
	 */
	@SuppressWarnings("unchecked")
	public <A, S> A connect(S service) {
		((ServiceDefTarget) service).setServiceEntryPoint(serviceEntryPoint);
		return (A) service;
	}
}