package com.web4enterprise.pdf.layout.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web4enterprise.pdf.layout.paragraph.ElementLine;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;

public class TableCell {
	public TableCellStyle cellStyle = TableCellStyle.THIN_SOLID_BORDERS;
	protected List<Paragraph> paragraphs = new ArrayList<>();
	protected int rowSpan = 0;
	protected int columnSpan = 0;
	
	public TableCell(Paragraph... paragraphs) {
		if(paragraphs != null) {
			this.paragraphs = Arrays.asList(paragraphs);
		}
	}
	
	public TableCell(TableCellStyle style, Paragraph... paragraphs) {
		this(paragraphs);
		if(style != null) {
			cellStyle = style;
		}
	}
	
	public TableCell rowSpan(int nbRows) {
		rowSpan = nbRows;
		return this;
	}
	
	public TableCell columnSpan(int nbColumns) {
		columnSpan = nbColumns;
		return this;
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
	
	public float getHeight() {
		float height = 0;
		for(Paragraph paragraph : paragraphs) {
			float paragraphsHeight = 0;
			
			for(ElementLine elementLine : paragraph.getElementLines()) {
				//FIXME: calculate real height.
				paragraphsHeight += paragraph.getStyle().getFontVariant().getHeight(paragraph.getStyle().getFontSize());
			}
			
			if(paragraphsHeight > height) {
				height = paragraphsHeight;
			}
		}
		
		return height;
	}
	
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}
}
