package com.web4enterprise.pdf.layout.document;

import com.web4enterprise.pdf.core.geometry.Rect;

public interface Element {
	public float getHeight(float width);
	public float layout(Document document, Rect boundingBox, float startY);
}
