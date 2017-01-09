package com.web4enterprise.pdf.layout.table.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.path.StraightPath;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.placement.LineStyle;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.TableCell;
import com.web4enterprise.pdf.layout.table.TableCellStyle;
import com.web4enterprise.pdf.layout.table.TableRow;

public class PdfTable implements Table, PdfDocumentEmbeddable {
	protected List<TableRow> rows = new ArrayList<>();

	protected Map<Integer, Float> fixedColumnsWidths = new HashMap<>();
	protected List<Float> columnsWidths = new ArrayList<>();
	protected boolean repeatHeader = true;
	protected int nbHeaderRows = 1;
	
	protected boolean computed = false;

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
	public TableRow addRow(TableCell...cells) {
		TableRow row = new TableRow(cells);
		rows.add(row);
		return row;
	}
	
	public List<TableRow> getRows() {
		return rows;
	}
	
	public float getColumnWidth(int index) {
		return columnsWidths.get(index);
	}
	
	public void computeInnerLayout(Pager pager) {
		computeColumnWidths();
		computeRowsHeights(pager);
		computeCellsWidths();
		computeCellsHeights();
		
		computed = true;
	}
	
	public void setColumnWidth(int columnIndex, float columnWidth) {
		fixedColumnsWidths.put(columnIndex, columnWidth);
	}
	
	public boolean isRepeatHeader() {
		return repeatHeader;
	}

	public void setRepeatHeader(boolean repeatHeader) {
		this.repeatHeader = repeatHeader;
	}

	public int getNbHeaderRows() {
		return nbHeaderRows;
	}

	public void setNbHeaderRows(int nbHeaderRows) {
		this.nbHeaderRows = nbHeaderRows;
	}

	protected void computeRowsHeights(Pager pager) {
		for(TableRow row : rows) {
			int columnIndex = 0;
			float currentRowHeight = 0;
			for(TableCell cell : row.getCells()) {
				float currentCellHeight = cell.getHeight(pager, columnsWidths.get(columnIndex));
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
	public float getHeight(Pager pager, float width) {
		if(!computed) {
			computeInnerLayout(pager);
		}
		
		float height = 0.0f;
		
		for(TableRow row : getRows()) {
			height += row.getHeight();
		}
		
		return height;
	}

	@Override
	public void layout(Pager pager, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		if(!computed) {
			computeInnerLayout(pager);
		}
		
		Map<Integer, Integer> rowMerges = new HashMap<>();
		for(TableRow row : getRows()) {
			float rowHeight = createRow(pager, boundingBox, startY, row, rowMerges, pageFootNotes);
			startY -= rowHeight;
		}
		
		pager.getCursorPosition().setY(startY);
	}

	protected float createRow(Pager pager, Rect boundingBox, float startY, TableRow row, Map<Integer, Integer> rowMerges, PageFootNotes pageFootNotes) {
		Page currentPage = pager.getCurrentPage();
		float startX = boundingBox.getLeft();
		
		float rowHeight = row.getHeight();
		if(startY - rowHeight < boundingBox.getBottom()) {
			pager.addPage();
			currentPage = pager.getCurrentPage();
			if(isRepeatHeader()) {
				for(int i = 0; i < getNbHeaderRows(); i++) {
					createRow(pager, boundingBox, startY, getRows().get(i), rowMerges, pageFootNotes);
				}
			}
		}
		
		//If this is the first row to create, then set link to it.
		if(pageId == null) {
			pageId = currentPage.getCorePage().getId();
			linkX = startX;
			linkY = startY;
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
					currentPage.getCorePage().add(path);
				}
				
				float paragraphStartY = startY;
				for(Paragraph paragraph : cell.getParagraphs()) {					
					((PdfParagraph) paragraph).layout(pager, 
							new Rect(paragraphStartY, startX, paragraphStartY - row.getHeight(), startX + width), 
							paragraphStartY, pageFootNotes);
					paragraphStartY -= ((PdfParagraph) paragraph).getHeight(pager, width);
				}
				
				//Top border
				if(cellStyle.getTopBorderStyle().width > 0 && cellStyle.getTopBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getTopBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX - cellStyle.getLeftBorderStyle().width / 2, startY), 
							new Point(startX + width + cellStyle.getRightBorderStyle().width / 2, startY));
					currentPage.getCorePage().add(path);
				}
				
				//Left border
				if(cellStyle.getLeftBorderStyle().width > 0 && cellStyle.getLeftBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getLeftBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX, startY - cellStyle.getTopBorderStyle().width / 2), 
							new Point(startX, bottom + cellStyle.getBottomBorderStyle().width / 2));
					currentPage.getCorePage().add(path);
				}
				
				//Bottom border
				if(cellStyle.getBottomBorderStyle().width > 0 && cellStyle.getBottomBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getBottomBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX - cellStyle.getLeftBorderStyle().width / 2, bottom), 
							new Point(startX + width + cellStyle.getRightBorderStyle().width / 2, bottom));
					currentPage.getCorePage().add(path);
				}
				
				//Right border
				if(cellStyle.getRightBorderStyle().width > 0 && cellStyle.getRightBorderStyle().lineStyle != LineStyle.NONE) {
					BorderStyle borderStyle = cellStyle.getRightBorderStyle();
					StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
							new Point(startX + width, startY - cellStyle.getTopBorderStyle().width / 2), 
							new Point(startX + width, bottom + cellStyle.getBottomBorderStyle().width / 2));
					currentPage.getCorePage().add(path);
				}
			}
			
			startX += width;
		}

		return rowHeight;
	}
	
	@Override
	public PdfTable clone() {
		//TODO: clone this.
		return this;
	}

	@Override
	public int getPage() {
		return pageId;
	}

	@Override
	public float getLinkX() {
		return linkX;
	}

	@Override
	public float getLinkY() {
		return linkY;
	}
}
