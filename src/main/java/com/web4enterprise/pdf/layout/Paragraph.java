package com.web4enterprise.pdf.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Paragraph {	
	protected ParagraphStyle style = new ParagraphStyle();
	protected List<Text> texts = new ArrayList<>();
	
	public Paragraph(String... texts) {
		for(String text : texts) {
			this.texts.add(new Text(text));
		}
	}
	
	public Paragraph(ParagraphStyle style, String... texts) {
		this.style = style;
		for(String text : texts) {
			this.texts.add(new Text(text));
		}
	}
	
	public Paragraph(Text... texts) {
		this.texts.addAll(Arrays.asList(texts));
	}
	
	public Paragraph(ParagraphStyle style, Text... texts) {
		this.style = style;
		this.texts.addAll(Arrays.asList(texts));
	}
	
	public List<Text> getTexts() {
		return texts;
	}
	
	public void addText(Text text) {
		texts.add(text);
	}
	
	public ParagraphStyle getStyle() {
		return style;
	}

	public void setStyle(ParagraphStyle style) {
		this.style = style;
	}
	
	public List<TextLine> getTextLines() {
		return TextLine.getTextLines(texts);
	}
}
