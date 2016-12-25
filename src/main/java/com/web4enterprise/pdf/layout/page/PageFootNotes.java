package com.web4enterprise.pdf.layout.page;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;

public class PageFootNotes implements Element {
	protected List<Element> elements = new ArrayList<>();
	protected float width = 0.0f;
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;
	
	public void addElement(Element element) {
		elements.add(element);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public void clear() {
		elements.clear();
	}

	public void compute(Document document, float width) {
		height = 0.0f;
		for(Element element : elements) {
			height += element.getHeight(document, width);
		}
		computedWidth = width;
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	@Override
	public float getHeight(Document document, float width) {
		if(computedWidth != width) {
			compute(document, width);
		}
		
		return height;
	}

	@Override
	public float layout(Document document, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		for(Element element : elements) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			startY = element.clone().layout(document, boundingBox, startY, pageFootNotes);
		}
		
		return startY;
	}

	@Override
	public PageFootNotes clone() {
		//TODO: clone this.
		return this;
	}	
}
