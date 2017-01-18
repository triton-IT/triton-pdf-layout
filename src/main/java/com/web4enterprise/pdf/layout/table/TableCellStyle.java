package com.web4enterprise.pdf.layout.table;

import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.style.Style;

public class TableCellStyle implements Style {
	protected BorderStyle[] bordersStyle = {BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID};
	protected Color backgroundColor = null;
	
	public static final TableCellStyle THIN_SOLID_BORDERS = new TableCellStyle();
	
	public TableCellStyle() {
	}
	
	public TableCellStyle(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void setBordersStyle(BorderStyle top, BorderStyle left, BorderStyle bottom, BorderStyle right) {
		bordersStyle[0] = top;
		bordersStyle[1] = left;
		bordersStyle[2] = bottom;
		bordersStyle[3] = right;
	}
	
	public void setBordersStyle(BorderStyle top, BorderStyle left, BorderStyle bottom, BorderStyle right, Color backgroundColor) {
		bordersStyle[0] = top;
		bordersStyle[1] = left;
		bordersStyle[2] = bottom;
		bordersStyle[3] = right;
		this.backgroundColor = backgroundColor;
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

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
