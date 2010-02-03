/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.panels;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.iloggr.client.ActionListener;
import com.iloggr.client.ApplicationManagementMessages;
import com.iloggr.client.ParameterTypeListBox;
import com.iloggr.client.VoidActionListener;
import com.iloggr.client.controllers.AccountController;
import com.iloggr.client.controllers.ApplicationController;
import com.iloggr.client.controllers.ILoggrStateController;
import com.iloggr.client.controllers.StatusController;
import com.iloggr.client.model.Application;
import com.iloggr.client.model.Counter;
import com.iloggr.client.model.InformationField;
import com.iloggr.client.model.Provisioning;
import com.iloggr.client.model.ProvisioningParameter;
import com.iloggr.gwt.util.client.FormatUtility;
import com.iloggr.gwt.util.lang.StringUtility;


public class ApplicationManagementPanel extends ListenerPanel {
	private final AccountController accountController;
	private final ApplicationController applicationController;
	private final StatusController statusController;
	private final ApplicationManagementMessages messages;
	private final EventLogPanel eventLogPanel;
	private final ReportingPanel reportingPanel;
	private final TabPanel container = new TabPanel();
	private final FormPanel XMLForm = new FormPanel("_blank");
	private final ILoggrStateController stateController;

	@Inject
	public ApplicationManagementPanel(ApplicationManagementMessages messages,
			ApplicationController applicationController, StatusController statusController,
			AccountController accountController, EventLogPanel eventLogPanel, ReportingPanel reportingPanel,
			ILoggrStateController stateController) {
		this.accountController = accountController;
		this.applicationController = applicationController;
		this.statusController = statusController;
		this.stateController = stateController;
		this.messages = messages;
		this.eventLogPanel = eventLogPanel;
		this.reportingPanel = reportingPanel;
		initWidget(container);
	}

	public void refresh() {
		container.clear();
		container.add(createInfoTab(), "Info");
		container.add(createProvisioningTab(), "Provisioning");
		container.add(createCounterTab(), "Counters");
		container.add(createReportTab(), "Reports");
		container.add(createLoggingTab(), "Logging");
		container.selectTab(0);
	}

