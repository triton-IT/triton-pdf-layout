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

package com.web4enterprise.pdf.layout.document.impl.command;

import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.document.impl.PdfSectionCommand;
import com.web4enterprise.pdf.layout.page.impl.Page;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;

/**
 * PDF command to add a embeddable to a section.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfAddEmbeddableCommand implements PdfSectionCommand {
	/**
	 * The embeddable to add to section.
	 */
	protected PdfDocumentEmbeddable embeddable;
	
	/**
	 * Create a command to add an embeddable to a section.
	 * 
	 * @param embeddable The embeddable to add to section.
	 */
	public PdfAddEmbeddableCommand(PdfDocumentEmbeddable embeddable) {
		this.embeddable = embeddable;
	}

	@Override
	public void prepareNextLayOut(PdfPager pdfPager) {
		//Do nothing.
	}
	
	@Override
	public void layOut(PdfPager pdfPager) {
		Page currentPage = pdfPager.getCurrentPage();
		PageFootNotes pageFootNotes = currentPage.getFootNotes();
		embeddable.layOut(pdfPager, 
				currentPage.getInnerRect(),
				pageFootNotes);
	}
	
	@Override
	public boolean verifyLayOut(PdfPager pdfPager) {
		return embeddable.verifyLayOut(pdfPager);
	}
}
