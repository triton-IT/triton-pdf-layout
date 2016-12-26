package com.web4enterprise.pdf.layout.page;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;

public class PageHeader implements Element {
	protected List<Element> elements = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
	public void addElement(Element element) {
		elements.add(element);
	}

	public void compute(Document document, float width) {
		height = 0.0f;
		for(Element element : elements) {
			height += element.getHeight(document, width);
		}
		computedWidth = width;
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
		pageId = document.getCurrentPage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
		
		for(Element element : elements) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			startY = element.clone().layout(document, boundingBox, startY, pageFootNotes);
		}
		
		return startY;
	}

	@Override
	public PageHeader clone() {
		//TODO: clone this.
		return this;
	}

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
}
