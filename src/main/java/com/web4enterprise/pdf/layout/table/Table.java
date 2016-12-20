package com.web4enterprise.pdf.layout.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
	protected List<TableRow> rows = new ArrayList<>();

	protected Map<Integer, Float> fixedColumnsWidths = new HashMap<>();
	protected List<Float> columnsWidths = new ArrayList<>();
	protected boolean repeatHeaderOnNewPage = true;
	protected int nbHeaderRows = 1;
	
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
		calculateCellsWidths();
		calculateCellsHeights();
	}
	
	public void setColumnWidth(int columnIndex, float columnWidth) {
		fixedColumnsWidths.put(columnIndex, columnWidth);
	}
	
	public boolean isRepeatHeaderOnNewPage() {
		return repeatHeaderOnNewPage;
	}

	public void setRepeatHeaderOnNewPage(boolean repeatHeaderOnNewPage) {
		this.repeatHeaderOnNewPage = repeatHeaderOnNewPage;
	}

	public int getNbHeaderRows() {
		return nbHeaderRows;
	}

	public void setNbHeaderRows(int nbHeaderRows) {
		this.nbHeaderRows = nbHeaderRows;
	}

	protected void calculateRowsHeights() {
		for(TableRow row : rows) {
			int columnIndex = 0;
			float currentRowHeight = 0;
			for(TableCell cell : row.getCells()) {
				float currentCellHeight = cell.getHeight(columnsWidths.get(columnIndex));
				if(currentCellHeight > currentRowHeight) {
					currentRowHeight = currentCellHeight;
				}
				columnIndex++;
			}
			row.setHeight(currentRowHeight);
		}
	}
	
	protected void calculateColumnWidths() {
		List<Float> currentColumnsWidths = new ArrayList<>();
		for(TableRow row : rows) {
			int columnIndex = 0;
			for(TableCell cell : row.getCells()) {
				Float fixedWidth = fixedColumnsWidths.get(columnIndex);
				if(fixedWidth != null) {
					currentColumnsWidths.add(fixedWidth);
				} else {
					float currentWidth = cell.getWidth();
					if(currentColumnsWidths.size() <= columnIndex) {
						currentColumnsWidths.add(currentWidth);
					} else if(currentWidth > currentColumnsWidths.get(columnIndex)) {
						currentColumnsWidths.set(columnIndex, currentWidth);
					}
				}
				columnIndex++;
			}
		}
		for(float columnWidth : currentColumnsWidths) {
			columnsWidths.add(columnWidth);
		}
	}
	
	protected void calculateCellsWidths() {
		for(TableRow row : rows) {
			int columnIndex = 0;
			int nbMergedCells = 0;
			for(TableCell cell : row.getCells()) {	
				if(nbMergedCells > 0) {
					cell.setMerged(true);
					nbMergedCells--;
				} else {
					//Get width for merged cells if any.
					for(int i = 0; i <= cell.getMergedColumns(); i++) {
						cell.setComputedWidth(cell.getComputedWidth() + getColumnWidth(columnIndex + i));
					}
				}
				columnIndex++;
			}
		}
	}
	
	protected void calculateCellsHeights() {
		int rowIndex = 0;
		for(TableRow row : rows) {
			int columnIndex = 0;
			for(TableCell cell : row.getCells()) {	
				if(!cell.isMerged()) {
					cell.setComputedHeight(row.getHeight());
					
					//Get height for merged cells if any.
					for(int i = 1; i <= cell.getMergedRows(); i++) {
						TableRow mergedRow = rows.get(rowIndex + i);
						if(mergedRow != null) {
							cell.setComputedHeight(cell.getComputedHeight() + mergedRow.getHeight());
							mergedRow.getCells().get(columnIndex).setMerged(true);
						}
					}
				}
				columnIndex++;
			}
			rowIndex++;
		}
	}
}
