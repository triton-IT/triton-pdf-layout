package com.web4enterprise.pdf.layout.document.impl.command;

import com.web4enterprise.pdf.layout.document.impl.PdfSectionCommand;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.page.impl.Page;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;

public class PdfAddEmbeddableCommand implements PdfSectionCommand {
	protected PdfDocumentEmbeddable embeddable;
	
	public PdfAddEmbeddableCommand(PdfDocumentEmbeddable embeddable) {
		this.embeddable = embeddable;
	}
	
	public void perform(PdfPager pdfPager) {
		Page currentPage = pdfPager.getCurrentPage();
		PageFootNotes pageFootNotes = currentPage.getFootNotes();
		embeddable.layOut(pdfPager, 
				currentPage.getInnerRect(),
				pdfPager.getCursorPosition().getY(),
				pageFootNotes);
	}
}
