package com.web4enterprise.pdf.layout;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.page.Page;

public class Document {
	protected Pdf document = new Pdf();
	protected Page currentPage;
	protected PageStyle currentPageStyle;

	protected int blockStartX = 0;
	protected int blockStartY = 0;
	
	public Document() {
		addPage(PageStyle.A4_PORTRAIT);
	}
	
	public Document(PageStyle pageStyle) {
		addPage(pageStyle);
	}
	
	public void addPage() {
		currentPage = document.createPage(currentPageStyle.getFormat().width, currentPageStyle.getFormat().height);
		blockStartY = currentPageStyle.format.height - currentPageStyle.margins.top;
	}
	
	public void addPage(PageStyle pageStyle) {
		currentPageStyle = pageStyle;
		addPage();
	}
	
	public void addParagraph(Paragraph paragraph) {
		//Get all lines of text (composed of text of different style).
		List<ElementLine> textLines = paragraph.getElementLines();
		//Get the paragraph style which is the default style of text which compose it.
		ParagraphStyle paragraphStyle = paragraph.getStyle();
		
		//Apply top margin to paragraph.
		blockStartY -= paragraphStyle.margins.top;
		
		//Get this paragraph style. 
		int textSize = paragraphStyle.getFontSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		//Iterate of each line of text to display them.
		for(ElementLine textLine : textLines) {
			//Calculate the maximum size allowed for text.
			int maxWidth =  currentPageStyle.getFormat().width 
					- (currentPageStyle.getMargins().left + currentPageStyle.getMargins().right + paragraphStyle.margins.left + paragraphStyle.margins.right);
			int firstLineMaxWidth = maxWidth;

			if(isFirstLine) {
				firstLineMaxWidth -= paragraphStyle.firstLineMargin;
			}
			//Split text to get-in maximum space.
			List<ElementLine> elementSubLines = textLine.splitToMaxWidth(paragraphStyle, textSize, firstLineMaxWidth, maxWidth);
			
			boolean firstSubLine = true;
			for(ElementLine elementSubLine : elementSubLines) {
				if(blockStartY < currentPageStyle.margins.bottom) {
					addPage();
					//Add page reinitialize blockStart, so calculate it again.
					blockStartY -= textSize / 2;
				}
				
				blockStartX = 0;
				if(paragraphStyle.alignment == Alignment.LEFT) {
					blockStartX = currentPageStyle.getMargins().getLeft() + paragraphStyle.margins.left;
					if(isFirstLine) {
						blockStartX += paragraphStyle.firstLineMargin;
					}
				} else if(paragraphStyle.alignment == Alignment.RIGHT) {
					blockStartX = currentPageStyle.format.getWidth() 
							- currentPageStyle.getMargins().getRight() 
							- paragraphStyle.margins.right
							- elementSubLine.getWidth(paragraphStyle, textSize);
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
					blockStartX = currentPageStyle.getMargins().getLeft()
							+ paragraphStyle.margins.left
							+ (freeSpace - elementSubLine.getWidth(paragraphStyle, textSize)) / 2;
					if(isFirstLine) {
						blockStartX += paragraphStyle.firstLineMargin;
					}
				}
				
				if(isFirstLine) {
					isFirstLine = false;
				}
				
				if(firstSubLine) {
					firstSubLine = false;
				}
				
				float lineSpacing = 0;
				for(ParagraphElement element : elementSubLine) {
					Point elementSize = element.layout(currentPage, paragraphStyle, textSize, blockStartX, blockStartY);
					blockStartX += elementSize.getX();
					
					//Keep greatest line spacing to not overlap elements of other lines.
					float currentLineSpacing = element.getLineSpacing(paragraphStyle);
					if(currentLineSpacing > lineSpacing) {
						lineSpacing = currentLineSpacing;
					}
				}
				blockStartY -= lineSpacing;
			}
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		//Apply bottom margin to paragraph.
		blockStartY -= paragraphStyle.margins.bottom;
	}

	public Image createImage(InputStream imageStream) throws PdfGenerationException {
		return new Image(document.createImage(imageStream));
	}
	
	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
