/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.server.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.iloggr.client.model.Account;
import com.iloggr.server.RESTServiceDispatcher.GETService;
import com.iloggr.server.RESTServiceDispatcher.POSTService;
import com.iloggr.server.managers.AccountManager;

//TODO: eliot - For restful account activation - cleanup later
public class AccountRESTService implements GETService, POSTService {

	private final AccountServiceImpl accountService;

	@Inject
	public AccountRESTService(AccountServiceImpl accountService) {
		this.accountService = accountService;
	}

	public boolean canHandle(HttpServletRequest request) {
		String cmd = request.getParameter("cmd");
		return (cmd != null && 
				(cmd.equalsIgnoreCase("activate") ||
						cmd.equalsIgnoreCase("resetpassword"))
		);
	}

	// TODO(jsirois): this should be converted to a pure POSTService
	public void query(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service(request, response);
	}

	public void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service(request, response);
	}

	private void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String token = request.getParameter("token");
			String id = request.getParameter("id");
			String cmd = request.getParameter("cmd");
			if (cmd.equalsIgnoreCase("activate")) {
				if (id != null && token != null) {
					// Activate
					accountService.activateAccount(Long.parseLong(id), token);
					response.sendRedirect("http://iloggr.com/AccountActivated.html");
				} else {
					sendError(response, "An error has occurred during activation, if this persists");
				}
			} else if (cmd.equalsIgnoreCase("resetpassword")) {
				if (token != null) {
					// reset
					AccountManager am = new AccountManager();
					Account acct = am.getAccount(Long.parseLong(id));
					if (acct == null || !acct.getEmailToken().equals(token) ){
						sendError(response, "Bad reset token");
						return;
					}
					// activate if not already activated
					accountService.activateAccount(Long.parseLong(id), token);
					// show a password reset  screen
					// mainstate&submenustate&token
					String historyToken = "AccountManagement&ChangePassword&" +
					acct.getSecurityToken();
					response.sendRedirect("http://iloggr.com/ILoggr.html#"+historyToken);
				} else {
					sendError(response, "No reset token provided");
				}
			}
		} catch (Throwable e) {
			sendError(response, "This link is invalid");
		}
	}

	public void sendError(HttpServletResponse response, String error) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("<h1>Error: " + error + ", please contact support at: support@iloggr.com</h1>");  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


