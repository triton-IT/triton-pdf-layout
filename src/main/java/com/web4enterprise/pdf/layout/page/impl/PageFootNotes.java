package com.web4enterprise.pdf.layout.page.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;

public class PageFootNotes implements PdfDocumentEmbeddable {
	protected List<PdfDocumentEmbeddable> pdfDocumentEmbeddables = new ArrayList<>();
	protected float width = 0.0f;
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
	public void addEmbeddable(PdfDocumentEmbeddable pdfDocumentEmbeddable) {
		pdfDocumentEmbeddables.add(pdfDocumentEmbeddable);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public void clear() {
		pdfDocumentEmbeddables.clear();
	}

	public void compute(PdfPager pdfPager, float width) {
		height = 0.0f;
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			height += pdfDocumentEmbeddable.getHeight(pdfPager, width);
		}
		computedWidth = width;
	}
	
	public boolean isEmpty() {
		return pdfDocumentEmbeddables.isEmpty();
	}
	
	@Override
	public float getHeight(PdfPager pdfPager, float width) {
		if(computedWidth != width) {
			compute(pdfPager, width);
		}
		
		return height;
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
		
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			//We do not pass any footNote because it will end in a never ending loop because pageFootNotes call operations on itself.
			//Furthermore, a footNote cannot create another footNote, it will change the page layout and its too late for this.
			pdfDocumentEmbeddable.clone().layOut(pdfPager, boundingBox, startY, null);
		}
	}

	@Override
	public PageFootNotes clone() {
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
}
