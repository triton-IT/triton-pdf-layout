package com.web4enterprise.pdf.layout;

public class PageStyle {
	protected PageFormat format = PageFormat.A4_PORTRAIT;
	protected Margins margins = Margins.DEFAULT;
	
	public PageStyle() {
	}
	
	/**
	 * Generally, if page format, margins also change, so create a constructor with both.
	 * @param format The page format (width and height).
	 * @param margins
	 */
	public PageStyle(PageFormat format, Margins margins) {
		this.format = format;
		this.margins = margins;
	}
	
	public PageFormat getFormat() {
		return format;
	}

	public void setFormat(PageFormat format) {
		this.format = format;
	}

	public Margins getMargins() {
		return margins;
	}

	public void setMargins(Margins margins) {
		this.margins = margins;
	}
}
