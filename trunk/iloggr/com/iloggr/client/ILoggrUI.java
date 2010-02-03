/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;


import java.util.HashSet;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.ApplicationController;
import com.iloggr.client.controllers.ILoggrStateController;
import com.iloggr.client.controllers.StatusController;
import com.iloggr.client.controllers.ILoggrStateController.MainMenuState;
import com.iloggr.client.controllers.ILoggrStateController.SubMenuState;
import com.iloggr.client.controllers.ILoggrStateController.UserState;
import com.iloggr.client.model.Application;
import com.iloggr.client.panels.AccountSummaryPanel;
import com.iloggr.client.panels.ApplicationManagementPanel;
import com.iloggr.client.panels.ChangePasswordPanel;
import com.iloggr.client.panels.ForgotPasswordPanel;
import com.iloggr.client.panels.LoginPanel;
import com.iloggr.client.panels.NewApplicationPanel;
import com.iloggr.client.panels.OKPanel;
import com.iloggr.client.panels.SignUpPanel;
import com.iloggr.client.panels.StatusPanel;
import com.iloggr.client.services.RecordServiceAsync;
import com.iloggr.gwt.util.client.AsyncCallbackFactory;

/**
 * @author eliot
 */
@Singleton
public class ILoggrUI extends Composite  {

	private final SignUpPanel signUpPanel;
	private final LoginPanel loginPanel;
	private final ChangePasswordPanel changePasswordPanel;
	private final ForgotPasswordPanel forgotPasswordPanel;
	private final AccountController accountController;
	private final ApplicationController applicationController;
	private final StatusPanel statusPanel;
	private final StatusController statusController;
	private final NewApplicationPanel newApplicationPanel;
	private final ApplicationManagementPanel applicationManagementPanel;
	private final AccountSummaryPanel accountSummaryPanel;
	private final ILoggrStateController stateController;

	private final DockPanel dockPanel = new DockPanel();
	private final AbsolutePanel topOfPage = new AbsolutePanel();
	private final SubMenuPanel subMenuPanel = new SubMenuPanel();
	private final PanelFrame centerPanel = new PanelFrame();

	// various sub menus based on main menu items
	//	private final Tree accountSubMenu = createAccountSubMenu();
	private final MainMenu informationSubMenu = createInformationSubMenu();
	private final MainMenu supportSubMenu = createSupportSubMenu();
	private final MainMenu signUpSubMenu = createSignUpSubMenu();
	private final MainMenu mainMenu = createMainMenu();

	private final AbsolutePanel toDoPanel = createToDoPanel();

	HashSet<TreeItem> ApplicationItems;

	// Most panels will require a main panel refresh.  This listener's onClose method will assure
	// that a refresh is done when the sub panel closes.
	private final SubPanelListener refreshListener = new SubPanelListener()  {
		public void onClose(Composite subPanel) {
			refresh();
		}
	};

