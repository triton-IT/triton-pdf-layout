package com.web4enterprise.pdf.layout;

public class TextStyle {
	protected String fontName = "Times-Roman";
	protected int textSize = 12;
	protected boolean bold;
	protected boolean italic;
	protected boolean underline;
	
	public TextStyle() {
	}
	
	public TextStyle(int textSize) {
		this.textSize = textSize;
	}
	
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
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
