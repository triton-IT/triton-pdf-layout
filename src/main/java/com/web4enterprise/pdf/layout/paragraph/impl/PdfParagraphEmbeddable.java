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
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.impl.Page;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;

public interface PdfParagraphEmbeddable extends ParagraphEmbeddable, Cloneable {
	List<PdfParagraphEmbeddable> getLines();
	float getWidth(ParagraphStyle defaultStyle, float defaultTextSize);
	SplitInformation split(PdfPager pdfPager, ParagraphStyle defaultStyle, float fontSize, float positionX, float firstLineMaxWidth, Float maxWidth);
	Point layOut(Page page, ParagraphStyle defaultStyle, float defaultFontSize, float positionX, float positionY);
	float getLineSpacing(ParagraphStyle defaultStyle);
	List<PdfFootNote> getFootNotes();
	String getTOCText();
	boolean isLinked();
	
	class SplitInformation {
		public float positionX;
		public List<PdfParagraphEmbeddable> splitEmbeddables = new ArrayList<>();
	}
	
	PdfParagraphEmbeddable clone();
}
