package com.web4enterprise.pdf.layout;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.Color;
import com.web4enterprise.pdf.core.Page;
import com.web4enterprise.pdf.core.Point;
import com.web4enterprise.pdf.core.StraightPath;
import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontVariant;

public class Text implements ParagraphElement {
	public static final String NEW_LINE = "\n";	
	public static final Text NEW_TEXT_LINE = new Text(NEW_LINE);

	protected TextStyle style = new TextStyle();
	protected String string;
	
	public Text(String string) {
		this.string = string;
	}
	
	public Text(TextStyle style, String string) {
		this.style = style;
		this.string = string;
	}

	public TextStyle getStyle() {
		return style;
	}

	public void setStyle(TextStyle style) {
		this.style = style;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
	public List<ParagraphElement> getLines() {
		ArrayList<ParagraphElement> lines = new ArrayList<>();

		//If string starts with a new line, we have to create an empty one.
		if(string.startsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		//Split will return only one entry if regex if at start or end of string.
		String[] stringLines = string.split(NEW_LINE);
		for(String stringLine : stringLines) {
			lines.add(new Text(style, stringLine));
		}
		
		//If string ends with a new line, we have to create an empty one.
		if(string.endsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		return lines;
	}
	
	public SplitInformation split(ParagraphStyle defaultStyle, int fontSize, int positionX, int firstLineMaxWidth, Integer maxWidth) {
		TextStyle textStyle = getStyle();
		//Get font name between paragraph and text ones.
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		//Get font size between paragraph and text ones.
		int currentTextSize = (textStyle.getFontSize() != null)?textStyle.getFontSize():fontSize;
		
		//Split the text on max width.
		SplitInformation splitInformation = new SplitInformation();
		split(string, currentFontVariant, currentTextSize, positionX, firstLineMaxWidth, maxWidth, splitInformation, true);
		
		return splitInformation;
	}
	
	public void split(String stringToSplit, FontVariant font, int fontSize, int positionX, int firstLineMaxWidth, 
			int maxWidth, SplitInformation splitInformation, boolean isFirstLine) {
		int textWidth = font.getWidth(fontSize, stringToSplit);
		String keptString = stringToSplit;
		
		int currentMaxWidth = maxWidth;
		if(isFirstLine) {
			currentMaxWidth = firstLineMaxWidth;
		}
		//If we have to split.
		while(textWidth > currentMaxWidth - positionX) {
			//Get index of split
			int splitIndex = keptString.lastIndexOf(' ');
			//If we can split, try to split again. We test against 0 in addition to -1 because first character can be a space and then string cannot be split anymore.
			if(splitIndex != 0 && splitIndex != -1) {
				keptString = stringToSplit.substring(0, splitIndex);
				textWidth = font.getWidth(fontSize, keptString);
			}
		}
		
		//Add information on current line to split information.
		splitInformation.splitElements.add(new Text(style, keptString));
		splitInformation.positionX = textWidth;
		
		//Try to split the text left.
		if(stringToSplit.length() != keptString.length()) {
			String textLeft = stringToSplit.substring(keptString.length());
			//We split on space earlier, we don't want to display it on new line, so remove it.
			split(textLeft.substring(1), font, fontSize, 0, firstLineMaxWidth, maxWidth, splitInformation, false);
		}
	}
	
	public int getWidth(ParagraphStyle defaultStyle, int defaultTextSize) {
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		int currentTextSize = (style.fontSize != null)?style.fontSize:defaultTextSize;
		
		return currentFontVariant.getWidth(currentTextSize, string);
	}
	
	public Point layout(Page page, ParagraphStyle defaultStyle, int defaultFontSize, int positionX, int positionY) {
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		int currentFontSize = (style.fontSize != null)?style.fontSize:defaultFontSize;
		
		Color color = (style.getFontColor() != null)?style.getFontColor():defaultStyle.getFontColor();
		
		page.addText(positionX, positionY, currentFontSize, currentFontVariant, color, string);
		
		Boolean isUnderlined = (style.isUnderlined != null)?style.isUnderlined:defaultStyle.isUnderlined();
		
		int width = currentFontVariant.getWidth(currentFontSize, string);
		if(isUnderlined != null && isUnderlined) {
			int underlineStartX = positionX;
			positionX += width;
			int underlineEndX = positionX;
			int underlineY = (int) (positionY - ((float) currentFontSize) / 12);
			
			StraightPath line = new StraightPath(new Point(underlineStartX, underlineY), new Point(underlineEndX, underlineY));
			line.setLineWidth(currentFontSize / 20);
			line.setStrokeColor(color);
			page.addPath(line);
		} else {
			positionX += width;
		}
		
		return new Point(width, currentFontSize);
	}

	@Override
	public float getLineSpacing(ParagraphStyle defaultStyle) {
		return defaultStyle.fontSize * defaultStyle.lineSpacing;
	}
}
