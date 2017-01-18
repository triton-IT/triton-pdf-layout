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
	protected int pageId;
	protected float linkX;
	protected float linkY;
	
	protected Map<Style, Integer> styles = new HashMap<>();
	protected ArrayList<DocumentEmbeddable> embeddables = new ArrayList<>();

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
	public float getHeight(PdfPager pdfPager, float width) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox,
			PageFootNotes pageFootNotes) {
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
		
		embeddables.forEach(embeddable -> {
			PdfParagraph paragraph = new PdfParagraph(embeddable.getTOCText());
			paragraph.layOut(pdfPager, boundingBox, pageFootNotes);
		});
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
				this.embeddables.add(embeddable);
			}
		}
	}
}
