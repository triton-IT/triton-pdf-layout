package com.web4enterprise.pdf.layout.page.header.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.page.header.PageHeader;

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
	public float getHeight(Pager pager, float width) {
		if(computedWidth != width) {
			compute(pager, width);
		}
		
		return height;
	}

	@Override
	public void layout(Pager pager, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		pageId = pager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
		
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			pdfDocumentEmbeddable.clone().layout(pager, boundingBox, startY, pageFootNotes);
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

	protected void compute(Pager pager, float width) {
		height = 0.0f;
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			height += pdfDocumentEmbeddable.getHeight(pager, width);
		}
		computedWidth = width;
	}
}
