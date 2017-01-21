package com.web4enterprise.pdf.layout.document.impl;


public interface PdfSectionCommand {
	void prepareNextLayOut(PdfPager pdfPager);
	void layOut(PdfPager pdfPager);
	boolean verifyLayOut(PdfPager pdfPager);
}
