package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.page.Page;

public interface ParagraphElement extends Cloneable {
	List<ParagraphElement> getLines();
	float getWidth(ParagraphStyle defaultStyle, float defaultTextSize);
	SplitInformation split(ParagraphStyle defaultStyle, float fontSize, float positionX, float firstLineMaxWidth, Float maxWidth);
	Point layout(Page page, ParagraphStyle defaultStyle, float defaultFontSize, float positionX, float positionY);
	float getLineSpacing(ParagraphStyle defaultStyle);
	
	class SplitInformation {
		public float positionX;
		public List<ParagraphElement> splitElements = new ArrayList<>();
	}
	
	ParagraphElement clone();
}
