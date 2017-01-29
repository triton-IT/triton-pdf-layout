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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PdfPageFootNotes;
import com.web4enterprise.pdf.layout.page.impl.PdfEmbeddableContainer;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.pdf.layout.toc.TableOfContent;

public class PdfTableOfContent extends PdfEmbeddableContainer implements TableOfContent {	
	protected Map<Style, Integer> styles = new HashMap<>();
	
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
	
	/**
	 * Associate a level to a paragraph style.
	 * 
	 * @param level
	 * @param style
	 */
	public void addLevel(int level, Style... styles) {
		for(Style style : styles) {
			this.styles.putIfAbsent(style, level);
		}
	}
	
	public void addEmbeddables(List<DocumentEmbeddable> embeddables) {
		for(DocumentEmbeddable embeddable : embeddables) {
			if(embeddable != this && styles.containsKey(embeddable.getStyle())) {
				this.pdfDocumentEmbeddables.add((PdfDocumentEmbeddable) embeddable);
			}
		}
	}
}
