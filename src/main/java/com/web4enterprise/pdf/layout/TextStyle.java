package com.web4enterprise.pdf.layout;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontStyle;

public class TextStyle {
	protected Font font;
	protected FontStyle fontStyle;
	protected Integer fontSize;
	protected Boolean isUnderlined;
	
	public TextStyle() {
	}
	
	public TextStyle(Font font) {
		this.font = font;
	}
	
	public TextStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}
	
	public TextStyle(Font font, int fontSize) {
		this.font = font;
		this.fontSize = fontSize;
	}
	
	public TextStyle(Font font, FontStyle fontStyle, int fontSize) {
		this.font = font;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	
	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	
	public Boolean isUnderlined() {
		return isUnderlined;
	}
	
	public void setUnderlined(Boolean isUnderlined) {
		this.isUnderlined = isUnderlined;
	}
}
