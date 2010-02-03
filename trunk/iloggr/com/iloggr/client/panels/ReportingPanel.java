package com.iloggr.client.panels;


import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
//import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.inject.Inject;
import com.iloggr.client.ActionListener;
import com.iloggr.client.UniqueUserTimeChart;
import com.iloggr.client.controllers.ApplicationController;
import com.iloggr.client.model.Event;
import com.iloggr.client.model.UserReportData;
import com.iloggr.client.services.ReportingService;
import com.iloggr.gwt.util.client.DateUtility;

public class ReportingPanel extends Composite {

	final VerticalPanel container = new VerticalPanel();
	final ApplicationController applicationController;
	StatusPanel statusPanel;
//	AnnotatedTimeLine chart;
	GeoMap.Options mapOptions = GeoMap.Options.create();
	GeoMap map;
	private DatePicker from = new DatePicker();
	private DatePicker to = new DatePicker();
	VerticalPanel reportPanel = new VerticalPanel();
	Table summaryReport;
	final ListBox summaryUnits = new ListBox();
	final int[] unitMap;
	UniqueUserTimeChart chart;
	
//	AnnotatedTimeLine ;


	@Inject
	public ReportingPanel(ApplicationController applicationController, StatusPanel statusPanel) {
		this.applicationController = applicationController;
		this.statusPanel = statusPanel;
		initWidget(container);
		container.setWidth("800px");

		// Units for summary table
		unitMap = new int[4];
		summaryUnits.addItem(ReportingService.UNIT_NAMES[ReportingService.MONTH_REPORT_UNIT]);
		unitMap[0] = ReportingService.MONTH_REPORT_UNIT;
		summaryUnits.addItem(ReportingService.UNIT_NAMES[ReportingService.WEEK_REPORT_UNIT]);
		unitMap[1] = ReportingService.WEEK_REPORT_UNIT;
		summaryUnits.addItem(ReportingService.UNIT_NAMES[ReportingService.DAY_REPORT_UNIT]);
		unitMap[2] = ReportingService.DAY_REPORT_UNIT;
		summaryUnits.addItem(ReportingService.UNIT_NAMES[ReportingService.HOUR_REPORT_UNIT]);
		unitMap[3] = ReportingService.HOUR_REPORT_UNIT;
		summaryUnits.setSelectedIndex(2);
		HorizontalPanel unitsPanel = new HorizontalPanel();
		unitsPanel.add(new Label("Time unit for report (eg. # unique phones per day): "));
		unitsPanel.add(summaryUnits);
		container.add(unitsPanel);

		FlexTable searchTable = new FlexTable(); 
		FlexCellFormatter cellFormatter = searchTable.getFlexCellFormatter();
		from.setValue(new Date());
		to.setValue(new Date());
		searchTable.setWidget(1, 0, new Label("From:"));
		cellFormatter.setHorizontalAlignment(1, 0,HasHorizontalAlignment.ALIGN_CENTER);
		searchTable.setWidget(0, 2, from);
		cellFormatter.setRowSpan(0, 2, 3);
		searchTable.setWidget(1, 4, new Label("To:"));
		searchTable.setWidget(0, 6, to);
		cellFormatter.setRowSpan(0, 6, 3);
		VerticalPanel reportButtonPanel = new VerticalPanel();
		searchTable.setWidget(0, 7, reportButtonPanel);
		container.add(searchTable);
		reportPanel = new VerticalPanel();
		container.add(reportPanel);
		
		//  Add the report buttons
		Button uniquePhone = new Button("Run User Usage Reports",  new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget w = getUniquePhoneReport();
				reportPanel.clear();
				reportPanel.add(w);
				updateUniqueUserReport();
				updateSummaryReport();
				}
		});
		
		Button userRegionReport = new Button("Run Region Usage Report",  new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget w = getUserRegionReport();
				reportPanel.clear();
				reportPanel.add(w);
				updateUserRegionReport();
				updateSummaryReport();
				}
		});

		reportButtonPanel.add(uniquePhone);
		reportButtonPanel.add(userRegionReport);
		/*
		Button repeatReport = new Button("Repeat User Report",  new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		reportButtonPanel.add(repeatReport);*/


		// Visualization component initialization callbacks
	    Runnable onTableLoadCallback = new Runnable() {
	      public void run() {
	    		summaryReport = new Table();
	    		// Summary table
	    		container.add(summaryReport);
	      }
	    };
	    
	    Runnable dontCareLoadCallback = new Runnable() {
		      public void run() {
		      }
		    };
		    
	    

	    // Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
