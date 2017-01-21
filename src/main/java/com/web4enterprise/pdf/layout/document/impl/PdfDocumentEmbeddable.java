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

public interface PdfDocumentEmbeddable extends DocumentEmbeddable, Cloneable, Linkable {
	float getHeight(PdfPager pdfPager, float width);
	
	void layOut(PdfPager pdfPager, Rect boundingBox, PageFootNotes pageFootNotes);
	
	/**
	 * Most embeddables are static, they do not need verification.
	 * 
	 * @param pdfPager
	 * @return
	 */
	default boolean verifyLayOut(PdfPager pdfPager) {
		return true;
	}
	
	PdfDocumentEmbeddable clone();
}
