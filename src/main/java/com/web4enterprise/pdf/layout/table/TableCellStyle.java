package com.web4enterprise.pdf.layout.table;

import com.web4enterprise.pdf.layout.placement.BorderStyle;

public class TableCellStyle {
	protected BorderStyle[] bordersStyle = {BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID};
	
	public static final TableCellStyle THIN_SOLID_BORDERS = new TableCellStyle();
	
	public TableCellStyle() {
		
	}
	
	public void setBordersStyle(BorderStyle top, BorderStyle left, BorderStyle bottom, BorderStyle right) {
		bordersStyle[0] = top;
		bordersStyle[1] = left;
		bordersStyle[2] = bottom;
		bordersStyle[3] = right;
	}
	
	public BorderStyle getTopBorderStyle() {
		return bordersStyle[0];
	}
	
	public BorderStyle getLeftBorderStyle() {
		return bordersStyle[1];
	}
	
	public BorderStyle getBottomBorderStyle() {
		return bordersStyle[2];
	}
	
	public BorderStyle getRightBorderStyle() {
		return bordersStyle[3];
	}
}