//	    VisualizationUtils.loadVisualizationApi(dontCareLoadCallback, AnnotatedTimeLine.PACKAGE);
	    VisualizationUtils.loadVisualizationApi(dontCareLoadCallback, GeoMap.PACKAGE);
	    VisualizationUtils.loadVisualizationApi(onTableLoadCallback, Table.PACKAGE);
	}
	
	public void resetContents() {
		statusPanel.clear();
		reportPanel.clear();
		DataTable data = DataTable.create();
		summaryReport.draw(data);
	}
	
	private Widget getUniquePhoneReport() {
		// Unique phone report
//		AnnotatedTimeLine.Options options = AnnotatedTimeLine.Options.create();
/*		chart = new AnnotatedTimeLine("800px", "300px");
		chart.setTitle("Unique User Report");
		return chart;*/
		chart = new UniqueUserTimeChart(1200, 300, from.getValue(), to.getValue(), unitMap[summaryUnits.getSelectedIndex()]);
		return chart;
	}
	
	private Widget getUserRegionReport() {
		// Unique phone map report
		mapOptions.setDataMode(GeoMap.DataMode.MARKERS);
		mapOptions.setRegion("world");
		map = new GeoMap();
		map.setTitle("User Region Report");
		return map;
 	}

	
	private void updateSummaryReport() {
		final DataTable data = DataTable.create();
		Date bodFrom = DateUtility.getUTC(DateUtility.getBOD(from.getValue()));
		Date eodTo = DateUtility.getUTC(DateUtility.getEOD(to.getValue()));
		
		applicationController.fetchUniqueUserStats(bodFrom, eodTo, unitMap[summaryUnits.getSelectedIndex()], new ActionListener<List<UserReportData>>() {

			public void onFailure() {
				// TODO Auto-generated method stub
			}

			public void onSuccess(List<UserReportData> records) {
				for (UserReportData record : records) {
					data.addColumn(ColumnType.NUMBER, record.getDescription());
				}
				
				int count = 0;
				data.addRow(); // single row table
				for (UserReportData record : records) {
					data.setValue(0, count++, (record.getType()==UserReportData.LONG)?record.getLongdata():record.getFloatdata());
				}
				summaryReport.draw(data);
			}
		});

	}

	private void updateUniqueUserReport() {
		final DataTable data = DataTable.create();
		Date bodFrom = DateUtility.getUTC(DateUtility.getBOD(from.getValue()));
		Date eodTo = DateUtility.getUTC(DateUtility.getEOD(to.getValue()));
		data.addColumn(ColumnType.DATE, "Date");
		data.addColumn(ColumnType.NUMBER, "Unique Visitors");
		final int units = unitMap[summaryUnits.getSelectedIndex()];
		final DateTimeFormat df =  DateTimeFormat.getFormat(ReportingService.UNIT_JAVA_FORMATS[units]);
		applicationController.fetchUniqueUserReportData(bodFrom, eodTo, units, new ActionListener<List<UserReportData>>() {

			public void onFailure() {
			}

			public void onSuccess(List<UserReportData> records) {
				if (records.size() == 0) {
					statusPanel.setMessage("No data for this application");
				}
				chart.addRecords(records);
	/*
				for (UserReportData record : records) {
					int row = data.addRow();
					String stringDate = record.getDescription();
					Date date = df.parse(stringDate);
	/*				data.setValue(row, 0, date);
					data.setValue(row, 1, record.getLongdata());*/
	//				chart.addPoint(date, record.getLongdata());
				}
/*				chart.draw(data);*/
	//			chart.update();
				
		});
	}
	
	private void updateUserRegionReport() {
		final DataTable data = DataTable.create();
		Date bodFrom = DateUtility.getUTC(DateUtility.getBOD(from.getValue()));
		Date eodTo = DateUtility.getUTC(DateUtility.getEOD(to.getValue()));
		data.addColumn(ColumnType.NUMBER, "LATITUDE", "Latitude");
		data.addColumn(ColumnType.NUMBER, "LONGITUDE", "Longitude");
		data.addColumn(ColumnType.NUMBER, "VALUE", "Value");
//		data.addColumn(ColumnType.STRING, "HOVER", "HoverText");
		applicationController.fetchUserRegionReportData(bodFrom, eodTo, new ActionListener<List<Event>>() {

			public void onFailure() {
			}

			public void onSuccess(List<Event> events) {
				if (events.size() == 0) {
					statusPanel.setMessage("No data for this application");
				}
				for (Event event : events) {
					double latitude = event.getLatitude();
					double longitude = event.getLongitude();
					if (latitude != Event.NOLAT && longitude != Event.NOLON) {
						int row = data.addRow();
						data.setValue(row, 0, latitude);
						data.setValue(row, 1, longitude);
						data.setValue(row, 2, 1); // TODO - set each at a weight of one.. will map show properly?
					}
				}
				map.draw(data, mapOptions);
			}
		});
	}





}
