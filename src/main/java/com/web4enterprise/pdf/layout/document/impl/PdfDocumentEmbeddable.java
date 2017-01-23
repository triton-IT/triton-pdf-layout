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

package com.web4enterprise.pdf.layout.document.impl;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.link.Linkable;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.page.impl.PageFootNotes;

/**
 * An object embeddable in a document.
 * 
 * 
 * @author Régis Ramillien
 */
public interface PdfDocumentEmbeddable extends DocumentEmbeddable, Cloneable,
		Linkable {
	/**
	 * Get height of embeddable.
	 * 
	 * @param pdfPager
	 *            The pager to get information from.
	 * @param width
	 *            The maximum available width for embeddable.
	 * @return The height of embeddable.
	 */
	float getHeight(PdfPager pdfPager, float width);

	/**
	 * Lay-out the embeddable based on best effort. The layout must be
	 * preferably done right on the first call. If a call to
	 * {@link #verifyLayOut} returns false, this method will be called again.
	 * This behavior will continue until the calls of {@link #verifyLayOut} for
	 * each embeddable returns true.
	 * 
	 * @param pdfPager
	 *            THe pager to get information from.
	 * @param boundingBox
	 *            The bounding box available for component.
	 * @param pageFootNotes
	 *            The foot-notes used of page.
	 */
	void layOut(PdfPager pdfPager, Rect boundingBox, PageFootNotes pageFootNotes);

	/**
	 * 
	 * Most embeddables are static, they do not need verification.
	 * 
	 * @param pdfPager
	 * @return {@code true} if embeddable is ok, {@code false} is embeddable
	 *         needs to be re-layed-out.
	 */
	default boolean verifyLayOut(PdfPager pdfPager) {
		return true;
	}

	/**
	 * Clone this embedable.
	 * 
	 * @return A clone to this embeddable.
	 */
	PdfDocumentEmbeddable clone();
}
