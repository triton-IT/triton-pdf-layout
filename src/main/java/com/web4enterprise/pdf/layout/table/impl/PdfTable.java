/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.table.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.path.StraightPath;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PdfPage;
import com.web4enterprise.pdf.layout.page.impl.PdfPageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.placement.LineStyle;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.TableCell;
import com.web4enterprise.pdf.layout.table.TableCellStyle;
import com.web4enterprise.pdf.layout.table.TableRow;

public class PdfTable extends PdfDocumentEmbeddable implements Table {
	protected List<TableRow> rows = new ArrayList<>();

	protected Map<Integer, Float> fixedColumnsWidths = new HashMap<>();
	protected List<Float> columnsWidths = new ArrayList<>();
	protected boolean repeatHeader = true;
	protected int nbHeaderRows = 1;
	
	protected boolean computed = false;

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, PdfPageFootNotes pdfPageFootNotes) {
		pageNumber = pdfPager.getCurrentPageNumber();
		if(!computed) {
			computeInnerLayout(pdfPager);
		}
		
		float startY = pdfPager.getCursorPosition().getY();
		
		Map<Integer, Integer> rowMerges = new HashMap<>();
		for(TableRow row : getRows()) {
			float rowHeight = createRow(pdfPager, boundingBox, startY, row, rowMerges, pdfPageFootNotes);
			startY -= rowHeight;
		}
		
		pdfPager.getCursorPosition().setY(startY);
	}
	
	@Override
	public PdfTable clone() {
		//TODO: clone this.
		return this;
	}
	
	@Override
	public TableCell createTableCell() {
		return new PdfTableCell();		
	}

	@Override
	public TableCell createTableCell(String... values) {
		return new PdfTableCell(values);
		
	}

	@Override
	public TableCell createTableCell(Paragraph... paragraphs) {
		return new PdfTableCell(paragraphs);
		
	}

	@Override
	public TableCell createTableCell(TableCellStyle style, String... values) {
		return new PdfTableCell(style, values);
		
	}

	@Override
	public TableCell createTableCell(TableCellStyle style, Paragraph... paragraphs) {
		return new PdfTableCell(style, paragraphs);
	}
	
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
	
	public void computeInnerLayout(PdfPager pdfPager) {
		computeColumnWidths();
		computeRowsHeights(pdfPager);
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

	protected void computeRowsHeights(PdfPager pdfPager) {
		for(TableRow row : rows) {
			int columnIndex = 0;
			float currentRowHeight = 0;
			for(TableCell cell : row.getCells()) {
				PdfTableCell pdfCell = (PdfTableCell) cell;
				float currentCellHeight = pdfCell.getHeight(pdfPager, columnsWidths.get(columnIndex));
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
				PdfTableCell pdfCell = (PdfTableCell) cell;
				Float fixedWidth = fixedColumnsWidths.get(columnIndex);
				if(fixedWidth != null) {
					currentColumnsWidths.add(fixedWidth);
				} else {
					float currentWidth = pdfCell.getWidth();
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
				PdfTableCell pdfCell = (PdfTableCell) cell;
				if(nbMergedCells > 0) {
					cell.setMerged(true);
					nbMergedCells--;
				} else {
					//Get width for merged cells if any.
					for(int i = 0; i <= cell.getMergedColumns(); i++) {
						pdfCell.setComputedWidth(pdfCell.getComputedWidth() + getColumnWidth(columnIndex + i));
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
				PdfTableCell pdfCell = (PdfTableCell) cell;
				if(!cell.isMerged()) {
					pdfCell.setComputedHeight(row.getHeight());
					
					//Get height for merged cells if any.
					for(int i = 1; i <= cell.getMergedRows(); i++) {
						TableRow mergedRow = rows.get(rowIndex + i);
						if(mergedRow != null) {
							pdfCell.setComputedHeight(pdfCell.getComputedHeight() + mergedRow.getHeight());
							mergedRow.getCells().get(columnIndex).setMerged(true);
						}
					}
				}
				columnIndex++;
			}
			rowIndex++;
		}
	}

	protected float createRow(PdfPager pdfPager, Rect boundingBox, float startY, TableRow row, Map<Integer, Integer> rowMerges, PdfPageFootNotes pdfPageFootNotes) {
		PdfPage currentPage = pdfPager.getCurrentPage();
		float startX = boundingBox.getLeft();
		
		float rowHeight = row.getHeight();
		if(startY - rowHeight < boundingBox.getBottom()) {
			pdfPager.addPage();
			currentPage = pdfPager.getCurrentPage();
			if(isRepeatHeader()) {
				for(int i = 0; i < getNbHeaderRows(); i++) {
					createRow(pdfPager, boundingBox, startY, getRows().get(i), rowMerges, pdfPageFootNotes);
				}
			}
		}
		
		//If this is the first row to create, then set link to it.
		if(pageId == null) {
			pageId = currentPage.getCorePage().getId();
			linkX = startX;
			linkY = startY;
		}

		float cellStartY = pdfPager.getCursorPosition().getY();
		float lowestCellY = cellStartY;
		for(TableCell cell : row.getCells()) {
			pdfPager.getCursorPosition().setY(cellStartY);
			PdfTableCell pdfCell = (PdfTableCell) cell;
			float width = pdfCell.getComputedWidth();
			
			if(!cell.isMerged()) {
				TableCellStyle cellStyle = cell.getStyle();
	
				float bottom = startY - pdfCell.getComputedHeight();
				
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
				
				for(Paragraph paragraph : cell.getParagraphs()) {
					((PdfParagraph) paragraph).layOut(pdfPager, 
							new Rect(cellStartY, startX, cellStartY - row.getHeight(), startX + width), 
							pdfPageFootNotes);
					if(pdfPager.getCursorPosition().getY() < lowestCellY) {
						lowestCellY = pdfPager.getCursorPosition().getY();
					}
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
		
		pdfPager.getCursorPosition().setY(lowestCellY);

		return rowHeight;
	}

	@Override
	public void compute(PdfPager pdfPager, float width) {
		computeInnerLayout(pdfPager);
		
		height = 0.0f;
		
		for(TableRow row : getRows()) {
			height += row.getHeight();
		}
	}
}
