package com.web4enterprise.pdf.layout.document;

import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.impl.VerticalStopsList;

public class Section {
	protected PageStyle style;
	protected PageHeader header;
	protected PageFooter footer;
	protected VerticalStopsList verticalStops = new VerticalStopsList();

	public Section() {
		this(PageStyle.A4_PORTRAIT, null, null);
	}

	public Section(PageStyle style, PageHeader header, PageFooter footer,
			float... verticalStops) {
		this.style = style;
		this.header = header;
		this.footer = footer;
		this.verticalStops.add(verticalStops);
	}
	
	/**
	 * Add a vertical stop to the current page.
	 * On a call to {@see nextVerticalStop}, the next element will be add to the next available vertical stop.
	 * 
	 * @param position The position in number of units of the vertical stopp to place.
	 */
	public void addVerticalStop(float position) {
		this.verticalStops.add(position);
	}

	public PageStyle getStyle() {
		return style;
	}

	public PageHeader getHeader() {
		return header;
	}

	public PageFooter getFooter() {
		return footer;
	}

	public VerticalStopsList getVerticalStops() {
		return verticalStops;
	}
}
