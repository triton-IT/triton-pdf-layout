package com.web4enterprise.pdf.layout;

import java.io.OutputStream;
import java.util.List;

import com.web4enterprise.pdf.core.Page;
import com.web4enterprise.pdf.core.Pdf;
import com.web4enterprise.pdf.core.PdfGenerationException;
import com.web4enterprise.pdf.core.font.Font;

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
		ParagraphStyle style = paragraph.getStyle();
		
		//Apply top margin to paragraph.
		blockStart -= style.margins.top;
		
		//Get this paragraph style. 
		int textSize = style.getTextSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		//Iterate of each line of text to display them.
		for(TextLine textLine : textLines) {
			//Calculate the maximum size allowed for text.
			int maxWidth =  currentPageStyle.getFormat().width 
					- (currentPageStyle.getMargins().left + currentPageStyle.getMargins().right + style.margins.left + style.margins.right);
			int firstLineMaxWidth = maxWidth;

			if(isFirstLine) {
				firstLineMaxWidth -= style.firstLineMargin;
			}
			//Split text to get-in maximum space.
			List<TextLine> textSubLines = textLine.splitToMaxWidth(style.getFont(), textSize, firstLineMaxWidth, maxWidth);
			
			boolean firstSubLine = true;
			for(TextLine textSubLine : textSubLines) {
				if(blockStart < currentPageStyle.margins.bottom) {
					addPage();
					//Add page reinitialize blockStart, so calculate it again.
					blockStart -= textSize / 2;
				}
				
				int x = 0;
				if(style.alignment == Alignment.LEFT) {
					x = currentPageStyle.getMargins().getLeft() + style.margins.left;
					if(isFirstLine) {
						x += style.firstLineMargin;
					}
				} else if(style.alignment == Alignment.RIGHT) {
					x = currentPageStyle.format.getWidth() 
							- currentPageStyle.getMargins().getRight() 
							- style.margins.right
							- textSubLine.getWidth(style.getFont(), textSize);
				} else if(style.alignment == Alignment.CENTER) {
					//Calculate the maximum free space for paragraph.
					int freeSpace = currentPageStyle.format.getWidth() 
							- currentPageStyle.getMargins().getRight()
							- currentPageStyle.getMargins().getLeft()
							- style.margins.right
							- style.margins.left;
					if(isFirstLine) {
						freeSpace -= style.getFirstLineMargin();
					}
					
					// Then calculate the position based to left constraints and text centered on free space.
					x = currentPageStyle.getMargins().getLeft()
							+ style.margins.left
							+ (freeSpace - textSubLine.getWidth(style.getFont(), textSize)) / 2;
					if(isFirstLine) {
						x += style.firstLineMargin;
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
					Font currentFont = (text.style.font != null)?text.style.font:style.font;
					int currentTextSize = (text.style.textSize != null)?text.style.textSize:textSize;
					
					currentPage.addText(x, blockStart, currentTextSize, text.string);
					x += currentFont.getWidth(currentTextSize, text.string);
				}
				blockStart -= textSize * style.lineSpacing;
			}
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		//Apply bottom margin to paragraph.
		blockStart -= style.margins.bottom;
	}
	
	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
