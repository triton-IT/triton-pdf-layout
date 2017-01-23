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

package com.web4enterprise.pdf.layout.page.footer.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;
import com.web4enterprise.pdf.layout.style.Style;

public class PdfPageFooter implements PageFooter, PdfDocumentEmbeddable {
	protected List<PdfDocumentEmbeddable> pdfDocumentEmbeddables = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;

	protected Float linkX = null;
	protected Float linkY = null;
	protected Integer pageId = null;
	
	protected Integer pageNumber = null;

	@Override
	public void addEmbeddables(DocumentEmbeddable... embeddables) {
		for(DocumentEmbeddable embeddable : embeddables) {
			pdfDocumentEmbeddables.add((PdfDocumentEmbeddable) embeddable);
		}		
	}
	
	@Override
	public float getHeight(PdfPager pdfPager, float width) {
		if(computedWidth != width) {
			compute(pdfPager, width);
		}
		
		return height;
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, PageFootNotes pageFootNotes) {
		pageNumber = pdfPager.getCurrentPageNumber();
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
		
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : this.pdfDocumentEmbeddables) {
			//Need to clone element because footer is repeated and changing any value of the element for a page will change it for each page.
			pdfDocumentEmbeddable.clone().layOut(pdfPager, boundingBox, pageFootNotes);
		}
	}

	@Override
	public PdfPageFooter clone() {
		//TODO: clone this.
		return this;
	}

	@Override
	public Integer getPage() {
		return pageId;
	}

	@Override
	public Float getLinkX() {
		return linkX;
	}

	@Override
	public Float getLinkY() {
		return linkY;
	}

	@Override
	public Style getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTOCText() {
		//Footer is not supported in TOC.
		return null;
	}
	
	@Override
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void compute(PdfPager pdfPager, float width) {
		height = 0.0f;
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			height += pdfDocumentEmbeddable.getHeight(pdfPager, width);
		}
		computedWidth = width;
	}
}
