/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.paragraph.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PdfPageFootNotes;
import com.web4enterprise.pdf.layout.page.impl.PdfEmbeddableContainer;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;

public class PdfFootNote extends PdfEmbeddableContainer implements FootNote {
	private static final Logger LOGGER = Logger.getLogger(PdfFootNote.class.getName());
	
	protected List<PdfParagraph> paragraphs = new ArrayList<>();
	protected String id;
	
	public PdfFootNote(PdfParagraph... paragraphs) {		
		if(paragraphs.length != 0) {
			this.paragraphs.addAll(Arrays.asList(paragraphs));
		}
	}

	@Override
	public void addEmbeddable(Paragraph paragraph) {
		paragraphs.add((PdfParagraph) paragraph);
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, PdfPageFootNotes pdfPageFootNotes) {
		super.layOut(pdfPager, boundingBox, pdfPageFootNotes);
		
		for(PdfParagraph paragraph : this.paragraphs) {
			paragraph.layOut(pdfPager, boundingBox, pdfPageFootNotes);
		}
	}

	@Override
	public PdfFootNote clone() {
		//TODO: clone this.
		return this;
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
}
