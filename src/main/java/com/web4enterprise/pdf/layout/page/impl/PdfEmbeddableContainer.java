package com.web4enterprise.pdf.layout.page.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;

/**
 * Defines a container for embeddables.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfEmbeddableContainer extends PdfDocumentEmbeddable implements PageHeader, PageFooter {
	/**
	 * The list of embeddables in this container.
	 */
	protected List<PdfDocumentEmbeddable> pdfDocumentEmbeddables = new ArrayList<>();

	@Override
	public void addEmbeddables(DocumentEmbeddable... embeddables) {
		for(DocumentEmbeddable embeddable : embeddables) {
			pdfDocumentEmbeddables.add((PdfDocumentEmbeddable) embeddable);
		}
	}
	
	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, PdfPageFootNotes pdfPageFootNotes) {
		super.layOut(pdfPager, boundingBox, pdfPageFootNotes);
		
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			pdfDocumentEmbeddable.clone().layOut(pdfPager, boundingBox, pdfPageFootNotes);
		}
	}

	@Override
	public PdfEmbeddableContainer clone() {
		//TODO: clone this.
		return this;
	}
	
	@Override
	public void compute(PdfPager pdfPager, float width) {
		height = 0.0f;
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			height += pdfDocumentEmbeddable.getHeight(pdfPager, width);
		}
		computedWidth = width;
	}
	
	/**
	 * Check if container contains embeddables.
	 * 
	 * @return true if container contains embeddable, false otherwise.
	 */
	public boolean isEmpty() {
		return pdfDocumentEmbeddables.isEmpty();
	}
}
