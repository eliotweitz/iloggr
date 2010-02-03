package com.iloggr.client.panels;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.inject.Inject;
import com.iloggr.client.ActionListener;
import com.iloggr.client.controllers.ApplicationController;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.LocationFix;
import com.iloggr.client.model.Phone;
import com.iloggr.client.visualization.EventVisualizer;
import com.iloggr.gwt.util.client.DateUtility;

public class EventLogPanel extends Composite {

	DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");

	private final VerticalPanel container = new VerticalPanel();
	private final Grid grid = new Grid();
	private final NavBar navbar = new NavBar();
	private final ApplicationController applicationController;
	private final FormPanel CSVForm = new FormPanel("_blank");

	private int startRow = 0;
	private final int pageSize = 30;

	private final DatePicker from = new DatePicker();
	private final DatePicker to = new DatePicker();
	private List<Event> results;
	TextBox maxRows = new TextBox();

	private final Button doSearch = new Button("Show records for date range",  new ClickHandler() {
		public void onClick(ClickEvent event) {
			doSearch.setEnabled(false);
			getRecords();
		}
	});

	private final Button clearSearch = new Button("Clear records",  new ClickHandler() {
		public void onClick(ClickEvent event) {
			clearRecords();
		}
	});


	private final Button getCSV = new Button("Download CSV for date range",  new ClickHandler() {
		public void onClick(ClickEvent event) {
			getRecordsAsCSV();
		}
	});
	@Inject
	public EventLogPanel(ApplicationController applicationController, final EventVisualizer eventVisualizer) {

		this.applicationController = applicationController;

		initWidget(container);
		container.setWidth("800px");

		FlexTable searchTable = new FlexTable();
		FlexCellFormatter cellFormatter = searchTable.getFlexCellFormatter();
		maxRows.setText(Integer.toString(pageSize));
		maxRows.setWidth("50px");
		from.setValue(new Date());
		to.setValue(new Date());
		searchTable.setWidget(1, 0, new Label("From:"));
		cellFormatter.setHorizontalAlignment(1, 0,HasHorizontalAlignment.ALIGN_CENTER);
		searchTable.setWidget(0, 2, from);
		cellFormatter.setRowSpan(0, 2, 3);
		searchTable.setWidget(1, 4, new Label("To:"));
		searchTable.setWidget(0, 6, to);
		cellFormatter.setRowSpan(0, 6, 3);
		searchTable.setWidget(1, 8, new Label("CSV file max rows:"));
		searchTable.setWidget(1, 9, maxRows);
		searchTable.setWidget(1, 10, getCSV);
		VerticalPanel bsp = new VerticalPanel();
		bsp.add(doSearch);
		bsp.add(clearSearch);
		searchTable.setWidget(2, 8, bsp);
		container.add(searchTable);
		container.add(CSVForm);
		grid.setStyleName("table");
		container.add(navbar);
		container.add(grid);
		//  6 columns for event data
		initTable(6, pageSize+1); // add a row for the header labels
		//		setStyleName("DynaTable-DynaTableWidget");
	}




	private class NavBar extends Composite  {

		public final DockPanel bar = new DockPanel();
		public final Button gotoFirst = new Button("&lt;&lt;", new ClickHandler() {
			public void onClick(ClickEvent event) {
				startRow = 0;
				getRecords();
			}
		});

		public final Button gotoNext = new Button("&gt;",  new ClickHandler() {
			public void onClick(ClickEvent event) {
				startRow += pageSize;
				getRecords();
			}
		});

		public final Button gotoPrev = new Button("&lt;",  new ClickHandler() {
			public void onClick(ClickEvent event) {
				startRow -= pageSize;
				if (startRow < 0) {
					startRow = 0;
				}
				getRecords();
			}
		});


		public final HTML status = new HTML();

		public NavBar() {
			initWidget(bar);
			bar.setStyleName("navbar");
			status.setStyleName("status");

			HorizontalPanel buttons = new HorizontalPanel();
			buttons.add(gotoFirst);
			buttons.add(gotoPrev);
			buttons.add(gotoNext);
			bar.add(buttons, DockPanel.EAST);
			bar.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_RIGHT);
			bar.add(status, DockPanel.CENTER);
			bar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			bar.setCellHorizontalAlignment(status, HasHorizontalAlignment.ALIGN_RIGHT);
			bar.setCellVerticalAlignment(status, HasVerticalAlignment.ALIGN_MIDDLE);
			bar.setCellWidth(status, "100%");

