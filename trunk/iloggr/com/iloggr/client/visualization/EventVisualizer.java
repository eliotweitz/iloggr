package com.iloggr.client.visualization;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.google.inject.Inject;
import com.iloggr.client.model.Event;

public class EventVisualizer extends Composite {

	private final List<Event> events;
	private VerticalPanel panel;

	@Inject
	public EventVisualizer(List<Event> events) {
		this.events = events;
		initWidget(createLayout());
	}

	private Widget createLayout() {
		panel = new VerticalPanel();
		return panel;
	}

	public void loadVisualization() {
		panel.add(layoutVisualization());
	}

	@SuppressWarnings("deprecation")
	private Widget layoutVisualization() {
		Options options = LineChart.Options.create();
		options.setSize(400, 300);
		options.setSmoothLine(true);
		options.setEnableTooltip(true);

		final DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "Day");
		dataTable.addColumn(ColumnType.NUMBER, "Number of Unique Phones");

		Map<Date, Set<Long>> phonesPerDay = new TreeMap<Date, Set<Long>>();
		for (Event event : events) {
			Date recordTime = event.getRecordTime();
			Date day = new Date(recordTime.getYear(), recordTime.getMonth(), recordTime.getDate());
			Set<Long> phones = phonesPerDay.get(day);
			if (phones == null) {
				phones = new HashSet<Long>();
				phonesPerDay.put(day, phones);
			}
			phones.add(event.getPhone().getId());
		}

		dataTable.addRows(phonesPerDay.size());
		int rowIndex = 0;
		for (Map.Entry<Date, Set<Long>> entry : phonesPerDay.entrySet()) {
			Date date = entry.getKey();
			String formattedDate = "" + (date.getMonth() + 1) + "/" + date.getDate() + "/" + (1900 + date.getYear());
			dataTable.setCell(rowIndex, 0, formattedDate, formattedDate, null);
			int uniquePhoneCount = entry.getValue().size();
			dataTable.setCell(rowIndex, 1, uniquePhoneCount, "" + uniquePhoneCount, null);
			rowIndex++;
		}

		final LineChart graph = new LineChart(dataTable, options);
		graph.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				JsArray<Selection> selections = graph.getSelections();
				for (int i = 0; i < selections.length(); i++) {
					Selection selection = selections.get(i);
					if (selection.isCell()) {
						int row = selection.getRow();
						int column = selection.getColumn();
						Object value = getCellValue(dataTable, row, column);
						GWT.log("Value: " + value + " of type: " + value.getClass().getName()
								+ " selected at (" + row + "," + column + ")", null);
					}
				}
			}
		});
		return graph;
	}

	private Object getCellValue(DataTable dataTable, int row, int column) {
		ColumnType columnType = dataTable.getColumnType(column);
		switch(columnType) {
			case BOOLEAN:
				return dataTable.getValueBoolean(row, column);
			case DATE:
			case DATETIME:
				return dataTable.getValueDate(row, column);
			case NUMBER:
				return dataTable.getValueDouble(row, column); // TODO(jsirois): int is an available conversion
			case STRING:
				return dataTable.getValueString(row, column);
			case TIMEOFDAY:
				return dataTable.getValueTimeOfDay(row, column);
			default:
				throw new IllegalArgumentException("Unknown column type: " + columnType);
		}
	}
}
