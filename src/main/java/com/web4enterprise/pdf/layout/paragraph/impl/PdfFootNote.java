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

import java.util.logging.Logger;

import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PdfEmbeddableContainer;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;

/**
 * Implements a foot-note for a PDF document
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfFootNote extends PdfEmbeddableContainer implements FootNote {
	/**
	 * Logger for class.
	 */
	private static final Logger LOGGER = Logger.getLogger(PdfFootNote.class.getName());
	/**
	 * The identifier of this foot-note.
	 */
	protected String id;
	
	/**
	 * Constructs a foot-note from a list of paragraphs.
	 * 
	 * @param paragraphs The paragraphs to add to this foot-note.
	 */
	public PdfFootNote(PdfParagraph... paragraphs) {		
		if(paragraphs.length != 0) {
			addEmbeddables(paragraphs);
		}
	}

	@Override
	public PdfFootNote clone() {
		//TODO: clone this.
		return this;
	}

	@Override
	public void compute(PdfPager pdfPager, float width) {
		//Prepend the foot note identifier to first element of paragraph.
		//This can't be done in constructor because paragraphs can be added later and we need to get the first paragraph style.
		PdfText footnoteIndex = new PdfText(getId() + " ");
		TextStyle footnoteIndexStyle = new TextStyle();
		footnoteIndexStyle.setScript(TextScript.SUPER);
		footnoteIndex.setStyle(footnoteIndexStyle);
		
		height = 0.0f;
		if(pdfDocumentEmbeddables.size() > 0) {
			//If first element is a paragraph, then we can prepend foot-note identifier on the same line.
			if(pdfDocumentEmbeddables.get(0) instanceof PdfParagraph) {
				((PdfParagraph) pdfDocumentEmbeddables.get(0)).prependEmbeddable(footnoteIndex);
			} else {
				//Else we insert the identifier at start of the element.
				pdfDocumentEmbeddables.add(0, new PdfParagraph(footnoteIndex));
			}

			for(PdfDocumentEmbeddable embeddable : pdfDocumentEmbeddables) {
				height += embeddable.getHeight(pdfPager, width);
			}
		} else {
			LOGGER.warning("A footnote has been added without note.");
			addEmbeddables(new PdfParagraph(footnoteIndex));
		}
		
		computedWidth = width;
	}
	
	/**
	 * Generate and set an identifier to this footNote.
	 * 
	 * @param pdfPager The pager to use for this footNote.
	 * @return The identifier of this footNote.
	 */
	public String generateId(PdfPager pdfPager) {
		id = pdfPager.getCurrentPage().generateFootNoteId();
		return id;
	}
	
	/**
	 * Return the identifier of this foot-note.
	 * 
	 * @return The identifier of this foot-note.
	 */
	public String getId() {
		if(id == null) {
			LOGGER.severe("FootNote id is asked but has not been generated.");
		}
		return id;
	}
}
