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

package com.web4enterprise.pdf.layout.image.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.impl.Page;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfFootNote;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;

/**
 * PDF Implementation of the Image API.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfImage implements Image, PdfParagraphEmbeddable {
	/**
	 * The core PDF image.
	 */
	protected com.web4enterprise.pdf.core.image.Image coreImage;
	/**
	 * The list of foot-notes of this image.
	 */
	protected List<PdfFootNote> footNotes = new ArrayList<>();
	/**
	 * The element linked to this image
	 */
	protected PdfDocumentEmbeddable linkedElement;
	/**
	 * Constructor from an existing image definition.
	 * 
	 * @param coreImage The existing image.
	 */
	public PdfImage(com.web4enterprise.pdf.core.image.Image coreImage) {
		this.coreImage = coreImage.cloneReference();
	}

	@Override
	public void setWidth(int width) {
		this.coreImage.setWidth(width);
	}

	@Override
	public void setWidth(int width, boolean keepRatio) {
		float oldWidth = this.coreImage.getWidth();
		this.coreImage.setWidth(width);
		if(keepRatio) {
			this.coreImage.setHeight((int) Math.round((width * this.coreImage.getHeight()) / oldWidth));
		}
	}

	@Override
	public void setHeight(int height) {
		this.coreImage.setHeight(height);
	}

	@Override
	public void setHeight(int height, boolean keepRatio) {
		float oldHeight = this.coreImage.getHeight();
		this.coreImage.setHeight(height);
		if(keepRatio) {
			this.coreImage.setWidth((int) Math.round((height * this.coreImage.getWidth()) / oldHeight));
		}
	}

	@Override
	public void setLink(DocumentEmbeddable documentEmbeddable) {
		linkedElement = (PdfDocumentEmbeddable) documentEmbeddable;
		coreImage.setLink(linkedElement);
	}

	@Override
	public void addFootNote(FootNote footNote) {
		footNotes.add((PdfFootNote) footNote);
	}

	@Override
	public void addFootNote(Paragraph... paragraphs) {
		footNotes.add(new PdfFootNote((PdfParagraph[]) paragraphs));
	}
	
	@Override
	public List<PdfParagraphEmbeddable> getLines() {
		List<PdfParagraphEmbeddable> lines = new ArrayList<>();
		lines.add(this);
		return lines;
	}	

	@Override
	public List<PdfFootNote> getFootNotes() {
		return footNotes;
	}
	
	@Override
	public float getWidth(ParagraphStyle defaultStyle, float defaultTextSize) {
		return getWidth();
	}
	
	@Override
	public SplitInformation split(PdfPager pdfPager, ParagraphStyle defaultStyle, float fontSize,
			float positionX, float firstLineMaxWidth, Float maxWidth) {
		SplitInformation splitInformation = new SplitInformation();
		
		List<PdfParagraphEmbeddable> lines = new ArrayList<>();
		
		//If image does not fit in left space (and if we are not at the start of a line).
		if(positionX != 0 && positionX + getWidth() > firstLineMaxWidth) {
			//Add a new line but with an invisible text to not change previous line shape.
			TextStyle textStyle = new TextStyle(Font.TIMES_ROMAN, 0);
			lines.add(new PdfText(textStyle, ""));
			positionX = 0;
		}
		
		lines.add(this);
		
		splitInformation.positionX = getWidth();
		splitInformation.splitEmbeddables = lines;
		return splitInformation;
	}
	
	@Override
	public Point layOut(Page page, ParagraphStyle defaultStyle,
			float defaultFontSize, float positionX, float positionY) {
		coreImage.setX(positionX);
		coreImage.setY(positionY);
		
		page.getCorePage().add(coreImage);
		
		return new Point(coreImage.getWidth(), coreImage.getHeight());
	}	

	@Override
	public float getLineSpacing(ParagraphStyle defaultStyle) {
		return defaultStyle.getFontSize() * defaultStyle.getLineSpacing();
	}
	
	@Override
	public PdfImage clone() {
		return new PdfImage(coreImage);
	}

	@Override
	public String getTOCText() {
		//Image is not supported for now in TOC.
		//TODO: Add a optional name to an image.
		return null;
	}
	
	@Override
	public boolean isLinked() {
		return linkedElement != null;
	}

	@Override
	public int getWidth() {
		return coreImage.getWidth();
	}

	@Override
	public int getHeight() {
		return coreImage.getHeight();
	}
}
