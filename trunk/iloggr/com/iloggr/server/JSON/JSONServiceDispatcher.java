/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.JSON;


import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.iloggr.gwt.util.client.ILException;
import com.iloggr.server.GuicedServlet;
import com.iloggr.server.RPCServletUtils;
import com.iloggr.server.ServiceFactory;
import com.iloggr.server.RPCServletUtils.ContentType;


/**
 * Servlet that accepts JSON RPC calls and dispatches them to services bound by Guice.
 */
public class JSONServiceDispatcher extends GuicedServlet {

	private static final long serialVersionUID = 1L;
	
	@Inject private ServiceFactory<Object> serviceFactory;

	@Override
	public final void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// Read the request fully.
			//
			String requestPayload = RPCServletUtils.readContent(request);

			// Parse the top level JSON object and form a request
			JSONRPCRequest rpcRequest = JSONRPC.decodeRequest(requestPayload);

			// Lookup the underlying method in the request
			Method serviceMethod = rpcRequest.getMethod();
			
			// Dispatching the request
			Object result = processCall(serviceMethod, rpcRequest);

			//  Get return encoding for method
			ContentType ct = getContentType(serviceMethod);
					
	  		// Serialized the result.
	  		String responsePayload = (String) ((ct != ContentType.JSON)?result:processResponse(result));

	  		// Write the response.
	  		writeResponse(request, response, responsePayload, ct);
		} catch (Throwable t) {
			doUnexpectedFailure(response, t);
		}
	}
	
	/**
	 * Determine what encoding for the response will be used for this method.  Currently, there are
	 * only two type: JSON encoded (default) and CSV file encoding for reporting methods.
	 * 
	 * CSV Methods have "CSV" embedded in their name
	 * XML Methods has "XML" embedded in their name
	 * 
	 * @param serviceMethod The method to look up for it's return encoding type
	 * @return
	 */
	public ContentType getContentType(Method serviceMethod) {
		String name = serviceMethod.getName();
		if (name.indexOf("CSV") > 0) return ContentType.CSV;
		else if (name.indexOf("XML") > 0) return ContentType.XML;
		else return ContentType.JSON;
	}
	
	
	private Object processCall(Method serviceMethod, JSONRPCRequest rpcRequest) throws Exception {
		if (serviceMethod == null) {
			throw new ILException(ILException.INVALID_SERVICE_REQUEST);
		}
		Class<?> serviceClass = serviceMethod.getDeclaringClass();
		Object service = serviceFactory.getService(serviceClass);
		if (service == null) {
			throw new ILException(ILException.INVALID_SERVICE_REQUEST);
		}
		return serviceMethod.invoke(service, rpcRequest.getParameters());
	}

	private String processResponse(Object result) {
		JSONRPCResponse response = new JSONRPCResponse(result);
		return response.encode().toString();
	}

	private void writeResponse(HttpServletRequest request,
		HttpServletResponse response, String responsePayload, ContentType ct) throws IOException {
	    boolean gzipEncode = RPCServletUtils.acceptsGzipEncoding(request)
	        && shouldCompressResponse(responsePayload);

	    RPCServletUtils.writeResponse(getServletContext(), response,
	        responsePayload, gzipEncode, ct);
	}

	private boolean shouldCompressResponse(String responsePayload) {
		return (responsePayload.length() > 2048);
	}

	protected void doUnexpectedFailure(HttpServletResponse response, Throwable e) {
	    ServletContext servletContext = getServletContext();
	    RPCServletUtils.writeResponseForUnexpectedFailure(servletContext, response, e);
    }
}
