/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.document.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.Section;
import com.web4enterprise.pdf.layout.page.PageFormat;
import com.web4enterprise.pdf.layout.page.impl.Page;

/**
 * The pager responsible for controling layouting of document.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfPager {
	/**
	 * Logger for class.
	 */
	private static final Logger LOGGER = Logger.getLogger(PdfPager.class.getName());
	
	/**
	 * The PDF document.
	 */
	protected Pdf pdf = null;

	/**
	 * The list of sections of document.
	 */
	protected List<PdfSection> pdfSections = new ArrayList<>();
	/**
	 * The section where document is currently writing on.
	 */
	protected PdfSection currentWriteSection = null;
	/**
	 * The section which is currently layed-out. 
	 */
	protected PdfSection currentLayOutSection = null;
	/**
	 * The page currently layed-out.
	 */
	protected Page currentPage = null;
	/**
	 * The number of the page currently layed-out.
	 */
	protected int currentPageNumber = 0;
	/**
	 * The current position of cursor in current page.
	 */
	protected Point cursorPosition = new Point(0.0f, 0.0f);
	
	/**
	 * Create a pager for document.
	 * 
	 * @param pdf The PDF document.
	 */
	public PdfPager(Pdf pdf) {
		this.pdf = pdf;
	}
	
	/**
	 * Add a page (based on previous page style) to document.
	 */
	public void addPage() {
		//currentPage can never be null because first page of document is created using addPage(PageStyle style, PageHeader header, PageFooter footer). 
		nextPage(currentLayOutSection);
	}

	/**
	 * Add a new section to document.
	 * 
	 * @param section The section to add.
	 * @return The section added.
	 */
	public Section nextSection(Section section) {
		//Create a new section from an existing one.
		currentWriteSection = new PdfSection(section);
		pdfSections.add(currentWriteSection);
		return currentWriteSection.getSection();
	}
	
	/**
	 * Add a new section (based on previous section style) to document.
	 * 
	 * @return The new section.
	 */
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
	
	/**
	 * Get the section of document currently under write.
	 * 
	 * @return The current section.
	 */
	public PdfSection getCurrentSection() {
		return currentWriteSection;
	}
	
	/**
	 * Finish to lay-out the current page and start to lay-out a new one.
	 * 
	 * @param pdfSection The current section under lay-out.
	 */
	public void nextPage(PdfSection pdfSection) {
		if(currentPage != null) {
			currentPage.layOutEndOfPage();
		}
		
		currentLayOutSection = pdfSection;
		Section section = currentLayOutSection.getSection();
		
		PageFormat currentPageFormat = section.getStyle().getFormat();
		com.web4enterprise.pdf.core.page.Page corePage = pdf.createPage(currentPageFormat.getWidth(), currentPageFormat.getHeight());
		
		currentPageNumber++;
		currentPage = new Page(this, corePage, section.getStyle(), section.getHeader(), section.getFooter());
		
		currentPage.getFootNotes().setWidth(currentPage.getInnerWidth());
		
		currentPage.layOutNewPage();
	}
	
	/**
	 * Get the current number of the page in document.
	 * 
	 * @return The number of current page.
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
	/**
	 * Lay-out the document document.
	 * This method iterate over each embeddable of document for rendering them.
	 * While the rendering of each embeddable is not verify, the rendering and verification is triggered again.
	 */
	public void layOut() {
		//We layout until all references are up-to-date.
		//Without TOC, it will be done in one run.
		//With TOC or any other dynamic element, this could last 2 or more runs.
		boolean verified = false;
		while(!verified) {
			//Clear all pages that could have been rendered on a previous run.
			pdf.clearPages();
			
			//Tell each embeddable to prepare for next layouting.
			//They usually reset themselves.
			for(PdfSection pdfSection : pdfSections) {
				for(PdfSectionCommand command : pdfSection) {
					command.prepareNextLayOut(this);
				}
			}
			
			//Render all embeddables of document.
			for(PdfSection pdfSection : pdfSections) {
				nextPage(pdfSection);
				for(PdfSectionCommand command : pdfSection) {
					command.layOut(this);
				}
			}
			//Finish layout-ing of document.
			finish();
		
			//Tell all embeddable of document to verify if they have been layouted correctly.
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

	/**
	 * Get page under lay-out.
	 * 
	 * @return The page currently under lay-out.
	 */
	public Page getCurrentPage() {
		return currentPage;
	}
	
	/**
	 * Get the cursor position on page.
	 * 
	 * @return The current cursor position.
	 */
	public Point getCursorPosition() {
		return cursorPosition;
	}
	
	/**
	 * Finish to lay-out document by rendering last footer.
	 */
	protected void finish() {
		Page page = getCurrentPage();
		if(page == null) {
			LOGGER.warning("Finishing a document without any page.");
		} else {
			page.layOutEndOfPage();
		}
	}
}
