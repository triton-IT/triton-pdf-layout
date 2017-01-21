package com.web4enterprise.pdf.layout.document.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.Section;
import com.web4enterprise.pdf.layout.page.PageFormat;
import com.web4enterprise.pdf.layout.page.impl.Page;

public class PdfPager {
	/**
	 * Logger for class.
	 */
	private static final Logger LOGGER = Logger.getLogger(PdfPager.class.getName());
	
	protected Pdf pdf = null;

	protected List<PdfSection> pdfSections = new ArrayList<>();
	protected PdfSection currentWriteSection = null;
	protected PdfSection currentLayOutSection = null;
	
	protected Page currentPage = null;
	protected Point cursorPosition = new Point(0.0f, 0.0f);
	
	public PdfPager(Pdf pdf) {
		this.pdf = pdf;
	}
	
	public void addPage() {
		//currentPage can never be null because first page of document is created using addPage(PageStyle style, PageHeader header, PageFooter footer). 
		nextPage(currentLayOutSection);
	}

	public Section nextSection(Section section) {
		//Create a new section from an existing one.
		currentWriteSection = new PdfSection(section);
		pdfSections.add(currentWriteSection);
		return currentWriteSection.getSection();
	}
	
	public Section nextSection() {
		//If we already have a section, create one from existing one.
		if(currentWriteSection != null) {
			nextSection(currentWriteSection.getSection());
		} else {
			//Else create a default section.
			nextSection(new Section());
		}
		
		return currentWriteSection.getSection();
	}
	
	public PdfSection getCurrentSection() {
		return currentWriteSection;
	}
	
	public void nextPage(PdfSection pdfSection) {
		if(currentPage != null) {
			currentPage.layOutEndOfPage();
		}
		
		currentLayOutSection = pdfSection;
		Section section = currentLayOutSection.getSection();
		
		PageFormat currentPageFormat = section.getStyle().getFormat();
		com.web4enterprise.pdf.core.page.Page corePage = pdf.createPage(currentPageFormat.getWidth(), currentPageFormat.getHeight());
		
		currentPage = new Page(this, corePage, section.getStyle(), section.getHeader(), section.getFooter());
		
		currentPage.getFootNotes().setWidth(currentPage.getInnerWidth());
		
		currentPage.layOutNewPage();
	}
	
	public void layOut() {
		//We layout until all references are up-to-date.
		//Without TOC, it will be done in one run.
		//With TOC or any other dynamic element, this could last 2 or more runs.
		boolean verified = false;
		while(!verified) {
			pdf.clearPages();
			
			for(PdfSection pdfSection : pdfSections) {
				for(PdfSectionCommand command : pdfSection) {
					command.prepareNextLayOut(this);
				}
			}
			
			for(PdfSection pdfSection : pdfSections) {
				nextPage(pdfSection);
				for(PdfSectionCommand command : pdfSection) {
					command.layOut(this);
				}
			}
			finish();
		
			verified = true;
			for(PdfSection pdfSection : pdfSections) {
				for(PdfSectionCommand command : pdfSection) {
					verified = command.verifyLayOut(this);
					if(!verified) {
						break;
					}
				}
				if(!verified) {
					break;
				}
			}
		}
	}

	public Page getCurrentPage() {
		return currentPage;
	}
	
	public Point getCursorPosition() {
		return cursorPosition;
	}
	
	protected void finish() {
		Page page = getCurrentPage();
		if(page == null) {
			LOGGER.warning("Finishing a document without any page.");
		} else {
			page.layOutEndOfPage();
		}
	}
}
