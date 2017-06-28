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

import com.web4enterprise.pdf.core.link.Linkable;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.page.impl.PdfPageFootNotes;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.report.commons.geometry.Rect;

/**
 * An object embeddable in a document.
 * 
 * 
 * @author Régis Ramillien
 */
public abstract class PdfDocumentEmbeddable implements DocumentEmbeddable, Cloneable,
		Linkable {
	protected float width = 0.0f;
	protected float height = 0.0f;
	protected float computedWidth = 0.0f;

	protected Float linkX = null;
	protected Float linkY = null;
	protected Integer pageId = null;
	
	protected Integer pageNumber = null;

	@Override
	public Integer getPage() {
		return pageId;
	}

	@Override
	public Float getLinkX() {
		return linkX;
	}

	@Override
	public Float getLinkY() {
		return linkY;
	}

	@Override
	public Style getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTOCText() {
		//Header is not supported in TOC.
		return null;
	}
	
	@Override
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	/**
	 * Get height of embeddable.
	 * 
	 * @param pdfPager
	 *            The pager to get information from.
	 * @param width
	 *            The maximum available width for embeddable.
	 * @return The height of embeddable.
	 */
	public float getHeight(PdfPager pdfPager, float width) {
		if(computedWidth != width) {
			compute(pdfPager, width);
		}
		
		return height;
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Lay-out the embeddable based on best effort. The layout must be
	 * preferably done right on the first call. If a call to
	 * {@link #verifyLayOut} returns false, this method will be called again.
	 * This behavior will continue until the calls of {@link #verifyLayOut} for
	 * each embeddable returns true.
	 * 
	 * @param pdfPager
	 *            The pager to get information from.
	 * @param boundingBox
	 *            The bounding box available for component.
	 * @param pdfPageFootNotes
	 *            The foot-notes used of page.
	 */
	public void layOut(PdfPager pdfPager, Rect boundingBox, PdfPageFootNotes pdfPageFootNotes) {
		pageNumber = pdfPager.getCurrentPageNumber();
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
	}

	/**
	 * 
	 * Most embeddables are static, they do not need verification.
	 * 
	 * @param pdfPager The pager to get information from.
	 * @return {@code true} if embeddable is ok, {@code false} is embeddable
	 *         needs to be re-layed-out.
	 */
	public boolean verifyLayOut(PdfPager pdfPager) {
		return true;
	}

	@Override
	public PdfDocumentEmbeddable clone() {
		//TODO:
		return null;
	}
	
	/**
	 * 
	 * Compute height of embeddable for the given width.
	 * 
	 * @param pdfPager The pager to get information from.
	 * @param width The width to compute for.
	 */
	public abstract void compute(PdfPager pdfPager, float width);
}
