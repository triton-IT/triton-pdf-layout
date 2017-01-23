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

public class PdfTableCell implements TableCell {
	public TableCellStyle cellStyle = TableCellStyle.THIN_SOLID_BORDERS;
	protected List<Paragraph> paragraphs = new ArrayList<>();
	protected int mergedRows = 0;
	protected int mergedColumns = 0;
	protected float computedWidth = 0.0f;
	protected float computedHeight = 0.0f;
	protected boolean merged = false;
	
	public PdfTableCell() {
		this.paragraphs.add(new PdfParagraph(""));
	}
	
	public PdfTableCell(String... values) {
		if(values != null) {
			for(String value : values) {
				this.paragraphs.add(new PdfParagraph(value));
			}
		}
	}
	
	public PdfTableCell(Paragraph... paragraphs) {
		if(paragraphs != null) {
			for(Paragraph paragraph : paragraphs) {
				this.paragraphs.add((PdfParagraph) paragraph);
			}
		}
	}
	
	public PdfTableCell(TableCellStyle style, String... values) {
		this(values);
		if(style != null) {
			cellStyle = style;
		}
	}
	
	public PdfTableCell(TableCellStyle style, Paragraph... paragraphs) {
		this(paragraphs);
		if(style != null) {
			cellStyle = style;
		}
	}
	
	public TableCellStyle getStyle() {
		return cellStyle;
	}
	
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public int getMergedRows() {
		return mergedRows;
	}

	public TableCell setMergedRows(int mergedRows) {
		this.mergedRows = mergedRows;
		return this;
	}

	public int getMergedColumns() {
		return mergedColumns;
	}

	public TableCell setMergedColumns(int mergedColumns) {
		this.mergedColumns = mergedColumns;
		return this;
	}

	public boolean isMerged() {
		return merged;
	}

	public void setMerged(boolean merged) {
		this.merged = merged;
	}
	
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
	
	public float getHeight(PdfPager pdfPager, float width) {
		float height = 0;
		for(Paragraph paragraph : paragraphs) {
			height += ((PdfParagraph) paragraph).getHeight(pdfPager, width);
		}
		
		return height;
	}

	public float getComputedWidth() {
		return computedWidth;
	}

	public void setComputedWidth(float computedWidth) {
		this.computedWidth = computedWidth;
	}

	public float getComputedHeight() {
		return computedHeight;
	}

	public void setComputedHeight(float computedHeight) {
		this.computedHeight = computedHeight;
	}
}
