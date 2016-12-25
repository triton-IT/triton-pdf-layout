package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.page.PageFootNotes;

public class FootNote implements Element {
	private final static Logger LOGGER = Logger.getLogger(FootNote.class.getName());
	
	protected List<Element> elements = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;
	protected String id;
	
	public FootNote(Paragraph... paragraph) {
		if(paragraph != null) {
			elements.addAll(Arrays.asList(paragraph));
		}
	}
	
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
	
	public String generateId(Document document) {
		id = document.generateFootNoteId();
		return id;
	}
	
	public String getId() {
		if(id == null) {
			LOGGER.severe("FootNote id is asked but has not been generated.");
		}
		return id;
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
