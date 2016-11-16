package com.web4enterprise.pdf.layout;

import com.web4enterprise.pdf.core.font.Font;

public class TextStyle {
	protected Font font;
	protected Integer textSize;
	protected boolean bold;
	protected boolean italic;
	protected boolean underline;
	
	public TextStyle() {
	}
	
	public TextStyle(Font font, int textSize) {
		this.font = font;
		this.textSize = textSize;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Integer getTextSize() {
		return textSize;
	}
	public void setTextSize(Integer textSize) {
		this.textSize = textSize;
	}
	public boolean isBold() {
		return bold;
	}
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	public boolean isItalic() {
		return italic;
	}
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	public boolean isUnderline() {
		return underline;
	}
	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	
}