	private FlexTable createInfoTab() {
		final Application application = applicationController.getCurrentApplication();
		FlexTable table = new FlexTable();
		table.setSize("100%", "100%");

		final Label idLabel = new Label(messages.appIDLabel());
		table.setWidget(0, 0, idLabel);

		Label aidLabel = new Label(application.getAppID());
		table.setWidget(0,3,aidLabel);

		Button downloadFile = new Button("Download ID File", new ClickHandler() {
			public void onClick(ClickEvent event) {
				getXMLFile(applicationController.getCurrentApplication().getAppID());
			}});
		table.setWidget(0, 4, downloadFile);
		// Invisible form for file download
		table.setWidget(0, 5, XMLForm);

		final Label eLabel = new Label(messages.releaseDateLabel());
		table.setWidget(1, 0, eLabel);
		eLabel.setWidth("81px");

		final DatePicker releaseDate = new DatePicker();
		releaseDate.setValue(application.getReleaseDate());
		table.setWidget(1, 3, releaseDate);
		releaseDate.setValue(application.getReleaseDate(), true);

		final String appName = application.getName();
		table.setWidget(2, 0, new Label("Application Name: "));
		final TextBox anTextBox = new TextBox();
		anTextBox.setText(appName);
		table.setWidget(2, 3, anTextBox);

		table.setWidget(3, 0, new Label("MOTD Announcement"));
		final TextArea announcement = new TextArea();
		announcement.setText(application.getAnnouncement());
		announcement.setVisibleLines(4);
		table.setWidget(3, 3, announcement);

		Button updateApp = new Button("Save Changes", new ClickHandler() {
			public void onClick(ClickEvent event) {
				accountController.updateApplication(anTextBox.getText(), releaseDate.getValue(), announcement.getText(),
						application.getAppID(), new ActionListener<Application>() {
					public void onSuccess(Application app) {
						stateController.setData(app.getName());
						//								applicationController.setCurrentApplication(app);
						statusController.statusMessage("Application: " + appName + " updated.");
					}

					public void onFailure() {
					}
				});
			}});
		table.setWidget(4, 0, updateApp);


		Button deleteApp = new Button("Delete Application", new ClickHandler() {
			public void onClick(ClickEvent event) {
				final OKPanel rusure = new OKPanel("Yes, delete this application", "No, don't delete it", "Are you sure, any deployed iPhone applications will need to be rebuilt?");
				ClickHandler okHandler = new ClickHandler() {
					public void onClick(ClickEvent event) {
						rusure.hide();
						accountController.deleteApplication(application, new VoidActionListener() {
							@Override
							public void onSuccess() {
								statusController.statusMessage("Application: " + appName + " deleted.");
								stateController.setData("");
								//						applicationController.setCurrentApplication(null);
								closePanel();
							}

							public void onFailure() {
							}
						});
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

				}});

		table.setWidget(4, 3, deleteApp);

		return table;
	}

	private VerticalPanel createProvisioningTab() {
		final Application application = applicationController.getCurrentApplication();
		final VerticalPanel tab = new VerticalPanel();
		// Add meta information about provisioning
		Provisioning prov = application.getProvisioning();
		String version = prov.getVersion();
		Date lastUpdate = prov.getLastUpdate();
		FlexTable table = new FlexTable();
		table.getFlexCellFormatter();
		table.addStyleName("cw-FlexTable");
		table.setWidth("20em");
		table.setCellSpacing(5);
		table.setCellPadding(3);
		//		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		//cellFormatter.setColSpan(0, 0, 2);
		table.setWidget(0, 0, new Label("Version: "));
		table.setWidget(0, 1, new InformationField((version != null)?version:"No Version"));
		table.setWidget(1, 0, new Label("Last Update: "));
		table.setWidget(1, 1, new InformationField(FormatUtility.formatDate(lastUpdate)));
		tab.add(table);

		// get specific provisioning parameters
		tab.add(new Label(""));
		final FlexTable paramTable = new FlexTable();
		paramTable.getFlexCellFormatter();
		paramTable.addStyleName("cw-FlexTable");
		paramTable.setWidth("80em");
		paramTable.setCellSpacing(5);
		paramTable.setCellPadding(3);
		tab.add(paramTable);
		refreshParamTable(paramTable);
		return tab;

	}

	private void refreshParamTable(final FlexTable paramTable) {
		paramTable.clear();
		Label l1 = new Label("Parameter Name");
		Label l2 = new Label("Parameter Value");
		Label l3 = new Label("Parameter Type");
		Label l4 = new Label("Parameter Active");
		l1.setStyleName("column-header");
		l2.setStyleName("column-header");
		l3.setStyleName("column-header");
		l4.setStyleName("column-header");
		paramTable.setWidget(0, 0, l1);
		paramTable.setWidget(0, 3, l2);
		paramTable.setWidget(0, 5, l3);
		paramTable.setWidget(0, 7, l4);
		applicationController.getProvisioningParameters(new ActionListener<List<ProvisioningParameter>>() {
			public void onSuccess(List<ProvisioningParameter> params) {
				// show provisioning parameters
				int row = 1;
				if (params.size() == 0) {
					paramTable.setWidget(row,0,new Label("No Parameters"));
				}
				for (final ProvisioningParameter pp : params) {
					paramTable.setWidget(row, 0, new Label(pp.getName()));
					final TextBox vt = new TextBox();
					vt.setStyleName("iloggr-info-field");
					vt.setText(pp.getValue());
					paramTable.setWidget(row, 3, vt);
					final Label vtt = new Label();
					vtt.setStyleName("iloggr-info-field");
					vtt.setText(pp.getType());
					paramTable.setWidget(row, 5, vtt);
					final CheckBox active = new CheckBox();
					active.setValue(pp.isActive());
					paramTable.setWidget(row, 7, active);
					// Parameter update button
					Button updateButton = new Button("Update", new ClickHandler() {
						public void onClick(ClickEvent event) {
							applicationController.updateProvisioningParameter(pp, vt.getText(), vtt.getText(),
									active.getValue(), new VoidActionListener() {
								@Override
								public void onSuccess() {
									refreshParamTable(paramTable);
									statusController.statusMessage("Parameter: " + pp.getName() + " updated.");
								}

								public void onFailure() {
									// TODO Auto-generated method stub
								}

							});
						}});

					paramTable.setWidget(row, 9, updateButton);
					// Parameter Delete
					paramTable.setWidget(row, 11, new Button("Delete", new ClickHandler() {
						public void onClick(ClickEvent event) {
							final OKPanel rusure = new OKPanel("Yes, delete this parameter", "No, don't delete it", "Are you sure?");
							ClickHandler okHandler = new ClickHandler() {
								public void onClick(ClickEvent event) {
									rusure.hide();
									applicationController.deleteProvisioningParameter(pp, new VoidActionListener() {
										@Override
										public void onSuccess() {
											refreshParamTable(paramTable);
											statusController.statusMessage("Parameter: " + pp.getName() + " deleted.");
										}
										public void onFailure() {
										}
									});
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
					}));

					row++;
				}
				//  Put in the rows for adding parameters
				int startAddRow = paramTable.getRowCount();
				final TextBox newName = new TextBox();
				newName.setStyleName("iloggr-info-field");
				final TextBox newValue = new TextBox();
				newValue.setStyleName("iloggr-info-field");
				final ParameterTypeListBox newType = new ParameterTypeListBox();
				final CheckBox newActive = new CheckBox();
				newActive.setValue(true);
				Label np = new Label("Add parameter:");
				paramTable.setWidget(startAddRow, 0, np);
				paramTable.setWidget(startAddRow+1, 0, newName);
				paramTable.setWidget(startAddRow+1, 3, newValue);
				paramTable.setWidget(startAddRow+1, 5, newType);
				paramTable.setWidget(startAddRow+1, 7, newActive);
				Button addParameterButton = new Button("Add", new ClickHandler() {
					public void onClick(ClickEvent event) {
						String name = newName.getText();
						if (StringUtility.isBlank(name)) {
							statusController.errorMessage("Please give the new provisioning parameter a name.");
							return;
						}
						applicationController.addProvisioningParameter(name, newValue.getText(),
								newType.getItemText(newType.getSelectedIndex()), newActive.getValue(), new VoidActionListener() {
							@Override
							public void onSuccess() {
								refreshParamTable(paramTable);
								statusController.statusMessage("Parameter: " + newName.getText() + " added.");
							}

							public void onFailure() {
								// TODO Auto-generated method stub
							}
						});
					}});

				addParameterButton.addStyleName("sc-FixedWidthButton");
				paramTable.setWidget(startAddRow+1, 9, addParameterButton);
		}

			public void onFailure() {
			}
		});


	}

	private VerticalPanel createCounterTab() {
		//		final Application application = applicationController.getCurrentApplication();
		final VerticalPanel tab = new VerticalPanel();

		// get specific counters 
		tab.add(new Label(""));
		final FlexTable counterTable = new FlexTable();
		counterTable.getFlexCellFormatter();
		counterTable.addStyleName("cw-FlexTable");
		counterTable.setWidth("40em");
		counterTable.setCellSpacing(5);
		counterTable.setCellPadding(3);
		tab.add(counterTable);
		refreshCounterTable(counterTable);
		return tab;

	}

	private void refreshCounterTable(final FlexTable counterTable) {
		counterTable.clear();
		Label l1 = new Label("Counter Name");
		Label l2 = new Label("Counter Value");
		l1.setStyleName("column-header");
		l2.setStyleName("column-header");
		counterTable.setWidget(0, 0, l1);
		counterTable.setWidget(0, 3, l2);
		applicationController.getApplicationCounters(new ActionListener<List<Counter>>() {
			public void onSuccess(List<Counter> counters) {
				// show provisioning parameters
				int row = 1;
				if (counters.size() == 0) {
					counterTable.setWidget(row,0,new Label("No Counters"));
				}
				for (final Counter c : counters) {
					counterTable.setWidget(row, 0, new Label(c.getName()));
					final Label vt = new Label();
					vt.setStyleName("iloggr-info-field");
					vt.setText(Long.toString(c.getCount()));
					counterTable.setWidget(row, 3, vt);
					// reset counter button
					Button resetButton = new Button("Reset counter", new ClickHandler() {
						public void onClick(ClickEvent event) {
							applicationController.resetApplicationCounter(c.getName(), new VoidActionListener() {
								@Override
								public void onSuccess() {
									refreshCounterTable(counterTable);
									statusController.statusMessage("Counter: " + c.getName() + " reset to 0");
								}

								public void onFailure() {
									// TODO Auto-generated method stub
								}

							});
						}});

					counterTable.setWidget(row, 5, resetButton);
					// Counter Delete
					counterTable.setWidget(row, 7, new Button("Delete", new ClickHandler() {
						public void onClick(ClickEvent event) {
							final OKPanel rusure = new OKPanel("Yes, delete this counter", "No, don't delete it", "Are you sure?");
							ClickHandler okHandler = new ClickHandler() {
								public void onClick(ClickEvent event) {
									rusure.hide();
									applicationController.deleteApplicationCounter(c.getName(), new VoidActionListener() {
										@Override
										public void onSuccess() {
											refreshCounterTable(counterTable);
											statusController.statusMessage("Counter: " + c.getName() + " deleted.");
										}
										public void onFailure() {
										}
									});
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
					}));
					row++;
				}
				int startAddRow = counterTable.getRowCount();
				// Table with new counters to add
				final TextBox newName = new TextBox();
				newName.setStyleName("iloggr-info-field");
				final TextBox newValue = new TextBox();
				newValue.setStyleName("iloggr-info-field");
				Label nc = new Label("Add Counter:");
				counterTable.setWidget(startAddRow, 0, nc);
				counterTable.setWidget(startAddRow+1, 0, newName);
				Button addCounterButton = new Button("Add", new ClickHandler() {
					public void onClick(ClickEvent event) {
						String name = newName.getText();
						if (StringUtility.isBlank(name)) {
							statusController.errorMessage("Please give the new counter a name.");
							return;
						}
						applicationController.addApplicationCounter(name,  new VoidActionListener() {
							@Override
							public void onSuccess() {
								refreshCounterTable(counterTable);
								statusController.statusMessage("Counter: " + newName.getText() + " added.");
							}

							public void onFailure() {
								// TODO Auto-generated method stub
							}
						});
					}});

				addCounterButton.addStyleName("sc-FixedWidthButton");
				counterTable.setWidget(startAddRow+1, 7, addCounterButton);
			}

			public void onFailure() {
			}
		});

	}


	private ReportingPanel createReportTab() {
		reportingPanel.resetContents();
		return reportingPanel;

	}

	private EventLogPanel createLoggingTab() {
		eventLogPanel.resetContents();
		return eventLogPanel;
	}

	public void getXMLFile(String appID) {
		XMLForm.setMethod(FormPanel.METHOD_POST);
		XMLForm.setAction("http://iloggr.com/account");
		XMLForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		Hidden JSONCommand = new Hidden();
		JSONCommand.setName("cmd");  // must have a name or not submitted by form
		String query = buildJSONQuery(accountController.getCurrentAccount().getSecurityToken(),appID);
		JSONCommand.setValue(query);
		XMLForm.setWidget(JSONCommand);
		XMLForm.submit();
	}

	//  TODO - EW: Probably a nicer way to do this with the GWT JSON capabilities
	private String buildJSONQuery(String securityToken, String appID) {
		StringBuffer query = new StringBuffer();
		query.append("{\"method\":\"getApplicationIDFileXML\", \"parameters\":[{\"__jsonclass__\":\"String\", \"value\":\"");
		query.append(securityToken);
		query.append("\"},{\"__jsonclass__\":\"String\", \"value\":\"");
		query.append(appID);
		query.append("\"}]}");
		return query.toString();
	}




}
