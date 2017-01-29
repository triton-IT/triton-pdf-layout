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
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.PdfPage;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.toc.TableOfContent;

/**
 * Implementation of {@link ParagraphEmbeddable} for a PDF.
 * 
 * 
 * @author Régis Ramillien
 */
public interface PdfParagraphEmbeddable extends ParagraphEmbeddable, Cloneable {
	/**
	 * Get the lines of this paragraph hembeddable.
	 * 
	 * @return The lines of this paragraph embeddable.
	 */
	List<PdfParagraphEmbeddable> getLines();
	/**
	 * Get the width of this paragraph embeddable.
	 * 
	 * @param defaultStyle The style to use if no other style has been defined.
	 * @param defaultFontSize The font size to use if no other style has been defined.
	 * @return The width of this paragraph embeddable.
	 */
	float getWidth(ParagraphStyle defaultStyle, float defaultFontSize);
	/**
	 * Split this embeddable into smaller embeddables.
	 * 
	 * @param pdfPager The pager to get page information from.
	 * @param defaultStyle The style to use if no other style has been defined.
	 * @param fontSize The text size to use if no other style has been defined.
	 * @param positionX The initial X position for this embeddable.
	 * @param firstLineMaxWidth The maximum of the first line.
	 * @param maxWidth The maximum width of other lines of this paragraph embeddable.
	 * @return The {@link SplitInformation} for this paragraph embeddable.
	 */
	SplitInformation split(PdfPager pdfPager, ParagraphStyle defaultStyle, float fontSize, float positionX, float firstLineMaxWidth, Float maxWidth);
	/**
	 * Layout this paragraph embeddable in a page.
	 * 
	 * @param pdfPage The page to render to.
	 * @param defaultStyle The style to use if no other style has been defined.
	 * @param defaultFontSize The text size to use if no other style has been defined.
	 * @param positionX The initial X position for this embeddable.
	 * @param positionY The initial Y position for this embeddable.
	 * @return The point where the layout of this paragraph embeddable finished.
	 */
	Point layOut(PdfPage pdfPage, ParagraphStyle defaultStyle, float defaultFontSize, float positionX, float positionY);
	/**
	 * Get the space between lines of this paragraph embeddable.
	 * 
	 * @param defaultStyle The style to use if no other style has been defined.
	 * @return The space between lines.
	 */
	float getLineSpacing(ParagraphStyle defaultStyle);
	/**
	 * Get the list of {@link FootNote} used in this paragraph embeddable.
	 * 
	 * @return The list of {@link FootNote} used.
	 */
	List<PdfFootNote> getFootNotes();
	/**
	 * Get the text which will be displayed on the {@link TableOfContent}.
	 * 
	 * @return The text to display on the {@link TableOfContent}.
	 */
	String getTOCText();
	/**
	 * Check if this paragraph embeddable is linked to a {@link PdfDocumentEmbeddable}.
	 * 
	 * @return true if a link exists, false otherwise.
	 */
	boolean isLinked();
	
	/**
	 * Defines information for splitting this paragraph embeddable.
	 * 
	 * 
	 * @author Régis Ramillien
	 */
	class SplitInformation {
		/**
		 * The initial X position.
		 */
		public float positionX;
		/**
		 * The list of {@link PdfParagraphEmbeddable}.
		 */
		public List<PdfParagraphEmbeddable> splitEmbeddables = new ArrayList<>();
	}
	
	/**
	 * Clone this paragraph embeddable.
	 * 
	 * @return The clone.
	 */
	PdfParagraphEmbeddable clone();
}
