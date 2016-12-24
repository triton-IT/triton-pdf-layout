package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.page.PageFootNotes;

public class FootNote implements Element {
	protected List<Element> elements = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;
	
	public FootNote(Paragraph... paragraph) {
		if(paragraph != null) {
			elements.addAll(Arrays.asList(paragraph));
		}
	}
	
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
		startY = boundingBox.getBottom() + height;
		for(Element element : this.elements) {
			startY = element.layout(document, boundingBox, startY, pageFootNotes);
		}
		
		return startY;
	}

	@Override
	public FootNote clone() {
		//TODO: clone this.
		return this;
	}
}
