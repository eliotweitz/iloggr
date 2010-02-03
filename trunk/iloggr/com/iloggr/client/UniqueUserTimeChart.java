package com.iloggr.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.gchart.client.GChart;
import com.iloggr.client.model.UserReportData;
import com.iloggr.client.services.ReportingService;

public class UniqueUserTimeChart extends GChart {

	int reportingUnit;
	Date start;
	Date stop;


	/**
	 * @param chartSize  Width
	 * @param chartSize2 Height
	 * @param start Start date
	 * @param end End date
	 * @param reportingUnit units (month, week, day, hour)
	 */
	public UniqueUserTimeChart(int width, int height, Date start, Date stop, int reportingUnit) {
		setChartSize(width, height);
		this.reportingUnit = reportingUnit;
		this.start = start;
		this.stop = stop;
		final DateTimeFormat df =  DateTimeFormat.getFormat("MM/dd/yy");

		setPlotAreaBackgroundColor("#CCC");
		setGridColor("#EEE");
		setBackgroundColor("#DDF");
		setBorderColor("black");
		setBorderWidth("1px");
		setBorderStyle("outset");
		setFontFamily("Veranda, Arial, sans-serif");

		setChartTitle("<b>Unique phone report for period: " + df.format(start)+ " - " +df.format(stop) + "</b>");
		setPadding("10px");

		addCurve();	

	     getCurve().getSymbol().setFillSpacing(0);
	     getCurve().getSymbol().setFillThickness(6);
	     getCurve().getSymbol().setBackgroundColor("rgba(0,0,255,1)");
	     getCurve().getSymbol().setBorderWidth(2);
	     getCurve().getSymbol().setBorderColor("rgba(0,0,255,0.5)");
	     getCurve().getSymbol().setWidth(0);
	     getCurve().getSymbol().setHeight(0);
	     getCurve().getSymbol().setBrushSize(40, getYChartSizeDecorated());
	     getCurve().getSymbol().setBrushLocation(AnnotationLocation.NORTH);
	     getCurve().getSymbol().setDistanceMetric(10, 1);
	     // position hover popup in plot area's top left corner
	     getCurve().getSymbol().setHoverAnnotationSymbolType(
	        SymbolType.ANCHOR_NORTHWEST);
	     getCurve().getSymbol().setHoverLocation(
	        AnnotationLocation.SOUTHEAST);
	     getCurve().getSymbol().setHoverXShift(2);
	     getCurve().getSymbol().setHoverYShift(-2);
	// 2px external selection border
	     getCurve().getSymbol().setHoverSelectionHeight(8);
	     getCurve().getSymbol().setHoverSelectionWidth(8);
	     getCurve().getSymbol().setHoverSelectionBorderWidth(-2);
	// same color to create illusion that selected point increases in size
	     getCurve().getSymbol().setHoverSelectionBorderColor("#00F");
	// brighten center to make selected point easier to see
	     getCurve().getSymbol().setHoverSelectionBackgroundColor("aqua");
	 		
		
		
	/*	
		
		getCurve().getSymbol().setSymbolType(SymbolType.BOX_CENTER); 
		getCurve().getSymbol().setWidth(5);
		getCurve().getSymbol().setHeight(5);
		getCurve().getSymbol().setBorderWidth(0);
		getCurve().getSymbol().setBackgroundColor("black");
		getCurve().getSymbol().setFillThickness(6);
		getCurve().getSymbol().setFillSpacing(5);
*/

		getXAxis().setAxisLabel(ReportingService.UNIT_NAMES[reportingUnit]);
		getXAxis().setHasGridlines(false);
		getXAxis().setTicksPerGridline(2);
		getXAxis().setTickLabelFormat("=(Date)"+ReportingService.UNIT_JAVA_DISPLAY_FORMATS[reportingUnit]);
		
		getYAxis().setTickCount(10);
		getYAxis().setHasGridlines(true);


	}
	
	public void addRecords(List<UserReportData> records) {
		long maxVal = 0;
		long minVal = 0;
		int numPoints = 0;
		final DateTimeFormat df =  DateTimeFormat.getFormat(ReportingService.UNIT_JAVA_FORMATS[reportingUnit]);

		for (UserReportData record : records) {
			long data = record.getLongdata();
			if (data < minVal) minVal = data;
			if (data > maxVal) maxVal = data;
			String stringDate = record.getDescription();
			Date date = df.parse(stringDate);
			addPoint(date, data);
			numPoints++;
		}
		getXAxis().setTickCount(numPoints);
		getYAxis().setAxisMax(maxVal);
		getYAxis().setAxisMin(minVal);
		update();
	}

	public void addPoint(Date when, long value) {
		getCurve().addPoint(when.getTime(), value);
	}


	public int getReportingUnit() {
		return reportingUnit;
	}

	public void setReportingUnit(int reportingUnit) {
		this.reportingUnit = reportingUnit;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getStop() {
		return stop;
	}

	public void setStop(Date stop) {
		this.stop = stop;
	}


}