			// Initialize prev & first button to disabled.
			//
			gotoPrev.setEnabled(false);
			gotoFirst.setEnabled(false);
			gotoNext.setEnabled(true);
		}
	}

	public void resetContents() {
		clearStatusText();
		
		for (int i=0; i<pageSize+1; i++) {
			grid.setHTML(i, 0, "&nbsp");
			grid.setHTML(i, 1, "&nbsp");
			grid.setHTML(i, 2, "&nbsp");
			grid.setHTML(i, 3, "&nbsp");
			grid.setHTML(i, 4, "&nbsp");
			grid.setHTML(i, 5, "&nbsp");
		}

		initTable(6, pageSize+1); // add a row for the header labels
	}
	



	public void getRecords() {
		Date bodFrom = DateUtility.getUTC(DateUtility.getBOD(from.getValue()));
		Date eodTo = DateUtility.getUTC(DateUtility.getEOD(to.getValue()));
		setStatusText("Fetching records, please wait...");
		applicationController.fetchEventData(bodFrom, eodTo, true,
				pageSize, startRow, new ActionListener<List<Event>>() {
			public void onFailure() {
				// messages sent to user in controller on failure
				doSearch.setEnabled(true);
			}

			public void onSuccess(List<Event> list) {
				doSearch.setEnabled(true);
				results = list;
				refresh();
			}
		});

	}

	public void clearRecords() {
		results = null;
		resetContents();
	}

	public void getRecordsAsCSV() {
		setStatusText("Fetching records, please wait...");
		Date bodFrom = DateUtility.getUTC(DateUtility.getBOD(from.getValue()));
		Date eodTo = DateUtility.getUTC(DateUtility.getEOD(to.getValue()));
		CSVForm.setMethod(FormPanel.METHOD_POST);
		CSVForm.setAction("http://iloggr.com/report");
		CSVForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		Hidden JSONCommand = new Hidden();
		JSONCommand.setName("cmd");  // must have a name or not submitted by form
		String query = buildJSONQuery(applicationController.getAccountController().getCurrentAccount().getSecurityToken(),
				applicationController.getCurrentApplication().getAppID(), bodFrom, eodTo, true, maxRows.getText(), 0);
		JSONCommand.setValue(query);
		CSVForm.setWidget(JSONCommand);
		CSVForm.submit();

		// Below is the GWT way.. Cannot figure out how to get this to trigger a file download
		/*		applicationController.fetchEventDataAsCSV(from.getValue(), to.getValue(), sortOrder.getSelectedIndex()>0,
				Integer.parseInt(maxRows.getText()), new ActionListener<String>() {
			public void onFailure() {
				// messages sent to user in controller on failure
				doSearch.setEnabled(true);
			}

			public void onSuccess(String csv) {
				String encodedCSV = Base64.encode(csv);
				export(encodedCSV);
				doSearch.setEnabled(true);
			}

		});*/


	}

	//  TODO - EW: Probably a nicer way to do this with the GWT JSON capabilities
	private String buildJSONQuery(String securityToken, String appID, Date from, Date to, boolean desc, String maxRows, int limit) {
		StringBuffer query = new StringBuffer();
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyyMMddHHmmss");
		query.append("{\"method\":\"fetchEventDataCSV\", \"parameters\":[{\"__jsonclass__\":\"String\", \"value\":\"");
		query.append(securityToken);
		query.append("\"},{\"__jsonclass__\":\"String\", \"value\":\"");
		query.append(appID);
		query.append("\"},{\"__jsonclass__\":\"Date\", \"value\":\"");
		query.append(dateFormat.format(from));
		query.append("\"},{\"__jsonclass__\":\"Date\", \"value\":\"");
		query.append(dateFormat.format(to));
		query.append("\"},{\"__jsonclass__\":\"Boolean\", \"value\":");
		query.append((desc)?"true":"false"); // not quoted
		query.append("},{\"__jsonclass__\":\"Integer\", \"value\":");
		query.append(maxRows); // not quoted
		query.append("},{\"__jsonclass__\":\"Integer\", \"value\":");
		query.append(limit); // not quoted
		query.append("}]}");
		return query.toString();
	}



	// srcRow - where we are in the dataset
	public void updateRowData(int startRow) {
		int srcRowIndex = 0;
		if (results == null) {
			resetContents();
			return;
		}
		int srcRowCount = results.size(); // <= pagesize
		boolean isLastPage = false;
		if (srcRowCount == 0) {
			if (startRow > 0) {
				setStatusText(startRow-pageSize + " - " + startRow + " END");
			} else {
				setStatusText("No records found");
			}
			isLastPage = true;
		} else {
			int destRowIndex = 1; // skip navbar row
			for (; srcRowIndex < srcRowCount && destRowIndex < pageSize+1; ++srcRowIndex, ++destRowIndex) {
				Event event = results.get(srcRowIndex);
				if (event != null) {
					Phone phone = event.getPhone();
					double lat = event.getLatitude();
					double lon = event.getLongitude();
					String description = event.getDescription();
					Date recordTime = event.getRecordTime();
					// Date, client id, description, data, lat, lon
					grid.setText(destRowIndex, 0, (recordTime != null)?dateFormat.format(recordTime):"unknown");
					grid.setText(destRowIndex, 1, (phone != null)?phone.getClientID():"unknown");
					grid.setText(destRowIndex, 2, (description != null)?description:"");
					grid.setText(destRowIndex, 3, Double.toString(event.getData()));
					grid.setText(destRowIndex, 4, (lat!=Event.NOLAT)?Double.toString(lat):"unknown");
					grid.setText(destRowIndex, 5, (lon!=Event.NOLON)?Double.toString(lon):"unknown");

				} else {
					grid.setText(destRowIndex, 0, "No Data");
					grid.setText(destRowIndex, 1, "No Data");
					grid.setText(destRowIndex, 2, "No Data");
					grid.setText(destRowIndex, 3, "No Data");
					grid.setText(destRowIndex, 4, "No Data");
					grid.setText(destRowIndex, 5, "No Data");
				}
			}

			// Clear remaining table rows.
			//
			isLastPage = false;
			for (; destRowIndex < pageSize+1; ++destRowIndex) {
				isLastPage = true;
				grid.clearCell(destRowIndex, 0);
				grid.clearCell(destRowIndex, 1);
				grid.clearCell(destRowIndex, 2);
				grid.clearCell(destRowIndex, 3);
				grid.clearCell(destRowIndex, 4);
				grid.clearCell(destRowIndex, 5);
			}
			// Update the status message.
			//
			setStatusText(startRow + " - " + (startRow+pageSize));
		}

		// Synchronize the nav buttons.
		navbar.gotoNext.setEnabled(!isLastPage);
		navbar.gotoFirst.setEnabled(startRow > 0);
		navbar.gotoPrev.setEnabled(startRow > 0);
	}


	public void clearStatusText() {
		navbar.status.setHTML("&nbsp;");
	}

	public void refresh() {
		// Disable buttons temporarily to stop the user from running off the end.
		//
		navbar.gotoFirst.setEnabled(false);
		navbar.gotoPrev.setEnabled(false);
		navbar.gotoNext.setEnabled(false);
		updateRowData(startRow);
	}

	/*	public void setRowCount(int rows) {
		grid.resizeRows(rows);
	}*/

	public void setStatusText(String text) {
		navbar.status.setText(text);
	}


	private void initTable(int colCount, int rowCount) {
		grid.resize(rowCount, colCount);
		// Set up the header row. It's one greater than the number of visible rows.
		Label col1 = new Label("Date/Time");
		col1.setStyleName("column-header");
		Label col2 = new Label("Client ID");
		col2.setStyleName("column-header");
		Label col3 = new Label("Description");
		col3.setStyleName("column-header");
		Label col4 = new Label("Data");
		col4.setStyleName("column-header");
		Label col5 = new Label("Lat");
		col5.setStyleName("column-header");
		Label col6 = new Label("Lon");
		col6.setStyleName("column-header");
		grid.setWidget(0, 0, col1);
		CellFormatter cf = grid.getCellFormatter();
		cf.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0, 1, col2 );
		cf.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0, 2, col3);
		cf.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0, 3, col4);
		cf.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0, 4, col5);
		cf.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0, 5, col6);
		cf.setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_CENTER);
		// must sum to 800?
		col1.setWidth("100px");
		col2.setWidth("150px");
		col3.setWidth("400px");
		col4.setWidth("50px");
		col5.setWidth("50px");
		col6.setWidth("50px");
		grid.setBorderWidth(1);
		grid.setCellPadding(2);
		grid.setCellSpacing(5);
	}

	/*  Another way to call JSON services.. Cannot figure out how to use output to trigger a file download
	private void callReportJSON(String query) {

		String url = "http://localhost:8080/report";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
		builder.setHeader("content-type", "text/x-iloggr-rpc");
		try {
			@SuppressWarnings("unused")
			Request request = builder.sendRequest(query, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// Couldn't connect to server (could be timeout, SOP violation, etc.)
					String xxx = request.toString();
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						@SuppressWarnings("unused")
						String stuff = response.getText();
						// Process the response in response.getText()
					} else {
						// Handle the error.  Can get the status text from response.getStatusText()
					}
				}

			});
		} catch (RequestException e) {
			// Couldn't connect to server
		}
	} */
}


