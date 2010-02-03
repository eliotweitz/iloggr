/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.inject.Inject;

/**
 * Servlet that accepts GWT RPC calls and dispatches them to services bound by Guice.
 */
public class GWTServiceDispatcher extends GuicedServlet {

	private static final long serialVersionUID = 1L;

	@Inject private ServiceFactory<RemoteService> serviceFactory;

	// TODO(jsirois): robustify
	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
		throws ServletException, IOException {

		String rawRequest = RPCServletUtils.readContentAsUtf8(httpRequest);
		RPCRequest rpcRequest = RPC.decodeRequest(rawRequest);
		Method serviceMethod = rpcRequest.getMethod();

		@SuppressWarnings("unchecked")
		Class<? extends RemoteService> serviceClass =
			(Class<? extends RemoteService>) serviceMethod.getDeclaringClass();

		RemoteService service = serviceFactory.getService(serviceClass);
		try {
			String responseContent = RPC.invokeAndEncodeResponse(service, serviceMethod, rpcRequest.getParameters());
			RPCServletUtils.writeResponse(getServletContext(), httpResponse, responseContent,
				RPCServletUtils.shouldGzipResponseContent(httpRequest, responseContent));
		} catch (SerializationException e) {
			RPCServletUtils.writeResponseForUnexpectedFailure(getServletContext(), httpResponse, e);
		}
	}
}
