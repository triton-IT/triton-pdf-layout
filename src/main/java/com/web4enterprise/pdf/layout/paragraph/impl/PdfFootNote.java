package com.web4enterprise.pdf.layout.paragraph.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;

public class PdfFootNote implements FootNote, PdfDocumentEmbeddable {
	private static final Logger LOGGER = Logger.getLogger(PdfFootNote.class.getName());
	
	protected List<PdfParagraph> paragraphs = new ArrayList<>();
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;
	protected String id;

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
	public PdfFootNote(PdfParagraph... paragraphs) {
		
		if(paragraphs.length != 0) {
			this.paragraphs.addAll(Arrays.asList(paragraphs));
		}
	}

	@Override
	public void addEmbeddable(Paragraph paragraph) {
		paragraphs.add((PdfParagraph) paragraph);
	}

	public void compute(PdfPager pdfPager, float width) {
		//Prepend the foot note identifier to first element of paragraph.
		//This can't be done in constructor because paragraphs can be added later and we need to get the first paragraph style.
		PdfText footnoteIndex = new PdfText(getId() + " ");
		TextStyle footnoteIndexStyle = new TextStyle();
		footnoteIndexStyle.setScript(TextScript.SUPER);
		footnoteIndex.setStyle(footnoteIndexStyle);
		
		height = 0.0f;
		if(paragraphs.size() > 0) {
			paragraphs.get(0).prependEmbeddable(footnoteIndex);

			for(PdfParagraph paragraph : paragraphs) {
				height += paragraph.getHeight(pdfPager, width);
			}
		} else {
			LOGGER.warning("A footnote has been added without note.");
			paragraphs.add(new PdfParagraph(footnoteIndex));
		}
		
		computedWidth = width;
	}
	
	public String generateId(PdfPager pdfPager) {
		id = pdfPager.getCurrentPage().generateFootNoteId();
		return id;
	}
	
	public String getId() {
		if(id == null) {
			LOGGER.severe("FootNote id is asked but has not been generated.");
		}
		return id;
	}
	
	@Override
	public float getHeight(PdfPager pdfPager, float width) {
		if(computedWidth != width) {
			compute(pdfPager, width);
		}
		return height;
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		startY = boundingBox.getBottom() + height;
		
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = startY;
		
		for(PdfParagraph paragraph : this.paragraphs) {
			paragraph.layOut(pdfPager, boundingBox, startY, pageFootNotes);
		}
	}

	@Override
	public PdfFootNote clone() {
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
