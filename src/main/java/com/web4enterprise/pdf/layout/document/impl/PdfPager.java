package com.web4enterprise.pdf.layout.document.impl;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.Section;
import com.web4enterprise.pdf.layout.page.PageFormat;
import com.web4enterprise.pdf.layout.page.impl.Page;

public class PdfPager {
	protected Pdf pdf = null;
	protected PdfSection currentSection = null;
	protected Page currentPage = null;
	protected Point cursorPosition = new Point(0.0f, 0.0f);
	
	public PdfPager(Pdf pdf) {
		this.pdf = pdf;
	}
	
	public void addPage() {
		//currentPage can never be null because first page of document is created using addPage(PageStyle style, PageHeader header, PageFooter footer). 
		nextPage(currentSection);
	}
	
	public void nextPage(PdfSection pdfSection) {
		if(currentPage != null) {
			currentPage.layOutEndOfPage();
		}
		
		currentSection = pdfSection;
		Section section = currentSection.getSection();
		
		PageFormat currentPageFormat = section.getStyle().getFormat();
		com.web4enterprise.pdf.core.page.Page corePage = pdf.createPage(currentPageFormat.getWidth(), currentPageFormat.getHeight());
		
		currentPage = new Page(this, corePage, section.getStyle(), section.getHeader(), section.getFooter());
		
		currentPage.getFootNotes().setWidth(currentPage.getInnerWidth());
		
		currentPage.layOutNewPage();
	}
	
	public void layOut() {
		for(PdfSectionCommand command : currentSection) {
			command.perform(this);
		}
	}
	
	public Page getCurrentPage() {
		return currentPage;
	}
	
	public Point getCursorPosition() {
		return cursorPosition;
	}
}
