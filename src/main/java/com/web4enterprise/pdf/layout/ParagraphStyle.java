package com.web4enterprise.pdf.layout;

import static com.web4enterprise.pdf.core.font.Font.TIMES_ROMAN;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontStyle;
import com.web4enterprise.pdf.core.font.FontVariant;

public class ParagraphStyle extends TextStyle {
	protected Alignment alignment = Alignment.LEFT;
	protected float lineSpacing = 1.0f;
	protected Margins margins = new Margins(0, 0, 0, 0);
	protected int firstLineMargin = 0;
	
	public ParagraphStyle() {
		super(TIMES_ROMAN, 12);
	}
	
	public ParagraphStyle(Font font, int fontSize) {
		super(font, fontSize);
	}
	
	public ParagraphStyle(Font font, FontStyle style, int fontSize) {
		super(font, style, fontSize);
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public float getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	public Margins getMargins() {
		return margins;
	}

	public void setMargins(Margins margins) {
		this.margins = margins;
	}

	public int getFirstLineMargin() {
		return firstLineMargin;
	}

	public void setFirstLineMargin(int firstLineMargin) {
		this.firstLineMargin = firstLineMargin;
	}
	
	public FontVariant getFontVariant() {
		return font.getVariant(fontStyle);
	}
	
	/**
	 * Do not let null as default style but PLAIN.
	 */
	@Override
	public FontStyle getFontStyle() {
		return (fontStyle!=null)?fontStyle:FontStyle.PLAIN;
	}
}
