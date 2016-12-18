package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web4enterprise.pdf.layout.text.Text;

public class Paragraph {	
	protected ParagraphStyle style = new ParagraphStyle();
	protected List<ParagraphElement> elements = new ArrayList<>();
	
	public Paragraph(String... texts) {
		for(String text : texts) {
			this.elements.add(new Text(text));
		}
	}
	
	public Paragraph(ParagraphStyle style, String... texts) {
		this.style = style;
		for(String text : texts) {
			this.elements.add(new Text(text));
		}
	}
	
	public Paragraph(ParagraphElement... elements) {
		this.elements.addAll(Arrays.asList(elements));
	}
	
	public Paragraph(ParagraphStyle style, ParagraphElement... elements) {
		this.style = style;
		this.elements.addAll(Arrays.asList(elements));
	}
	
	public List<ParagraphElement> getElements() {
		return elements;
	}
	
	public void addElement(ParagraphElement... elements) {
		this.elements.addAll(Arrays.asList(elements));
	}
	
	public ParagraphStyle getStyle() {
		return style;
	}

	public void setStyle(ParagraphStyle style) {
		this.style = style;
	}
	
	public List<ElementLine> getElementLines() {
		return ElementLine.getElementLines(elements);
	}
	
	public List<ElementLine> getElementLines(float maxWidth) {
		List<ElementLine> elementSubLines = new ArrayList<>();
		
		boolean isFirstLine = true;		
		for(ElementLine textLine : getElementLines()) {
			float firstLineMaxWidth = maxWidth;
	
			if(isFirstLine) {
				firstLineMaxWidth -= getStyle().getFirstLineMargin();
			}
			
			//Split text to get-in maximum space.
			elementSubLines.addAll(textLine.splitToMaxWidth(getStyle(), getStyle().getFontSize(), firstLineMaxWidth, maxWidth));
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		return elementSubLines;
	}
}
