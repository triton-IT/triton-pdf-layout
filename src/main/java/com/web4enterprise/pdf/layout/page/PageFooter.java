package com.web4enterprise.pdf.layout.page;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;

public class PageFooter implements Element {
	protected List<Element> elements = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;
	
	public void addElement(Element element) {
		elements.add(element);
	}

	public void compute(float width) {
		height = 0.0f;
		for(Element element : elements) {
			height += element.getHeight(width);
		}
		computedWidth = width;
	}
	
	@Override
	public float getHeight(float width) {
		if(computedWidth != width) {
			compute(width);
		}
		
		return height;
	}

	@Override
	public float layout(Document document, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		for(Element element : this.elements) {
			//Need to clone element because footer is repeated and changing any value of the element for a page will change it for each page.
			startY = element.clone().layout(document, boundingBox, startY, pageFootNotes);
		}
		
		return startY;
	}

	@Override
	public PageFooter clone() {
		//TODO: clone this.
		return this;
	}
}
