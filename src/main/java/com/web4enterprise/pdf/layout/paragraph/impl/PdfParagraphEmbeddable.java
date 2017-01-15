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
	
	class SplitInformation {
		public float positionX;
		public List<PdfParagraphEmbeddable> splitEmbeddables = new ArrayList<>();
	}
	
	PdfParagraphEmbeddable clone();
}
