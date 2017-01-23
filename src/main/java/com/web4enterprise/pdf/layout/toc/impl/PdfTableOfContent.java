package com.web4enterprise.pdf.layout.toc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.pdf.layout.toc.TableOfContent;

public class PdfTableOfContent implements TableOfContent, PdfDocumentEmbeddable {
	protected Float linkX = null;
	protected Float linkY = null;
	protected Integer pageId = null;
	
	protected Map<Style, Integer> styles = new HashMap<>();
	protected ArrayList<PdfDocumentEmbeddable> embeddables = new ArrayList<>();
	
	protected boolean verified = false;
	
	protected Integer pageNumber = null;

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
	public float getHeight(PdfPager pdfPager, float width) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox,
			PageFootNotes pageFootNotes) {
		pageNumber = pdfPager.getCurrentPageNumber();
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
		
		verified = true;
		embeddables.forEach(embeddable -> {
			PdfParagraph paragraph;
			
			Integer embeddablePage = embeddable.getPageNumber();
			if(embeddablePage != null) {
				paragraph = new PdfParagraph(embeddable.getTOCText(), " ", String.valueOf(embeddablePage));
			} else {
				//Prepare a default text to place further text the most accurately possible...
				paragraph = new PdfParagraph(embeddable.getTOCText(), " 0");
				verified = false;
			}
			paragraph.layOut(pdfPager, boundingBox, pageFootNotes);
		});
	}
	
	@Override
	public boolean verifyLayOut(PdfPager pdfPager) {
		return verified;
	}

	@Override
	public PdfDocumentEmbeddable clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Style getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTOCText() {
		//TOC is not supported in TOC.
		return null;
	}
	
	@Override
	public Integer getPageNumber() {
		return pageNumber;
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
				this.embeddables.add((PdfDocumentEmbeddable) embeddable);
			}
		}
	}
}
