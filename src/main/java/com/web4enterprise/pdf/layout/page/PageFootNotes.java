package com.web4enterprise.pdf.layout.page;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.document.impl.Layouter;

public class PageFootNotes implements Element {
	protected List<Element> elements = new ArrayList<>();
	protected float width = 0.0f;
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
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

	public void compute(Layouter layouter, float width) {
		height = 0.0f;
		for(Element element : elements) {
			height += element.getHeight(layouter, width);
		}
		computedWidth = width;
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	@Override
	public float getHeight(Layouter layouter, float width) {
		if(computedWidth != width) {
			compute(layouter, width);
		}
		
		return height;
	}

	@Override
	public void layout(Layouter layouter, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		pageId = layouter.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
		
		for(Element element : elements) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			//We do not pass any footNote because it will end in a never ending loop because pageFootNotes call operations on itself.
			//Furthermore, a footNote cannot create another footNote, it will change the page layouting and its too late for this.
			element.clone().layout(layouter, boundingBox, startY, null);
		}
	}

	@Override
	public PageFootNotes clone() {
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
