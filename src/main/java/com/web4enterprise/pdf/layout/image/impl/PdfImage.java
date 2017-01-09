package com.web4enterprise.pdf.layout.image.impl;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfFootNote;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;

public class PdfImage implements Image, PdfParagraphEmbeddable {	
	protected com.web4enterprise.pdf.core.image.Image coreImage;
	
	protected List<PdfFootNote> footNotes = new ArrayList<>();
	
	protected PdfDocumentEmbeddable linkedElement;
	
	public PdfImage(com.web4enterprise.pdf.core.image.Image coreImage) {
		this.coreImage = coreImage.cloneReference();
	}

	public int getWidth() {
		return coreImage.getWidth();
	}
	
	public void setWidth(int width) {
		this.coreImage.setWidth(width);
	}
	
	public void setWidth(int width, boolean keepRatio) {
		float oldWidth = this.coreImage.getWidth();
		this.coreImage.setWidth(width);
		if(keepRatio) {
			this.coreImage.setHeight((int) Math.round((width * this.coreImage.getHeight()) / oldWidth));
		}
	}
	
	public int getHeight() {
		return coreImage.getHeight();
	}
	
	public void setHeight(int height) {
		this.coreImage.setHeight(height);
	}
	
	public void setHeight(int height, boolean keepRatio) {
		float oldHeight = this.coreImage.getHeight();
		this.coreImage.setHeight(height);
		if(keepRatio) {
			this.coreImage.setWidth((int) Math.round((height * this.coreImage.getWidth()) / oldHeight));
		}
	}

	@Override
	public void setLink(DocumentEmbeddable documentEmbeddable) {
		linkedElement = (PdfDocumentEmbeddable) documentEmbeddable;
		coreImage.setLink(linkedElement);
	}

	@Override
	public void addFootNote(FootNote footNote) {
		footNotes.add((PdfFootNote) footNote);
	}

	@Override
	public void addFootNote(Paragraph... paragraphs) {
		footNotes.add(new PdfFootNote((PdfParagraph[]) paragraphs));
	}
	
	@Override
	public List<PdfParagraphEmbeddable> getLines() {
		List<PdfParagraphEmbeddable> lines = new ArrayList<>();
		lines.add(this);
		return lines;
	}	

	@Override
	public List<PdfFootNote> getFootNotes() {
		return footNotes;
	}
	
	@Override
	public float getWidth(ParagraphStyle defaultStyle, float defaultTextSize) {
		return getWidth();
	}
	
	@Override
	public SplitInformation split(Pager pager, ParagraphStyle defaultStyle, float fontSize,
			float positionX, float firstLineMaxWidth, Float maxWidth) {
		SplitInformation splitInformation = new SplitInformation();
		
		List<PdfParagraphEmbeddable> lines = new ArrayList<>();
		
		//If image does not fit in left space (and if we are not at the start of a line).
		if(positionX != 0 && positionX + getWidth() > firstLineMaxWidth) {
			//Add a new line but with an invisible text to not change previous line shape.
			TextStyle textStyle = new TextStyle(Font.TIMES_ROMAN, 0);
			lines.add(new PdfText(textStyle, ""));
			positionX = 0;
		}
		
		lines.add(this);
		
		splitInformation.positionX = getWidth();
		splitInformation.splitEmbeddables = lines;
		return splitInformation;
	}
	
	@Override
	public Point layout(Page page, ParagraphStyle defaultStyle,
			float defaultFontSize, float positionX, float positionY) {
		coreImage.setX(positionX);
		coreImage.setY(positionY);
		
		page.getCorePage().add(coreImage);
		
		return new Point(coreImage.getWidth(), coreImage.getHeight());
	}	

	@Override
	public float getLineSpacing(ParagraphStyle defaultStyle) {
		return defaultStyle.getFontSize() * defaultStyle.getLineSpacing();
	}
	
	@Override
	public PdfImage clone() {
		return new PdfImage(coreImage);
	}
}
