/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iloggr.server.JSON.JSONRPCResponse;


/**
 * Used by all servlet classes to read and write JSON requests.
 *
 * @author eliot
 * @version 1.0
 */
public class RPCServletUtils {
	private static final String ACCEPT_ENCODING = "Accept-Encoding";

	private static final String CHARSET_UTF8 = "UTF-8";

	private static final String CONTENT_DISPOSITION = "Content-Disposition";

	private static final String CONTENT_ENCODING = "Content-Encoding";

	private static final String CONTENT_ENCODING_GZIP = "gzip";

//	private static final String EXPECTED_CHARSET = "charset=utf-8";

//	private static final String EXPECTED_CONTENT_TYPE = "text/x-iloggr-rpc";

	private static final String CONTENT_TYPE_APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

	private static final String CONTENT_TYPE_APPLICATION_CSV_UTF8 = "application/csv; charset=utf8";

	private static final String CONTENT_TYPE_APPLICATION_XML_UTF8 = "text/plain; charset=utf8";

	private static final String CSV_ATTACHMENT = "attachment; filename=report.csv";
	
	private static final String XML_ATTACHMENT = "attachment; filename=iloggr.plist";

	private static final String JSON_ATTACHMENT = "attachment";


	
	public enum ContentType {JSON, CSV, XML};

	/**
	 * Controls the compression threshold at and below which no compression will
	 * take place.
	 */
	private static final int UNCOMPRESSED_BYTE_SIZE_LIMIT = 2048;
	
	public static String getContentTypeString(ContentType ct) {
		if (ct == ContentType.CSV) return CONTENT_TYPE_APPLICATION_CSV_UTF8;
		else if (ct == ContentType.JSON) return CONTENT_TYPE_APPLICATION_JSON_UTF8;
		else return CONTENT_TYPE_APPLICATION_XML_UTF8;
	}
	
	public static String getContentTypeAttachment(ContentType ct) {
		if (ct == ContentType.CSV) return CSV_ATTACHMENT;
		else if (ct == ContentType.XML) return XML_ATTACHMENT;
		else return JSON_ATTACHMENT;
	}

	/**
	 * Returns <code>true</code> if the {@link HttpServletRequest} accepts Gzip
	 * encoding. This is done by checking that the accept-encoding header
	 * specifies gzip as a supported encoding.
	 *
	 * @param request the request instance to test for gzip encoding acceptance
	 * @return <code>true</code> if the {@link HttpServletRequest} accepts Gzip
	 *         encoding
	 */
	public static boolean acceptsGzipEncoding(HttpServletRequest request) {
		assert (request != null);

		String acceptEncoding = request.getHeader(ACCEPT_ENCODING);
		if (null == acceptEncoding) {
			return false;
		}

		return (acceptEncoding.indexOf(CONTENT_ENCODING_GZIP) != -1);
	}

	/**
	 * Returns <code>true</code> if the response content's estimated UTF-8 byte
	 * length exceeds 256 bytes.
	 *
	 * @param content the contents of the response
	 * @return <code>true</code> if the response content's estimated UTF-8 byte
	 *         length exceeds 256 bytes
	 */
	public static boolean exceedsUncompressedContentLengthLimit(String content) {
		return (content.length() * 2) > UNCOMPRESSED_BYTE_SIZE_LIMIT;
	}

