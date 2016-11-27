package com.web4enterprise.pdf.layout;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.page.Page;

public interface ParagraphElement {
	List<ParagraphElement> getLines();
	int getWidth(ParagraphStyle defaultStyle, int defaultTextSize);
	SplitInformation split(ParagraphStyle defaultStyle, int fontSize, int positionX, int firstLineMaxWidth, Integer maxWidth);
	Point layout(Page page, ParagraphStyle defaultStyle, int defaultFontSize, int positionX, int positionY);
	float getLineSpacing(ParagraphStyle defaultStyle);
	
	public class SplitInformation {
		public int positionX;
		List<ParagraphElement> splitElements = new ArrayList<>();
	}
}
