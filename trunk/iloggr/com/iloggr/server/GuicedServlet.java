/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * A baseclass for servlets that wish to participate in the guice bindings provided for this vm.  Dependencies for the
 * servlet should be expressed as {@literal @Inject}ed fields.  These fields are guaranteed to be injected prior to
 * any request-serving methods are invoked by the servlet container.
 */
public abstract class GuicedServlet extends HttpServlet {

	@Override
	public final void init() throws ServletException {
		Bindings.getBindings().injectMembers(this);
	}
}
