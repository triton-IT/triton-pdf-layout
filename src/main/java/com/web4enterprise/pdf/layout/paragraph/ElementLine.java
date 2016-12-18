package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.web4enterprise.pdf.layout.paragraph.ParagraphElement.SplitInformation;

/*
 * A TextLine is a line of different Texts without any line return.
 * A line of text can be composed of one or more Texts. Texts can have different styles.
 */
public class ElementLine extends ArrayList<ParagraphElement> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Convert a list of text to  a list of TextLine.
	 * List of text is parsed to search for new line in each Text. 
	 * Texts are added to TextLine elements until a new line is found.
	 * If a new line is found in a text, a new TextLine is created and following texts are added until new line.
	 * @param texts The elements to parse.
	 * @return The list of TextLine.
	 */
	public static List<ElementLine> getElementLines(List<ParagraphElement> elements) {
		List<ElementLine> elementLines = new ArrayList<>();
		ElementLine elementLine = new ElementLine();
		elementLines.add(elementLine);

		for(ParagraphElement element : elements) {
			List<ParagraphElement> currentTextLines = element.getLines();
			Iterator<ParagraphElement> iterator = currentTextLines.iterator();
			while(iterator.hasNext()) {
				ParagraphElement currentElement = iterator.next();
				elementLine.add(currentElement);
				if(iterator.hasNext()) {
					elementLine = new ElementLine();
					elementLines.add(elementLine);
				}				
			}
		}
		
		return elementLines;
	}
	
	public List<ElementLine> splitToMaxWidth(ParagraphStyle defaultStyle, int defaultFontSize, float firstLineMaxWidth, Float maxWidth) {
		List<ElementLine> elementLines = new ArrayList<>();
		
		int currentX = 0;
		ElementLine currentElementLine = new ElementLine();
		elementLines.add(currentElementLine);
		
		boolean isFirstLine = true;
		for(ParagraphElement element : this) {
			SplitInformation splitInformation = element.split(defaultStyle, defaultFontSize, currentX, firstLineMaxWidth, maxWidth);
			
			//Insert end of line on current line.
			Iterator<ParagraphElement> iterator = splitInformation.splitElements.iterator();
			currentElementLine.add(iterator.next());
			
			//Create new line for each new line.
			while(iterator.hasNext()) {
				if(isFirstLine) {
					isFirstLine = false;
				}
				currentElementLine = new ElementLine();
				elementLines.add(currentElementLine);
				
				currentX = 0;
				
				ParagraphElement splitElement = iterator.next();
				currentElementLine.add(splitElement);
			}
			
			currentX += splitInformation.positionX;
		}
		
		return elementLines;
	}

	public int getWidth(ParagraphStyle defaultStyle, int defaultTextSize) {
		int width = 0;
		
		for(ParagraphElement element : this) {
			width += element.getWidth(defaultStyle, defaultTextSize);
		}
		
		return width;
	}
	
	public float getHeight(ParagraphStyle paragraphStyle) {
		float lineSpacing = 0;
		for(ParagraphElement element : this) {
			//Keep greatest line spacing to not overlap elements of other lines.
			float currentLineSpacing = element.getLineSpacing(paragraphStyle);
			if(currentLineSpacing > lineSpacing) {
				lineSpacing = currentLineSpacing;
			}
		}
		return lineSpacing;
	}
}
