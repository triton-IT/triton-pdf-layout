package com.web4enterprise.pdf.layout;

import java.io.OutputStream;
import java.util.List;

import com.web4enterprise.pdf.core.Color;
import com.web4enterprise.pdf.core.Page;
import com.web4enterprise.pdf.core.Pdf;
import com.web4enterprise.pdf.core.PdfGenerationException;
import com.web4enterprise.pdf.core.Point;
import com.web4enterprise.pdf.core.StraightPath;
import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontVariant;

public class Document {
	protected Pdf document = new Pdf();
	protected Page currentPage;
	protected PageStyle currentPageStyle;

	protected int blockStart;
	
	public Document(PageStyle pageStyle) {
		addPage(pageStyle);
	}
	
	public void addPage() {
		currentPage = document.createPage(currentPageStyle.getFormat().width, currentPageStyle.getFormat().height);
		blockStart = currentPageStyle.format.height - currentPageStyle.margins.top;
	}
	
	public void addPage(PageStyle pageStyle) {
		currentPageStyle = pageStyle;
		addPage();
	}
	
	public void addParagraph(Paragraph paragraph) {
		//Get all lines of text (composed of text of different style).
		List<TextLine> textLines = paragraph.getTextLines();
		//Get the paragraph style which is the default style of text which compose it.
		ParagraphStyle paragraphStyle = paragraph.getStyle();
		
		//Apply top margin to paragraph.
		blockStart -= paragraphStyle.margins.top;
		
		//Get this paragraph style. 
		int textSize = paragraphStyle.getFontSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		//Iterate of each line of text to display them.
		for(TextLine textLine : textLines) {
			//Calculate the maximum size allowed for text.
			int maxWidth =  currentPageStyle.getFormat().width 
					- (currentPageStyle.getMargins().left + currentPageStyle.getMargins().right + paragraphStyle.margins.left + paragraphStyle.margins.right);
			int firstLineMaxWidth = maxWidth;

			if(isFirstLine) {
				firstLineMaxWidth -= paragraphStyle.firstLineMargin;
			}
			//Split text to get-in maximum space.
			List<TextLine> textSubLines = textLine.splitToMaxWidth(paragraphStyle, textSize, firstLineMaxWidth, maxWidth);
			
			boolean firstSubLine = true;
			for(TextLine textSubLine : textSubLines) {
				if(blockStart < currentPageStyle.margins.bottom) {
					addPage();
					//Add page reinitialize blockStart, so calculate it again.
					blockStart -= textSize / 2;
				}
				
				int x = 0;
				if(paragraphStyle.alignment == Alignment.LEFT) {
					x = currentPageStyle.getMargins().getLeft() + paragraphStyle.margins.left;
					if(isFirstLine) {
						x += paragraphStyle.firstLineMargin;
					}
				} else if(paragraphStyle.alignment == Alignment.RIGHT) {
					x = currentPageStyle.format.getWidth() 
							- currentPageStyle.getMargins().getRight() 
							- paragraphStyle.margins.right
							- textSubLine.getWidth(paragraphStyle, textSize);
				} else if(paragraphStyle.alignment == Alignment.CENTER) {
					//Calculate the maximum free space for paragraph.
					int freeSpace = currentPageStyle.format.getWidth() 
							- currentPageStyle.getMargins().getRight()
							- currentPageStyle.getMargins().getLeft()
							- paragraphStyle.margins.right
							- paragraphStyle.margins.left;
					if(isFirstLine) {
						freeSpace -= paragraphStyle.getFirstLineMargin();
					}
					
					// Then calculate the position based to left constraints and text centered on free space.
					x = currentPageStyle.getMargins().getLeft()
							+ paragraphStyle.margins.left
							+ (freeSpace - textSubLine.getWidth(paragraphStyle, textSize)) / 2;
					if(isFirstLine) {
						x += paragraphStyle.firstLineMargin;
					}
				}
				
				if(isFirstLine) {
					isFirstLine = false;
				}
				
				if(firstSubLine) {
					firstSubLine = false;
				} else {
					//If we have split line, we must remove first whitespace which comes from previous subLine.
					textSubLine.get(0).string = textSubLine.get(0).string.substring(1);
				}
				
				for(Text text : textSubLine) {
					Font currentFont = (text.style.getFont() != null)?text.style.getFont():paragraphStyle.getFont();
					FontVariant currentFontVariant = currentFont.getVariant((text.style.getFontStyle() != null)?
							text.style.getFontStyle():paragraphStyle.getFontStyle());
					int currentTextSize = (text.style.fontSize != null)?text.style.fontSize:textSize;
					
					Color color = (text.style.getFontColor() != null)?text.style.getFontColor():paragraphStyle.getFontColor();
					
					currentPage.addText(x, blockStart, currentTextSize, currentFontVariant, color, text.string);
					
					Boolean isUnderlined = (text.style.isUnderlined != null)?text.style.isUnderlined:paragraphStyle.isUnderlined();
					if(isUnderlined != null && isUnderlined) {
						int underlineStartX = x;
						x += currentFontVariant.getWidth(currentTextSize, text.string);
						int underlineEndX = x;
						int underlineY = blockStart - currentTextSize / 12;
						
						StraightPath line = new StraightPath(new Point(underlineStartX, underlineY), new Point(underlineEndX, underlineY));
						line.setLineWidth(currentTextSize / 20);
						currentPage.addPath(line);
					} else {
						x += currentFontVariant.getWidth(currentTextSize, text.string);
					}
				}
				blockStart -= textSize * paragraphStyle.lineSpacing;
			}
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		//Apply bottom margin to paragraph.
		blockStart -= paragraphStyle.margins.bottom;
	}
	
	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
