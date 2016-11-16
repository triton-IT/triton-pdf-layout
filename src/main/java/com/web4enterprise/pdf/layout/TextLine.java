package com.web4enterprise.pdf.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontVariant;
import com.web4enterprise.pdf.layout.Text.SplitInformation;

/*
 * A TextLine is a line of different Texts without any line return.
 * A line of text can be composed of one or more Texts. Texts can have different styles.
 */
public class TextLine extends ArrayList<Text> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Convert a list of text to  a list of TextLine.
	 * List of text is parsed to search for new line in each Text. 
	 * Texts are added to TextLine elements until a new line is found.
	 * If a new line is found in a text, a new TextLine is created and following texts are added until new line.
	 * @param texts The texts to parse.
	 * @return The list of TextLine.
	 */
	public static List<TextLine> getTextLines(List<Text> texts) {
		List<TextLine> textLines = new ArrayList<>();
		TextLine textLine = new TextLine();
		textLines.add(textLine);

		for(Text text : texts) {
			List<Text> currentTextLines = text.getLines();
			Iterator<Text> iterator = currentTextLines.iterator();
			while(iterator.hasNext()) {
				Text currentText = iterator.next();
				textLine.add(currentText);
				if(iterator.hasNext()) {
					textLine = new TextLine();
					textLines.add(textLine);
				}				
			}
		}
		
		return textLines;
	}
	
	protected List<TextLine> splitToMaxWidth(TextStyle defaultStyle, int fontSize, int firstLineMaxWidth, Integer maxWidth) {
		List<TextLine> textLines = new ArrayList<>();
		
		int currentPosition = 0;
		TextLine currentTextLine = new TextLine();
		textLines.add(currentTextLine);
		
		boolean isFirstLine = true;
		for(Text text : this) {
			TextStyle textStyle = text.getStyle();
			//Get font name between paragraph and text ones.
			Font currentFont = (text.style.getFont() != null)?text.style.getFont():defaultStyle.getFont();
			FontVariant currentFontVariant = currentFont.getVariant((text.style.getFontStyle() != null)?
					text.style.getFontStyle():defaultStyle.getFontStyle());
			//Get font size between paragraph and text ones.
			int currentTextSize = (textStyle.getFontSize() != null)?textStyle.getFontSize():fontSize;
			
			//Split the text on max width.
			SplitInformation splitInformation = text.new SplitInformation();
			text.split(currentFontVariant, currentTextSize, currentPosition, firstLineMaxWidth, maxWidth, splitInformation);
			
			//Insert end of line on current line.
			Iterator<Text> iterator = splitInformation.splitTexts.iterator();
			currentTextLine.add(iterator.next());
			
			//Create new line for each new line.
			while(iterator.hasNext()) {
				if(isFirstLine) {
					isFirstLine = false;
				}
				currentTextLine = new TextLine();
				textLines.add(currentTextLine);
				
				currentPosition = 0;
				
				Text splitText = iterator.next();
				currentTextLine.add(splitText);
			}
			
			currentPosition += splitInformation.newPosition;
		}
		
		return textLines;
	}

	public int getWidth(TextStyle defaultStyle, int textSize) {
		int width = 0;
		
		for(Text text : this) {
			Font currentFont = (text.style.getFont() != null)?text.style.getFont():defaultStyle.getFont();
			FontVariant currentFontVariant = currentFont.getVariant((text.style.getFontStyle() != null)?
					text.style.getFontStyle():defaultStyle.getFontStyle());
			int currentTextSize = (text.style.fontSize != null)?text.style.fontSize:textSize;
			
			width += currentFontVariant.getWidth(currentTextSize, text.string);
		}
		
		return width;
	}
}