	@Inject
	public ILoggrUI(AccountController accountController, SignUpPanel signUpPanel, LoginPanel loginPanel,
			ChangePasswordPanel changePasswordPanel, ForgotPasswordPanel forgotPasswordPanel, StatusPanel statusPanel,
			NewApplicationPanel newApplicationPanel, ApplicationManagementPanel applicationManagementPanel, 
			ApplicationController applicationController, RecordServiceAsync recordService, AccountSummaryPanel accountSummaryPanel,
			AsyncCallbackFactory callbackFactory, final ILoggrStateController stateController, final StatusController statusController) {

		this.accountController = accountController;
		this.signUpPanel = signUpPanel;
		signUpPanel.setSubPanelListener(refreshListener);
		this.loginPanel = loginPanel;
		loginPanel.setSubPanelListener(refreshListener);
		this.changePasswordPanel = changePasswordPanel;
		this.forgotPasswordPanel = forgotPasswordPanel;
		this.statusPanel = statusPanel;
		this.newApplicationPanel = newApplicationPanel;
		newApplicationPanel.setSubPanelListener(refreshListener);
		this.applicationManagementPanel = applicationManagementPanel;
		applicationManagementPanel.setSubPanelListener(refreshListener);
		this.accountSummaryPanel = accountSummaryPanel;
		accountSummaryPanel.setSubPanelListener(refreshListener);
		this.stateController = stateController;
		this.statusController = statusController;

		this.applicationController = applicationController;


		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String token = event.getValue();
				stateController.recoverStateFromHistoryToken(token);
				refresh();
			}
		});



		this.initWidget(createLayout());
		//		History.newItem(stateController.createHistoryToken());
		History.fireCurrentHistoryState(); // refresh will occur here


		//		this.refresh();

	}

	private Widget createLayout() {
		// Full screen
		dockPanel.setWidth("100%");
		dockPanel.setHeight("100%");

		// Layout is as follows:
		//                                 Main Menu (North2)
		// (Sub Menu West)
		//
		//                       Operational display (Center)
		//					      Status Panel (South)

		//		topOfPage.setWidth("100%");
		//		topOfPage.setHeight("30px");

		// Space for HTML on top
		Image logo = new Image("iloggr_logo.png");
		logo.setHeight("60px");
		topOfPage.setSize("1000px", "70px");
		topOfPage.add(logo, 5, 7);
		Label whatsUp = new Label("What's up with your App?");
		whatsUp.setWidth("1000px");
		whatsUp.setStyleName("topTitle");
		topOfPage.add(whatsUp, 240, 5);
		dockPanel.add(topOfPage, DockPanel.NORTH);
		dockPanel.setBorderWidth(0);
		dockPanel.setCellWidth(topOfPage, "100%");
		dockPanel.setCellHeight(topOfPage, "10%");


		// Left-side submenu
		VerticalPanel west = new VerticalPanel();
		AbsolutePanel spacer = new AbsolutePanel();
		spacer.setHeight("50px");
		west.add(spacer);
		west.add(subMenuPanel);
		west.setCellWidth(subMenuPanel, "100%");
		dockPanel.add(west, DockPanel.WEST);
		dockPanel.setCellWidth(west, "15%");

		// Top main menu
		dockPanel.add(mainMenu, DockPanel.NORTH);
		dockPanel.setCellHeight(mainMenu, "5%");

		// Center screen operational area
		dockPanel.add(centerPanel, DockPanel.CENTER);
		dockPanel.setCellHorizontalAlignment(centerPanel, HasHorizontalAlignment.ALIGN_LEFT);
		dockPanel.setCellVerticalAlignment(centerPanel, HasVerticalAlignment.ALIGN_TOP);
		dockPanel.setCellWidth(centerPanel, "85%");
		dockPanel.setCellHeight(centerPanel, "85%");

		// Status panel on the bottom
		dockPanel.add(statusPanel, DockPanel.SOUTH); // Enable static positioning for statusMessage
		dockPanel.setCellHeight(statusPanel, "10%");

		return dockPanel;
	}

	private MainMenu createMainMenu() {
		MainMenu menu = new MainMenu();
		menu.setAutoOpen(true);
		menu.setAnimationEnabled(true);

		menu.addItem("Information", new Command() {
			public void execute() {
				stateController.setMainMenuState(MainMenuState.Information);
				refresh();
			}
		});
		menu.addItem("Developer Support", new Command() {
			public void execute() {
				stateController.setMainMenuState(MainMenuState.Support);
				refresh();
			}
		});
		menu.addItem("My Account", new Command() {
			public void execute() {
				stateController.setMainMenuState(MainMenuState.AccountManagement);
				if (stateController.getUserState() != UserState.Authenticated) {
					accountController.authenticateAccount(new VoidActionListener() {
						public void onFailure() {
							accountController.logout();
							refresh();
						}

						public void onSuccess() {
							refresh();
						}


					});
				} else {
					refresh();
				}
			}
		});

		menu.addItem("Sign Up", new Command() {
			public void execute() {
				stateController.setMainMenuState(MainMenuState.SignUp);
				refresh();
			}
		});
		return menu;
	}

	private AbsolutePanel createToDoPanel() {
		AbsolutePanel panel = new AbsolutePanel();
		panel.add(new Label("TBD"));
		return panel;
	}

	public void showCenterPanel(Widget panel) {
		centerPanel.setTitle("");
		centerPanel.setMain(panel);
		statusPanel.clear();
	}

	// TODO: How to access this from other panels for history management?
	public void showCenterPanel(String title, Widget panel) {
		centerPanel.setTitle(title);
		centerPanel.setMain(panel);
		statusPanel.clear();
	}

	/**
	 * Will display the specified submenu
	 *
	 * @param submenu The submenu to show
	 */
	public void showSubMenu(MenuBar submenu) {
		subMenuPanel.setMenu(submenu);
	}

	/*
	public void statusMessage(String string) {
		statusPanel.setMessage(string);

	}

	public void errorMessage(String errorMsg) {
		statusPanel.setErrorMessage(errorMsg);
	}
	 */

	private MainMenu createAccountSubMenu() {
		boolean isLoggedIn =  (stateController.getUserState() == UserState.Authenticated);
		MainMenu bar = new MainMenu(true); // vertical
		// Menu items
		if (isLoggedIn) {
			MenuItem m1 = new MenuItem("Account Summary", true, new Command() {
				public void execute() {
					stateController.setSubMenuState(SubMenuState.AccountSummary);
					refresh();
				}
			});
			bar.addItem(m1);


			new MenuItem("My Applications", true, new Command() {
				public void execute() {
					stateController.setSubMenuState(SubMenuState.ApplicationManagement);
					refresh();
				}
			});
			MenuBar appMenu = new MenuBar(true);
			// add applications
			if (accountController.getCurrentAccount() != null) {
				for (final Application a : accountController.getCurrentAccount().getApplications()) {
					final String appName = a.getName();
					appMenu.addItem(new MenuItem(a.getName(), true, new Command() {
						public void execute() {
							stateController.setSubMenuState(SubMenuState.EditApplication);
							stateController.setData(a.getName());
							refresh();
						}
					}));
				}
			}
			bar.addItem("My Applications", appMenu);

			MenuItem newApp = new MenuItem("New Application", true, new Command() {
				public void execute() {
					stateController.setSubMenuState(SubMenuState.NewApplication);
					refresh();
				}
			});
			bar.addItem(newApp);

			MenuItem m4 = new MenuItem("Change Password", true, new Command() {
				public void execute() {
					stateController.setSubMenuState(SubMenuState.ChangePassword);
					refresh();
				}
			});
			bar.addItem(m4);
		} // if logged in

		MenuItem fp = new MenuItem("Forgot Password", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.ForgotPassword);
				refresh();
			}
		});
		bar.addItem(fp);

		MenuItem support = new MenuItem("Contact Us", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.ContactSupport);
				refresh();
			}
		});
		bar.addItem(support);

		if (isLoggedIn) {
			MenuItem logOut = new MenuItem("Log out", true, new Command() {
				public void execute() {
					stateController.setSubMenuState(SubMenuState.LogOut);
					refresh();
				}
			});
			bar.addItem(logOut);
		}

		return bar;
	}


	private MainMenu createInformationSubMenu() {
		MainMenu bar = new MainMenu(true); // vertical
		// Menu items
		MenuItem m1 = new MenuItem("What is iLoggr?", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.WhatIs);
				refresh();
			}
		});
		bar.addItem(m1);

		MenuItem m2 = new MenuItem("Examples of Use", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.ExamplesOfUse);
				refresh();
			}
		});
		bar.addItem(m2);

		MenuItem m3 = new MenuItem("Pricing", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.Pricing);
				refresh();
			}
		});
		bar.addItem(m3);

		MenuItem m4 = new MenuItem("FAQ", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.FAQ);
				refresh();
			}
		});
		bar.addItem(m4);

		MenuItem m5 = new MenuItem("About Us", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.AboutUs);
				refresh();
			}
		});
		bar.addItem(m5);

		return bar;
	}

	private MainMenu createSupportSubMenu() {
		MainMenu bar = new MainMenu(true); // vertical
		// Menu items
		MenuItem m1 = new MenuItem("Download iPhone Library - Quickstart ", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.DownloadLibrary);
				refresh();
			}
		});
		bar.addItem(m1);

		MenuItem m2 = new MenuItem("API Documentation", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.APIDocumentation);
				refresh();
			}
		});
		bar.addItem(m2);

		MenuItem m3 = new MenuItem("Web service API's", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.WebServices);
				refresh();
			}
		});
		bar.addItem(m3);

		MenuItem m4 = new MenuItem("Contact Us", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.ContactSupport);
				refresh();
			}
		});
		bar.addItem(m4);

		return bar;
	}

	private MainMenu createSignUpSubMenu() {
		MainMenu bar = new MainMenu(true); // vertical

		MenuItem m2= new MenuItem("Create Account", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.SignUp);
				refresh();
			}
		});
		bar.addItem(m2);

		MenuItem m3 = new MenuItem("Account FAQ", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.FAQ);
				refresh();
			}
		});
		bar.addItem(m3);

		MenuItem m4 = new MenuItem("Contact Support", true, new Command() {
			public void execute() {
				stateController.setSubMenuState(SubMenuState.ContactSupport);
				refresh();
			}
		});
		bar.addItem(m4);
		return bar;
	}

	/**
	 * Will ask the controller for state and determine the subpanel and content that should be displayed
	 * according to the current states of the user, main menu, and sub menu.
	 */
	public void refresh() {
		MainMenuState mainMenuState = stateController.getMainMenuState();

		//  Preserve the current menu state on the stack
		History.newItem(stateController.createHistoryToken(), false);

		// Information
		if (mainMenuState == MainMenuState.Information) {
			showInformationSubpanel();
		}

		// Developer Support
		else if (mainMenuState == MainMenuState.Support) {
			showSupportSubpanel();
		}
		//  Sign up
		else if (mainMenuState == MainMenuState.SignUp) {
			showSignupSubpanel();
		}
		//  Account management
		else if (mainMenuState == MainMenuState.AccountManagement) {
			showAccountSubpanel();
		} else {  // if we are in some weird state, default
			stateController.setMainMenuState(MainMenuState.Information);
			stateController.setSubMenuState(SubMenuState.WhatIs);
			showInformationSubpanel();
		}
	}

	public void showInformationSubpanel() {
		SubMenuState subMenuState = stateController.getSubMenuState();
		if (subMenuState == SubMenuState.NoState)  {
			subMenuState = SubMenuState.WhatIs;
			stateController.setSubMenuState(subMenuState);
		}
		showSubMenu(informationSubMenu);
		if (subMenuState == SubMenuState.WhatIs) {
			showHTMLCenterpanel("What is iLoggr?", "ILoggrInformation.html");			
		}  else if (subMenuState == SubMenuState.ExamplesOfUse) {
			showHTMLCenterpanel("Examples of Use", "examples.html");			
		} else if (subMenuState == SubMenuState.FAQ) {
			showHTMLCenterpanel("iLoggr FAQ", "	faq.html");			
		} else if (subMenuState == SubMenuState.Pricing) {
			showHTMLCenterpanel("Pricing (TBD)", "pricingTBD.html");			
		} else if (subMenuState == SubMenuState.AboutUs) {
			showHTMLCenterpanel("About Us", "aboutus.html");
		}

	}

	public void showSupportSubpanel() {
		SubMenuState subMenuState = stateController.getSubMenuState();
		if (subMenuState == SubMenuState.NoState) {
			subMenuState = SubMenuState.DownloadLibrary;
			stateController.setSubMenuState(subMenuState);
		}
		showSubMenu(supportSubMenu);
		if (subMenuState == SubMenuState.DownloadLibrary) {
			showHTMLCenterpanel("Download iPhone Library and Quickstart", "DownloadLibrary.html");			
		} else if (subMenuState == SubMenuState.ContactSupport) {
			showHTMLCenterpanel("Contact Us", "contactus.html");			
		} else if (subMenuState == SubMenuState.WebServices) {
			showHTMLCenterpanel("Web service API's", "webservices.html");			
		} else if (subMenuState == SubMenuState.APIDocumentation) {
			showHTMLCenterpanel("ILoggr Library API Documentation", "APIDocumentation.html");
		}
	}

	public void showSignupSubpanel() {
		UserState userState = stateController.getUserState();
		// if logged in, don't show it
		if (userState == UserState.Authenticated) {
			return;
		}
		SubMenuState subMenuState = stateController.getSubMenuState();
		if (subMenuState == SubMenuState.NoState) {
			subMenuState = SubMenuState.SignUp;
			stateController.setSubMenuState(subMenuState);
		} 
		showSubMenu(signUpSubMenu);
		if (userState == UserState.Authenticated) {
			// why are they doing this, they are logged in? Ignore
		} else if (subMenuState == SubMenuState.SignUp) {
			showCenterPanel("If you have an invitation code, enter it and create an account.  If not, please contact sales@iloggr.com", signUpPanel);
		} else if (subMenuState == SubMenuState.FAQ) {
			showCenterPanel("Account FAQ", toDoPanel);
		} else if (subMenuState == SubMenuState.ContactSupport) {
			showCenterPanel("Contact Support", toDoPanel);
		} else if (subMenuState == SubMenuState.AboutUs) {
			showHTMLCenterpanel("About Us", "aboutus.html");
		}
	}



	public void showAccountSubpanel() {
		UserState userState = stateController.getUserState();
		SubMenuState subMenuState = stateController.getSubMenuState();
		showSubMenu(createAccountSubMenu());
		if (subMenuState == SubMenuState.ForgotPassword) {
			showCenterPanel(forgotPasswordPanel);	
		} else if (subMenuState == SubMenuState.ChangePassword) {
			String securityToken = stateController.getData();
			if ("".equals(securityToken.trim())) {
				showCenterPanel("Change your password", changePasswordPanel);
			} else {
				// pass along any data
				accountController.authenticateAccount(securityToken, 
					new VoidActionListener() {
						public void onSuccess() {
							showCenterPanel("Change your password", changePasswordPanel);
						}

						public void onFailure() {
							statusController.errorMessage("Password reset has failed, please contact support@iloggr.com");
						}
			});	
			}
		} else if (!checkLogin()) {
			// not logged in
			//		statusController.errorMessage("You are not logged it, please log in!");
		} else if (subMenuState == SubMenuState.NoState) {
			subMenuState = SubMenuState.AccountSummary;
			stateController.setSubMenuState(subMenuState);
			refresh();
		} else if (subMenuState == SubMenuState.EditApplication) {
			String appName = stateController.getData();
			Application application = accountController.getAppNamed(appName);
			applicationController.setCurrentApplication(application);
			showCenterPanel(application.getName()+" Application", applicationManagementPanel);
			applicationManagementPanel.refresh();
		} else if (subMenuState == SubMenuState.AccountSummary) {
			showCenterPanel(accountSummaryPanel);
			accountSummaryPanel.refresh();
		} else if (subMenuState == SubMenuState.NewApplication) {
			if (userState == UserState.Authenticated) {
				showCenterPanel("Add an application", newApplicationPanel);
			}
		} else if (subMenuState == SubMenuState.ContactSupport) {
			showHTMLCenterpanel("Contact Us", "contactus.html");			
		} else if (subMenuState == SubMenuState.LogOut) {
			final OKPanel rusure = new OKPanel("Yes, log me out", "No, don't log me out", "Are you sure?");
			ClickHandler okHandler = new ClickHandler() {
				public void onClick(ClickEvent event) {
					rusure.hide();
					accountController.logout();
					refresh();
//					checkLogin();
				}
			};
			ClickHandler cancelHandler = new ClickHandler() {
				public void onClick(ClickEvent event) {
					rusure.hide();
				}
			};
			rusure.setOKHandler(okHandler);
			rusure.setCancelHandler(cancelHandler);
			rusure.center();
		}
	}

	// return true if logged in
	public boolean checkLogin() {
		UserState userState = stateController.getUserState();
		if (userState != UserState.Authenticated) {
			showCenterPanel("Please Log in", loginPanel);
			return false;
		}
		return true;
	}

	private void showHTMLCenterpanel(String title, String filepath) {
		Frame frame = new Frame();
		frame.setUrl(filepath);
		frame.setWidth("1200px");
		frame.setHeight("600px");
		frame.setStyleName("CenterFrame");
		showCenterPanel(title, frame);
	}




}
