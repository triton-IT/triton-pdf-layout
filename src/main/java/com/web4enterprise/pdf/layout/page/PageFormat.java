package com.web4enterprise.pdf.layout.page;

/**
 * The size is in pt (point). This is the metric used byPDF.
 * https://en.wikipedia.org/wiki/Paper_size#Overview:_ISO_paper_Sizes
 */
public class PageFormat {
	public static PageFormat A0_PORTRAIT = new PageFormat(2384, 3370);
	public static PageFormat A0_LANDSCAPE = new PageFormat(3370, 2384);
	public static PageFormat A1_PORTRAIT = new PageFormat(1684, 2384);
	public static PageFormat A1_LANDSCAPE = new PageFormat(2384, 1684);
	public static PageFormat A2_PORTRAIT = new PageFormat(1191, 1684);
	public static PageFormat A2_LANDSCAPE = new PageFormat(1684, 1191);
	public static PageFormat A3_PORTRAIT = new PageFormat(842, 1191);
	public static PageFormat A3_LANDSCAPE = new PageFormat(1191, 842);
	public static PageFormat A4_PORTRAIT = new PageFormat(595, 842);
	public static PageFormat A4_LANDSCAPE = new PageFormat(842, 595);
	public static PageFormat A5_PORTRAIT = new PageFormat(420, 595);
	public static PageFormat A5_LANDSCAPE = new PageFormat(595, 420);
	public static PageFormat A6_PORTRAIT = new PageFormat(298, 420);
	public static PageFormat A6_LANDSCAPE = new PageFormat(420, 298);
	public static PageFormat A7_PORTRAIT = new PageFormat(210, 298);
	public static PageFormat A7_LANDSCAPE = new PageFormat(298, 210);
	public static PageFormat A8_PORTRAIT = new PageFormat(147, 210);
	public static PageFormat A8_LANDSCAPE = new PageFormat(210, 147);
	
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
