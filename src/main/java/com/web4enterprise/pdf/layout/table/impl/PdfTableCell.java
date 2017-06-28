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
import java.util.List;

import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddableLine;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.table.TableCell;
import com.web4enterprise.pdf.layout.table.TableCellStyle;

/**
 * Implements a table cell for a PDF document.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfTableCell implements TableCell {
	/**
	 * The style of this cell.
	 */
	public TableCellStyle cellStyle = TableCellStyle.THIN_SOLID_BORDERS;
	/**
	 * The list of paragraphs in this cell.
	 */
	protected List<Paragraph> paragraphs = new ArrayList<>();
	/**
	 * The number of merged rows.
	 */
	protected int mergedRows = 0;
	/**
	 * The number of merged columns.
	 */
	protected int mergedColumns = 0;
	/**
	 * The computed width.
	 */
	protected float computedWidth = 0.0f;
	/**
	 * The computed height.
	 */
	protected float computedHeight = 0.0f;
	/**
	 * Defines if table cell is merged.
	 */
	protected boolean merged = false;
	
	/**
	 * Create an empty cell.
	 */
	public PdfTableCell() {
	}
	
	/**
	 * Create a cell filled-in with paragraphs with specified values.
	 * 
	 * @param values The values to add as paragraph to this cell.
	 */
	public PdfTableCell(String... values) {
		if(values != null) {
			for(String value : values) {
				this.paragraphs.add(new PdfParagraph(value));
			}
		}
	}
	
	/**
	 * Create a cell filled-in with specified paragraphs.
	 * 
	 * @param paragraphs The paragraphs to add to this cell.
	 */
	public PdfTableCell(Paragraph... paragraphs) {
		if(paragraphs != null) {
			for(Paragraph paragraph : paragraphs) {
				this.paragraphs.add((PdfParagraph) paragraph);
			}
		}
	}

	/**
	 * Create a cell filled-in with paragraphs with specified values.
	 * 
	 * @param style The style to set to this cell.
	 * @param values The values to add as paragraph to this cell.
	 */
	public PdfTableCell(TableCellStyle style, String... values) {
		this(values);
		if(style != null) {
			cellStyle = style;
		}
	}
	
	/**
	 * Create a cell filled-in with specified paragraphs.
	 * 
	 * @param style The style to set to this cell.
	 * @param paragraphs The paragraphs to set to this cell.
	 */
	public PdfTableCell(TableCellStyle style, Paragraph... paragraphs) {
		this(paragraphs);
		if(style != null) {
			cellStyle = style;
		}
	}

	@Override
	public TableCellStyle getStyle() {
		return cellStyle;
	}

	@Override
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	@Override
	public int getMergedRows() {
		return mergedRows;
	}

	@Override
	public TableCell setMergedRows(int mergedRows) {
		this.mergedRows = mergedRows;
		return this;
	}

	@Override
	public int getMergedColumns() {
		return mergedColumns;
	}

	@Override
	public TableCell setMergedColumns(int mergedColumns) {
		this.mergedColumns = mergedColumns;
		return this;
	}

	@Override
	public boolean isMerged() {
		return merged;
	}

	@Override
	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	/**
	 * Get the width of this cell EXCLUDING merged cells.
	 * 
	 * @return THe width of this cell.
	 */
	public float getWidth() {
		float width = 0;
		for(PdfParagraph paragraph : (PdfParagraph[]) paragraphs.toArray()) {
			for(PdfParagraphEmbeddableLine pdfParagraphEmbeddableLine : paragraph.getEmbeddableLines()) {
				int elementWidth = pdfParagraphEmbeddableLine.getWidth(paragraph.getStyle(), paragraph.getStyle().getFontSize());
				if(elementWidth > width) {
					width = elementWidth;
				}
			}
		}
		
		return width;
	}

	/**
	 * Get the height of this cell.
	 * 
	 * @param pdfPager The pager to get information from.
	 * @param width The available width for this cell. 
	 * @return The height of this cell for the given width.
	 */
	public float getHeight(PdfPager pdfPager, float width) {
		float height = 0;
		for(Paragraph paragraph : paragraphs) {
			height += ((PdfParagraph) paragraph).getHeight(pdfPager, width);
		}
		
		return height;
	}

	/**
	 * Get the computed width of this cell.
	 * 
	 * @return The width.
	 */
	public float getComputedWidth() {
		return computedWidth;
	}

	/**
	 * Set the computed width of this cell.
	 * 
	 * @param computedWidth The computed width.
	 */
	public void setComputedWidth(float computedWidth) {
		this.computedWidth = computedWidth;
	}

	/**
	 * Get the computed height of this cell.
	 * 
	 * @return The computedHeight.
	 */
	public float getComputedHeight() {
		return computedHeight;
	}

	/**
	 * Set the computed height of this cell.	
	 * 
	 * @param computedHeight The computed height.
	 */
	public void setComputedHeight(float computedHeight) {
		this.computedHeight = computedHeight;
	}
}
