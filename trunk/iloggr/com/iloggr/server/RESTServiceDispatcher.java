/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

/**
 * Servlet that accepts HTTP GET calls and dispatches them to services bound by Guice.
 */
public class RESTServiceDispatcher extends GuicedServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * An interface to a REST service.
	 */
	public interface RestService {

		/**
		 * Returns true if this REST service can handle the given {@code request}.
		 */
		boolean canHandle(HttpServletRequest request);
	}

	/**
	 * Implemented by REST services that should accept GET requests (query).
	 */
	public interface GETService extends RestService {

		/**
		 * Called whenever {@code handle(request) == true} for an HTTP GET request.
		 * The core request servicing logic should go here.
		 */
		void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	}

	/**
	 * Implemented by REST services that should accept POST requests (insert).
	 */
	public interface POSTService extends RestService {

		/**
		 * Called whenever {@code handle(request) == true} for an HTTP POST request.
		 * The core request servicing logic should go here.
		 */
		void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	}

	/**
	 * Implemented by REST services that should accept PUT requests (update).
	 */
	public interface PUTService extends RestService {

		/**
		 * Called whenever {@code handle(request) == true} for an HTTP PUT request.
		 * The core request servicing logic should go here.
		 */
		void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	}

	/**
	 * Implemented by REST services that should accept DELETE requests (delete).
	 */
	public interface DELETEService extends RestService {

		/**
		 * Called whenever {@code handle(request) == true} for an HTTP DELETE request.
		 * The core request servicing logic should go here.
		 */
		void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	}

	@Inject private ImmutableSet<? extends GETService> getServices;
	@Inject private ImmutableSet<? extends POSTService> postServices;
	@Inject private ImmutableSet<? extends PUTService> putServices;
	@Inject private ImmutableSet<? extends DELETEService> deleteServices;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		dispatchRequest(request, response, getServices, Dispatcher.GET);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		dispatchRequest(request, response, postServices, Dispatcher.POST);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		dispatchRequest(request, response, putServices, Dispatcher.PUT);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		dispatchRequest(request, response, deleteServices, Dispatcher.DELETE);
	}

	private interface Dispatcher<T extends RestService> {
		Dispatcher<GETService> GET = new Dispatcher<GETService>() {
			public void dispatch(GETService service , HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				service.query(request, response);
			}
		};
		Dispatcher<POSTService> POST = new Dispatcher<POSTService>() {
			public void dispatch(POSTService service , HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				service.insert(request, response);
			}
		};
		Dispatcher<PUTService> PUT = new Dispatcher<PUTService>() {
			public void dispatch(PUTService service , HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				service.update(request, response);
			}
		};
		Dispatcher<DELETEService> DELETE = new Dispatcher<DELETEService>() {
			public void dispatch(DELETEService service , HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				service.delete(request, response);
			}
		};

		void dispatch(T service, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
	}

	private <T extends RestService> void dispatchRequest(HttpServletRequest request, HttpServletResponse response,
			ImmutableSet<? extends T> services, Dispatcher<T> dispatcher) throws ServletException, IOException {

		// TODO(jsirois): this is quick and lame - there are frameworks out there for REST that might add some welcome
		// unlameness
		for (T service : services) {
			if (service.canHandle(request)) {
				dispatcher.dispatch(service, request, response);
				return;
			}
		}
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
}
