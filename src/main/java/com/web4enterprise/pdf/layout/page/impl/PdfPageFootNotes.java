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

package com.web4enterprise.pdf.layout.page.impl;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;

/**
 * Container for page foo-notes.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfPageFootNotes extends PdfEmbeddableContainer {
	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, PdfPageFootNotes pdfPageFootNotes) {
		pageNumber = pdfPager.getCurrentPageNumber();
		pageId = pdfPager.getCurrentPage().getCorePage().getId();
		linkX = boundingBox.getLeft();
		linkY = pdfPager.getCursorPosition().getY();
		
		for(PdfDocumentEmbeddable pdfDocumentEmbeddable : pdfDocumentEmbeddables) {
			//Need to clone element because header is repeated and changing any value of the element for a page will change it for each page.
			//We do not pass any footNote because it will end in a never ending loop because pageFootNotes call operations on itself.
			//Furthermore, a footNote cannot create another footNote, it will change the page layout and its too late for this.
			pdfDocumentEmbeddable.clone().layOut(pdfPager, boundingBox, null);
		}
	}
}
