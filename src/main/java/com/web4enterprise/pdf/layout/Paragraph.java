package com.web4enterprise.pdf.layout;

import java.util.Arrays;
import java.util.List;

public class Paragraph {	
	protected ParagraphStyle style = new ParagraphStyle();
	protected List<String> textLines;
	
	public Paragraph(String... textLines) {
		this.textLines = Arrays.asList(textLines);
	}
	
	public Paragraph(ParagraphStyle style, String... textLines) {
		this.style = style;
		this.textLines = Arrays.asList(textLines);
	}
	
	public List<String> getTextLines() {
		return textLines;
	}
	
	public void addTextLine(String text) {
		textLines.add(text);
	}
	
	public ParagraphStyle getStyle() {
		return style;
	}
}
