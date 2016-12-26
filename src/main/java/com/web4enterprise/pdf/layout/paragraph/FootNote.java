package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;

public class FootNote implements Element {
	private final static Logger LOGGER = Logger.getLogger(FootNote.class.getName());
	
	protected List<Paragraph> paragraphs = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;
	protected String id;
	
	public FootNote(Paragraph... paragraph) {
		if(paragraph == null) {
			LOGGER.info("A foot note is added but no description is assigned to it.");
		} else {
			paragraphs.addAll(Arrays.asList(paragraph));
			//Prepend the foot note identifier to first element of paragraph.
			Text footnoteIndex = new Text("");
			TextStyle footnoteIndexStyle = new TextStyle();
			footnoteIndexStyle.setScript(TextScript.SUPER);
			footnoteIndex.setStyle(footnoteIndexStyle);
			paragraphs.get(0).prependElement(footnoteIndex);
		}
	}
	
	public void addElement(Paragraph paragraph) {
		paragraphs.add(paragraph);
	}

	public void compute(Document document, float width) {
		height = 0.0f;
		if(paragraphs.size() > 0) {
			Paragraph firstParagraph = paragraphs.get(0);
			((Text) firstParagraph.getElements().get(0)).setString(getId());
			for(Paragraph paragraph : paragraphs) {
				height += paragraph.getHeight(document, width);
			}
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
		for(Paragraph paragraph : this.paragraphs) {
			startY = paragraph.layout(document, boundingBox, startY, pageFootNotes);
		}
		
		return startY;
	}

	@Override
	public FootNote clone() {
		//TODO: clone this.
		return this;
	}
}
