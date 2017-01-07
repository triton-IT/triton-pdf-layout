package com.web4enterprise.pdf.layout.toc;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.document.impl.Layouter;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;

public class TableOfContent implements Element {
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
	public float getHeight(Layouter layouter, float width) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void layout(Layouter layouter, Rect boundingBox, float startY,
			PageFootNotes pageFootNotes) {
		pageId = layouter.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
	}

	@Override
	public Element clone() {
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
