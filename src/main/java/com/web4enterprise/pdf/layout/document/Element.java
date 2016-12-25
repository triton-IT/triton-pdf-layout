package com.web4enterprise.pdf.layout.document;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.page.PageFootNotes;

public interface Element extends Cloneable {
	public float getHeight(Document document, float width);
	public float layout(Document document, Rect boundingBox, float startY, PageFootNotes pageFootNotes);
	
	public Element clone();
}
