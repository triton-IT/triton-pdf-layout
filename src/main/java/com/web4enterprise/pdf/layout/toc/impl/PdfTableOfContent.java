package com.web4enterprise.pdf.layout.toc.impl;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.toc.TableOfContent;

public class PdfTableOfContent implements TableOfContent, PdfDocumentEmbeddable {
	protected int pageId;
	protected float linkX;
	protected float linkY;

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
	public float getHeight(Pager pager, float width) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void layout(Pager pager, Rect boundingBox, float startY,
			PageFootNotes pageFootNotes) {
		pageId = pager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
	}

	@Override
	public PdfDocumentEmbeddable clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Associate a level to a paragraph style.
	 * 
	 * @param level
	 * @param style
	 */
	public void addLevel(int level, ParagraphStyle style) {
		
	}
}
