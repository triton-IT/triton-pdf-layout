package com.web4enterprise.pdf.layout.paragraph.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;

public interface PdfParagraphEmbeddable extends ParagraphEmbeddable, Cloneable {
	List<PdfParagraphEmbeddable> getLines();
	float getWidth(ParagraphStyle defaultStyle, float defaultTextSize);
	SplitInformation split(Pager pager, ParagraphStyle defaultStyle, float fontSize, float positionX, float firstLineMaxWidth, Float maxWidth);
	Point layout(Page page, ParagraphStyle defaultStyle, float defaultFontSize, float positionX, float positionY);
	float getLineSpacing(ParagraphStyle defaultStyle);
	List<PdfFootNote> getFootNotes();
	
	class SplitInformation {
		public float positionX;
		public List<PdfParagraphEmbeddable> splitEmbeddables = new ArrayList<>();
	}
	
	PdfParagraphEmbeddable clone();
}
