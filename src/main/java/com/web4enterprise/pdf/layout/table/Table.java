package com.web4enterprise.pdf.layout.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.core.path.StraightPath;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.placement.LineStyle;

public class Table implements Element {
	protected List<TableRow> rows = new ArrayList<>();

	protected Map<Integer, Float> fixedColumnsWidths = new HashMap<>();
	protected List<Float> columnsWidths = new ArrayList<>();
	protected boolean repeatHeaderOnNewPage = true;
	protected int nbHeaderRows = 1;
	
	protected boolean computed = false;
	
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
	
	public void computeInnerLayout() {
		computeColumnWidths();
		computeRowsHeights();
		computeCellsWidths();
		computeCellsHeights();
		
		computed = true;
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

	protected void computeRowsHeights() {
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
	
	protected void computeColumnWidths() {
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
	
	protected void computeCellsWidths() {
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
	
	protected void computeCellsHeights() {
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
	
	@Override
	public float getHeight(float width) {
		if(!computed) {
			computeInnerLayout();
		}
		
		float height = 0.0f;
		
		for(TableRow row : getRows()) {
			height += row.getHeight();
		}
		
		return height;
	}

	@Override
	public float layout(Document document, Rect boundingBox, float startY) {
		if(!computed) {
			computeInnerLayout();
		}
		
		Map<Integer, Integer> rowMerges = new HashMap<>();
		for(TableRow row : getRows()) {
			startY = createRow(document, boundingBox, startY, row, rowMerges);
		}
		
		return startY;
	}

	protected float createRow(Document document, Rect boundingBox, float startY, TableRow row, Map<Integer, Integer> rowMerges) {
		Page currentPage = document.getCurrentPage();
		float startX = boundingBox.getLeft();
		
		float rowHeight = row.getHeight();
		if(startY - rowHeight < boundingBox.getBottom()) {
			document.addPage();
			currentPage = document.getCurrentPage();
			startY = document.getCurrentStartY();
			if(isRepeatHeaderOnNewPage()) {
				for(int i = 0; i < getNbHeaderRows(); i++) {
					startY-= createRow(document, boundingBox, startY, getRows().get(i), rowMerges);
				}
			}
		}
		
		for(TableCell cell : row.getCells()) {
			float width = cell.getComputedWidth();
			
			if(!cell.isMerged()) {
				TableCellStyle cellStyle = cell.getStyle();
	
				float bottom = startY - cell.getComputedHeight();
				
				//Fill background color.
				if(cellStyle.getBackgroundColor() != null) {
					StraightPath path = new StraightPath(new Point(startX, startY), 
							new Point(startX + width, startY), 
							new Point(startX + width, bottom),
							new Point(startX, bottom));
					path.setStroked(false);
					path.setFilled(true);
					path.setClosed(true);
					path.setFillColor(cellStyle.getBackgroundColor());
					currentPage.add(path);
				}
				
				float paragraphStartY = startY;
				for(Paragraph paragraph : cell.getParagraphs()) {					
					paragraph.layout(document, 
							new Rect(paragraphStartY, startX, paragraphStartY - row.getHeight(), startX + width), 
							paragraphStartY);
					paragraphStartY -= paragraph.getHeight(width);
				}
				
				//Top border
				if(cellStyle.getTopBorderStyle().width > 0 && cellStyle.getTopBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getTopBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX - cellStyle.getLeftBorderStyle().width / 2, startY), 
							new Point(startX + width + cellStyle.getRightBorderStyle().width / 2, startY));
					currentPage.add(path);
				}
				
				//Left border
				if(cellStyle.getLeftBorderStyle().width > 0 && cellStyle.getLeftBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getLeftBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX, startY - cellStyle.getTopBorderStyle().width / 2), 
							new Point(startX, bottom + cellStyle.getBottomBorderStyle().width / 2));
					currentPage.add(path);
				}
				
				//Bottom border
				if(cellStyle.getBottomBorderStyle().width > 0 && cellStyle.getBottomBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getBottomBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX - cellStyle.getLeftBorderStyle().width / 2, bottom), 
							new Point(startX + width + cellStyle.getRightBorderStyle().width / 2, bottom));
					currentPage.add(path);
				}
				
				//Right border
				if(cellStyle.getRightBorderStyle().width > 0 && cellStyle.getRightBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getRightBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX + width, startY - cellStyle.getTopBorderStyle().width / 2), 
							new Point(startX + width, bottom + cellStyle.getBottomBorderStyle().width / 2));
					currentPage.add(path);
				}
			}
			
			startX += width;
		}
		startY -= rowHeight;
		
		return startY;
	}
	
	@Override
	public Table clone() {
		//TODO: clone this.
		return this;
	}
}
