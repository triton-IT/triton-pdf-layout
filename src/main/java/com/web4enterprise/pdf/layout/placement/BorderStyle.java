package com.web4enterprise.pdf.layout.placement;

import com.web4enterprise.pdf.core.styling.Color;

public class BorderStyle {
	public static final BorderStyle NONE = new BorderStyle();
	static {
		NONE.width = 0;
		NONE.lineStyle = LineStyle.NONE;
	}
	public static final BorderStyle THIN_SOLID = new BorderStyle();
	
	public float width = 1.0f;
	public LineStyle lineStyle = LineStyle.SOLID;
	public Color color = Color.BLACK;
	
	public BorderStyle() {
	}
	
	public BorderStyle(float width) {
		this.width = width;
	}

	public BorderStyle(Color color) {
		this.color = color;
	}

	public BorderStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public BorderStyle(float width, Color color) {
		this.width = width;
		this.color = color;
	}

	public BorderStyle(float width, LineStyle lineStyle) {
		this.width = width;
		this.lineStyle = lineStyle;
	}

	public BorderStyle(float width, Color color, LineStyle lineStyle) {
		this.width = width;
		this.lineStyle = lineStyle;
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
