package com.web4enterprise.pdf.layout.table;

import java.util.ArrayList;
import java.util.List;

public class Table {
	protected List<TableRow> rows = new ArrayList<>();
	
	protected List<Float> columnsWidths = new ArrayList<>();
	
	public Table addRow(TableCell...cells) {
		rows.add(new TableRow(cells));
		return this;
	}
	
	public List<TableRow> getRows() {
		return rows;
	}
	
	public float getColumnWidth(int index) {
		return columnsWidths.get(index);
	}
	
	public void calculateInnerLayout() {
		calculateColumnWidths();
		calculateRowsHeights();
	}
	
	protected void calculateRowsHeights() {
		for(TableRow row : rows) {
			float currentRowHeight = 0;
			for(TableCell cell : row.getCells()) {
				float currentCellHeight = cell.getHeight();
				if(currentCellHeight > currentRowHeight) {
					currentRowHeight = currentCellHeight;
				}
			}
			row.setHeight(currentRowHeight);
		}
	}
	
	protected void calculateColumnWidths() {
		List<Float> currentColumnsWidths = new ArrayList<>();
		for(TableRow row : rows) {
			int columnIndex = 0;
			for(TableCell cell : row.getCells()) {
				float currentWidth = cell.getWidth();
				if(currentColumnsWidths.size() <= columnIndex) {
					currentColumnsWidths.add(currentWidth);
				} else if(currentWidth > currentColumnsWidths.get(columnIndex)) {
					currentColumnsWidths.set(columnIndex, currentWidth);
				}
				columnIndex++;
			}
		}
		for(float columnWidth : currentColumnsWidths) {
			columnsWidths.add(columnWidth);
		}
	}
}
