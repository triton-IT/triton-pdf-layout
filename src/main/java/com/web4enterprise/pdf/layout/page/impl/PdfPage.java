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

package com.web4enterprise.pdf.layout.page.impl;

import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.impl.PdfPageFooter;
import com.web4enterprise.pdf.layout.page.header.impl.PdfPageHeader;
import com.web4enterprise.report.commons.geometry.Rect;

/**
 * Represents a page in a PDF document.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfPage {
	/**
	 * The PDF core page.
	 */
	protected Page corePage;
	/**
	 * The pager used for this page.
	 */
	protected PdfPager pdfPager;
	/**
	 * The style of the page.
	 */
	protected PageStyle style = PageStyle.A4_PORTRAIT;
	/**
	 * The header of this page.
	 */
	protected PdfPageHeader header = null;
	/**
	 * The footer of this page.
	 */
	protected PdfPageFooter footer = null;
	/**
	 * The foot-notes of this page.
	 */
	protected PdfPageFootNotes footNotes = new PdfPageFootNotes();
	/**
	 * The identifier of next fooot-note to add.
	 */
	protected int currentFootNoteId = 1;
	
	/**
	 * Create a page from a pager, a core page and current layout parameters.
	 * 
	 * @param pdfPager The pager to use for this page.
	 * @param corePage The core page where this page will be rendered.
	 * @param pageStyle The style to use for this page.
	 * @param pageHeader The header to use for this page.
	 * @param pageFooter The footer to use for this page.
	 */
	public PdfPage(PdfPager pdfPager, Page corePage, PageStyle pageStyle, PdfPageHeader pageHeader, PdfPageFooter pageFooter) {
		this.corePage = corePage;
		
		this.pdfPager = pdfPager;
		
		this.style = pageStyle;
		this.header = pageHeader;
		this.footer = pageFooter;
	}
	
	/**
	 * Get core page.
	 * 
	 * @return The core page.
	 */
	public Page getCorePage() {
		return corePage;
	}
	
	/**
	 * Get the footer of this page.
	 * 
	 * @return The footer.
	 */
	public PdfPageFooter getFooter() {
		return footer;
	}
	
	/**
	 * Get the foot-notes of this page.
	 * 
	 * @return The foot-notes.
	 */
	public PdfPageFootNotes getFootNotes() {
		return footNotes;
	}
	
	/**
	 * Generate the next available fooot-note identifier.
	 * 
	 * @return A valid and unique foot-note identifier.
	 */
	public String generateFootNoteId() {
		return String.valueOf(currentFootNoteId++);
	}
	
	/**
	 * Layout the start of this  page.
	 */
	public void layOutNewPage() {
		float yPosition = style.getFormat().getHeight() - style.getMargins().getTop();
		pdfPager.getCursorPosition().setY(yPosition);
		
		if(header != null) {
			header.layOut(pdfPager, 
					new Rect(style.getMargins().getTop(), 
							style.getMargins().getLeft(),
							style.getMargins().getBottom(),
							style.getFormat().getWidth() - style.getMargins().getRight()),
						footNotes);
			pdfPager.getCursorPosition().setY(yPosition - header.getHeight(pdfPager, style.getInnerWidth()));
		}
	}
	
	/**
	 * Layout the end of this page.
	 */
	public void layOutEndOfPage() {
		layOutFootNotes();
		layOutFooter();
	}
	
	/**
	 * Get the inner top of this page.
	 * 
	 * @return The inner top of this page.
	 */
	public float getInnerTop() {
		float top = style.getInnerTop();
		
		PdfPageHeader pageHeader = this.header;
		if(pageHeader != null) {
			top -= pageHeader.getHeight(pdfPager, style.getInnerWidth());
		}
		
		return top;
	}
	
	/**
	 * Get inner bottom of page. (e.g. bottom of page + footer. Foot notes are not added to bottom calculation). 
	 * 
	 * @return The inner bottom of this page.
	 */
	public float getInnerBottom() {
		float bottom = style.getInnerBottom();

		PdfPageFooter pageFooter = this.footer;
		if(pageFooter != null) {
			bottom += pageFooter.getHeight(pdfPager, style.getInnerWidth());
		}
		
		return bottom;
	}
	
	/**
	 * Get the inner left of this page.
	 * 
	 * @return The inner left of this page.
	 */
	public float getInnerLeft() {
		return style.getInnerLeft();
	}

	/**
	 * Get the inner right of this page.
	 * 
	 * @return The inner right of this page.
	 */
	public float getInnerRight() {
		return style.getInnerRight();
	}

	/**
	 * Get the inner width of this page.
	 * 
	 * @return The inner width of this page.
	 */
	public float getInnerWidth() {
		return style.getInnerRight() - style.getInnerLeft();
	}
	
	/**
	 * Get inner rectangle of page. (e.g. use margins and header/footer in calculation. Foot notes are not used in calculation).
	 * 
	 * @return The inner rectangle.
	 */
	public Rect getInnerRect() {		
		return new Rect(getInnerTop(), 
				getInnerLeft(),
				getInnerBottom(),
				getInnerRight());
	}
	
	/**
	 * Lay-out the foot-notes of this page.
	 */
	protected void layOutFootNotes() {
		///If we have a footer to lay out.
		 if(!footNotes.isEmpty()) {
			float footerHeight = footer.getHeight(pdfPager, style.getInnerWidth());
			float footNotesHeight = footNotes.getHeight(pdfPager, style.getInnerWidth());

			footNotes.layOut(pdfPager, 
					new Rect(style.getInnerBottom() + footerHeight + footNotesHeight, 
							style.getInnerLeft(),
							style.getInnerBottom() + footerHeight,
							style.getInnerRight()),
					footNotes);
		}
	}
	
	/**
	 * Lay-out the footer of this page.
	 */
	protected void layOutFooter() {
		///If we have a footer to lay out.
		 if(footer != null) {
			float footerHeight = footer.getHeight(pdfPager, style.getInnerWidth());

			pdfPager.getCursorPosition().setY(style.getInnerBottom() + footerHeight);
			//We do not allow footNotes on footers. This will change page layouting and its too late for this.
			footer.layOut(pdfPager, 
					new Rect(style.getInnerBottom() + footerHeight, 
							style.getInnerLeft(),
							style.getInnerBottom(),
							style.getInnerRight()),
					null);
		}
	}
}
