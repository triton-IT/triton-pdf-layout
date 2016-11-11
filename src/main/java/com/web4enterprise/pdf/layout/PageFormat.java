package com.web4enterprise.pdf.layout;

/**
 * The size is in pt (point). This is the metric used byPDF.
 * http://www.printernational.org/iso-paper-sizes.php
 */
public class PageFormat {
	public static PageFormat A4_PORTRAIT = new PageFormat(595, 842);
	public static PageFormat A4_LANDSCAPE = new PageFormat(842, 595);
	public static PageFormat A3_PORTRAIT = new PageFormat(842, 1191);
	public static PageFormat A3_LANDSCAPE = new PageFormat(1191, 842);
	
	protected int width;
	protected int height;
	
	public PageFormat(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
