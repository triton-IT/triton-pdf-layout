package com.web4enterprise.pdf.layout;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.styling.Color;

public class TextStyle {
	protected Font font;
	protected FontsVariant fontStyle;
	protected Integer fontSize;
	protected Color fontColor;
	protected Boolean isUnderlined;
	protected Color underlineColor;
	
	public TextStyle() {
	}
	
	public TextStyle(Font font) {
		this.font = font;
	}
	
	public TextStyle(FontsVariant fontStyle) {
		this.fontStyle = fontStyle;
	}
	
	public TextStyle(Font font, int fontSize) {
		this.font = font;
		this.fontSize = fontSize;
	}
	
	public TextStyle(Font font, FontsVariant fontStyle, int fontSize) {
		this.font = font;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
	}
	
	public TextStyle(Font font, FontsVariant fontStyle, int fontSize, Color fontColor) {
		this.font = font;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	
	public FontsVariant getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FontsVariant fontStyle) {
		this.fontStyle = fontStyle;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	
	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Boolean isUnderlined() {
		return isUnderlined;
	}
	
	public void setUnderlined(Boolean isUnderlined) {
		this.isUnderlined = isUnderlined;
	}

	public Color getUnderlineColor() {
		return underlineColor;
	}

	public void setUnderlineColor(Color underlineColor) {
		this.underlineColor = underlineColor;
	}
}
