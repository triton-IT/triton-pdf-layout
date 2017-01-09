package com.web4enterprise.pdf.layout.document.impl;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.page.PageFormat;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;

public class Pager {
	protected Pdf pdf = null;
	protected Page currentPage = null;
	protected Point cursorPosition = new Point(0.0f, 0.0f);
	
	public Pager(Pdf pdf) {
		this.pdf = pdf;
	}
	
	public void addPage() {
		//currentPage can never be null because first page of document is created using addPage(PageStyle style, PageHeader header, PageFooter footer). 
		addPage(currentPage.getStyle(), currentPage.getHeader(), currentPage.getFooter());
	}
	
	public void addPage(PageStyle style, PageHeader header, PageFooter footer) {
		if(currentPage != null) {
			currentPage.layoutEndOfPage();
		}
		
		PageFormat currentPageFormat = style.getFormat();
		com.web4enterprise.pdf.core.page.Page corePage = pdf.createPage(currentPageFormat.getWidth(), currentPageFormat.getHeight());
		
		currentPage = new Page(this, corePage, style, header, footer);
		
		currentPage.layoutNewPage();
	}
	
	public Page getCurrentPage() {
		return currentPage;
	}
	
	public Point getCursorPosition() {
		return cursorPosition;
	}
}
