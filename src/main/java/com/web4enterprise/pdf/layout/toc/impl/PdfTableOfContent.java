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

package com.web4enterprise.pdf.layout.toc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PdfEmbeddableContainer;
import com.web4enterprise.pdf.layout.page.impl.PdfPageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.pdf.layout.toc.TableOfContent;
import com.web4enterprise.report.commons.geometry.Rect;

/**
 * Defines the table of content for a PDF document.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfTableOfContent extends PdfEmbeddableContainer implements TableOfContent {
	/**
	 * The mapping between level of this table of content and the styles used across the document.
	 */
	protected Map<Style, Integer> styles = new HashMap<>();
	/**
	 * Defines the state of verification of the layout.
	 * If the layout is done and valid, then this property will be set to true.
	 * If the layout is not done or invalid, then this property is set to false. 
	 */
	protected boolean verified = false;

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox,
			PdfPageFootNotes pdfPageFootNotes) {
		pageNumber = pdfPager.getCurrentPageNumber();
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
		
		verified = true;
		pdfDocumentEmbeddables.forEach(embeddable -> {
			PdfParagraph paragraph;
			
			Integer embeddablePage = embeddable.getPageNumber();
			if(embeddablePage != null) {
				paragraph = new PdfParagraph(embeddable.getTOCText(), " ", String.valueOf(embeddablePage));
				paragraph.setLink(embeddable);
			} else {
				//Prepare a default text to place further text the most accurately possible...
				paragraph = new PdfParagraph(embeddable.getTOCText(), " 0");
				verified = false;
			}
			paragraph.layOut(pdfPager, boundingBox, pdfPageFootNotes);
		});
	}
	
	@Override
	public boolean verifyLayOut(PdfPager pdfPager) {
		return verified;
	}

	@Override
	public PdfTableOfContent clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addLevel(int level, Style... styles) {
		for(Style style : styles) {
			this.styles.putIfAbsent(style, level);
		}
	}
	
	/**
	 * Add embeddables to this TOC to be able to generate it.
	 * 
	 * @param embeddables The list of embeddables to add to this TOC.
	 */
	public void addEmbeddables(List<DocumentEmbeddable> embeddables) {
		for(DocumentEmbeddable embeddable : embeddables) {
			if(embeddable != this && styles.containsKey(embeddable.getStyle())) {
				this.pdfDocumentEmbeddables.add((PdfDocumentEmbeddable) embeddable);
			}
		}
	}
}
