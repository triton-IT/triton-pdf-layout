package com.web4enterprise.pdf.layout.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web4enterprise.pdf.layout.document.impl.Layouter;
import com.web4enterprise.pdf.layout.paragraph.ElementLine;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;

public class TableCell {
	public TableCellStyle cellStyle = TableCellStyle.THIN_SOLID_BORDERS;
	protected List<Paragraph> paragraphs = new ArrayList<>();
	protected int mergedRows = 0;
	protected int mergedColumns = 0;
	protected float computedWidth = 0.0f;
	protected float computedHeight = 0.0f;
	protected boolean merged = false;
	
	public TableCell() {
		this.paragraphs.add(new Paragraph(""));
	}
	
	public TableCell(String... values) {
		if(values != null) {
			for(String value : values) {
				this.paragraphs.add(new Paragraph(value));
			}
		}
	}
	
	public TableCell(Paragraph... paragraphs) {
		if(paragraphs != null) {
			this.paragraphs = Arrays.asList(paragraphs);
		}
	}
	
	public TableCell(TableCellStyle style, String... values) {
		this(values);
		if(style != null) {
			cellStyle = style;
		}
	}
	
	public TableCell(TableCellStyle style, Paragraph... paragraphs) {
		this(paragraphs);
		if(style != null) {
			cellStyle = style;
		}
	}
	
	public TableCellStyle getStyle() {
		return cellStyle;
	}
	
	public float getWidth() {
		float width = 0;
		for(Paragraph paragraph : paragraphs) {
			for(ElementLine elementLine : paragraph.getElementLines()) {
				int elementWidth = elementLine.getWidth(paragraph.getStyle(), paragraph.getStyle().getFontSize());
				if(elementWidth > width) {
					width = elementWidth;
				}
			}
		}
		
		return width;
	}
	
	public float getHeight(Layouter layouter, float width) {
		float height = 0;
		for(Paragraph paragraph : paragraphs) {
			height += paragraph.getHeight(layouter, width);
		}
		
		return height;
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

	public boolean isMerged() {
		return merged;
	}

	public void setMerged(boolean merged) {
		this.merged = merged;
	}
}
