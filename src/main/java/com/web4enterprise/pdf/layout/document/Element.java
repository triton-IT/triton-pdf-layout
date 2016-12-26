package com.web4enterprise.pdf.layout.document;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.link.Linkable;
import com.web4enterprise.pdf.layout.page.PageFootNotes;

public interface Element extends Cloneable, Linkable {
	float getHeight(Document document, float width);
	float layout(Document document, Rect boundingBox, float startY, PageFootNotes pageFootNotes);
	
	Element clone();
}