	/**
	 * Returns the content of an {@link HttpServletRequest} by decoding it using
	 * the UTF-8 charset.
	 *
	 * @param request the servlet request whose content we want to read
	 * @return the content of an {@link HttpServletRequest} by decoding it using
	 *         the UTF-8 charset
	 * @throws IOException if the requests input stream cannot be accessed, read
	 *           from or closed
	 * @throws ServletException if the content length of the request is not
	 *           specified of if the request's content type is not
	 *           'text/x-gwt-rpc' and 'charset=utf-8'
	 */
	public static String readContent(HttpServletRequest request)
	throws IOException, ServletException {
		int contentLength = request.getContentLength();
		if (contentLength == -1) {
			// Content length must be known.
			throw new ServletException("Content-Length must be specified");
		}
		String characterEncoding = request.getCharacterEncoding();
		// Read the input stream until either empty or a full JSON object has been extracted
		InputStream in = request.getInputStream();
		String result;
		try {
			int balancedBraces = 0; // will keep track of balanced braces
			byte[] payload = new byte[contentLength];
			int offset = 0;
			int nextByte;
			boolean first = true;
			nextByte = in.read();
			// while there is still data, we have already found the top level "{" and we have not found the 
			// matching top level "}"
			while(nextByte > 0 && (first || balancedBraces != 0)) {
				if (nextByte == '{') {
					balancedBraces++;
					first = false;
				}
				if (nextByte == '}') balancedBraces--;
				if (!first) payload[offset++] = (byte)nextByte;
				nextByte = in.read();
			}
			if (characterEncoding != null && characterEncoding.equalsIgnoreCase(CHARSET_UTF8)) {
				result = new String(payload, CHARSET_UTF8);
			} else {
				result = new String(payload);  // use platform default encoding
			}
		} catch (Exception e) {
			throw new ServletException("Error while encoding request bytes");
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if the request accepts gzip encoding and the
	 * the response content's estimated UTF-8 byte length exceeds 256 bytes.
	 *
	 * @param request the request associated with the response content
	 * @param responseContent a string that will be
	 * @return <code>true</code> if the request accepts gzip encoding and the
	 *         the response content's estimated UTF-8 byte length exceeds 256
	 *         bytes
	 */
	public static boolean shouldGzipResponseContent(HttpServletRequest request,
			String responseContent) {
		return acceptsGzipEncoding(request)
		&& exceedsUncompressedContentLengthLimit(responseContent);
	}

	/**
	 * Write the response content into the {@link HttpServletResponse}. If
	 * <code>gzipResponse</code> is <code>true</code>, the response content
	 * will be gzipped prior to being written into the response.
	 *
	 * @param servletContext servlet context for this response
	 * @param response response instance
	 * @param responseContent a string containing the response content
	 * @param gzipResponse if <code>true</code> the response content will be
	 *          gzip encoded before being written into the response
	 * @throws IOException if reading, writing, or closing the response's output
	 *           stream fails
	 */
	public static void writeResponse(ServletContext servletContext,
			HttpServletResponse response, String responseContent, boolean gzipResponse, 
			ContentType ct)
	throws IOException {

		byte[] responseBytes = responseContent.getBytes(CHARSET_UTF8);
		if (gzipResponse) {
			// Compress the reply and adjust headers.
			//
			ByteArrayOutputStream output = null;
			GZIPOutputStream gzipOutputStream = null;
			Throwable caught = null;
			try {
				output = new ByteArrayOutputStream(responseBytes.length);
				gzipOutputStream = new GZIPOutputStream(output);
				gzipOutputStream.write(responseBytes);
				gzipOutputStream.finish();
				gzipOutputStream.flush();
				response.setHeader(CONTENT_ENCODING, CONTENT_ENCODING_GZIP);
				responseBytes = output.toByteArray();
			} catch (IOException e) {
				caught = e;
			} finally {
				if (null != gzipOutputStream) {
					gzipOutputStream.close();
				}
				if (null != output) {
					output.close();
				}
			}

			if (caught != null) {
				servletContext.log("Unable to compress response", caught);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		}

		// Send the reply.
		//
		response.setContentLength(responseBytes.length);
		
		String contentType = getContentTypeString(ct);
		String contentAttachment = getContentTypeAttachment(ct);
		
		response.setContentType(contentType);
		response.setHeader(CONTENT_DISPOSITION, contentAttachment);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getOutputStream().write(responseBytes);
	}



	/**
	 * Called when the servlet itself has a problem, rather than the invoked
	 * third-party method. It writes a simple 500 message back to the client.
	 *
	 * @param servletContext
	 * @param response
	 * @param failure
	 */
	public static void writeResponseForUnexpectedFailure(
			ServletContext servletContext, HttpServletResponse response,
			Throwable failure) {
		servletContext.log("Exception while dispatching incoming RPC call", failure);

		// Send GENERIC_FAILURE_MSG with 500 status.
		//
		try {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			//	      response.getWriter().write(JSONErrorString(GENERIC_FAILURE_MSG));
			JSONRPCResponse jResponse = new JSONRPCResponse(failure);
			response.getWriter().write(jResponse.encode().toString());
//			response.getWriter().write(JSONRPCRequest.encodeResponseForFailure(failure));
//			response.getWriter().write(JSONErrorString(failure.getMessage()));
		} catch (IOException ex) {
			servletContext.log(
					"respondWithUnexpectedFailure failed while sending the previous failure to the client",
					ex);
		}
	}
/*
	public static String JSONErrorString(String errorMsg) {
		JSONObject jr = new JSONObject();
		jr.put("__jsonclass__", "Response");
		jr.put("result", null);
		jr.put("errorMessage", errorMsg);
		jr.put("error", -1);
		return jr.toString();
	}*/

	private RPCServletUtils() {
		// Not instantiable
	}
}
