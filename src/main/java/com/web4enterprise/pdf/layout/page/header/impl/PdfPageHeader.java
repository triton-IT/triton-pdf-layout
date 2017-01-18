package com.web4enterprise.pdf.layout.page.header.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;
import com.web4enterprise.pdf.layout.style.Style;

public class PdfPageHeader implements PageHeader, PdfDocumentEmbeddable {
	protected List<PdfDocumentEmbeddable> pdfDocumentEmbeddables = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;

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
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
		
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			pdfDocumentEmbeddable.clone().layOut(pdfPager, boundingBox, pageFootNotes);
		}
	}

	@Override
	public PdfPageHeader clone() {
		//TODO: clone this.
		return this;
	}

	@Override
	public int getPage() {
		return pageId;
	}

	@Override
	public float getLinkX() {
		return linkX;
	}

	@Override
	public float getLinkY() {
		return linkY;
	}

	@Override
	public Style getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTOCText() {
		//Header is not supported in TOC.
		return null;
	}

	protected void compute(PdfPager pdfPager, float width) {
		height = 0.0f;
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			height += pdfDocumentEmbeddable.getHeight(pdfPager, width);
		}
		computedWidth = width;
	}
}
