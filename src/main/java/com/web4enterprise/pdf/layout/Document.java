package com.web4enterprise.pdf.layout;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.Font;
import com.web4enterprise.pdf.core.Page;
import com.web4enterprise.pdf.core.Pdf;
import com.web4enterprise.pdf.core.PdfGenerationException;

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
		List<String> textLines = paragraph.getTextLines();
		ParagraphStyle style = paragraph.getStyle();
		
		//Apply top margin to paragraph.
		blockStart -= style.margins.top;
		int textSize = style.getTextSize();
		
		boolean firstLine = true;
		for(String text : textLines) {
			
			List<String> textSubLines = splitToPageWidth(style.getFontName(), textSize, style.margins, style.firstLineMargin, text, firstLine);
			
			boolean firstSubLine = true;
			for(String textSubLine : textSubLines) {
//				blockStart -= textSize * style.lineSpacing;
				if(blockStart < currentPageStyle.margins.bottom) {
					addPage();
					//Add page reinitialize blockStart, so calculate it again.
					blockStart -= textSize / 2;
				}
				
				int x = currentPageStyle.getMargins().getLeft() + style.margins.left;
				if(firstLine) {
					x += style.firstLineMargin;
					firstLine = false;
				}
				if(firstSubLine) {
					firstSubLine = false;
				} else {
					//If we have split line, we must remove first whitespace which comes from previous subLine.
					textSubLine = textSubLine.substring(1);
				}
				currentPage.addText(x, blockStart, textSize, textSubLine);
				blockStart -= textSize * style.lineSpacing;
			}
			
			if(firstLine) {
				firstLine = false;
			}
		}
		
		//Cancel last line spacing because this is not a new line but end of paragraph.
//		blockStart += textSize * style.lineSpacing;
		
		//Apply bottom margin to paragraph.
		blockStart -= style.margins.bottom;
	}
	
	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
	
	protected List<String> splitToPageWidth(String fontName, int fontSize, Margins margins, int firstLineMargin, String text, boolean firstLine) {
		List<String> strings = new ArrayList<String>();
		splitToPageWidth(fontName, fontSize, margins, firstLineMargin, text, strings, firstLine);
		
		return strings;
	}
	
	protected void splitToPageWidth(String fontName, int fontSize, Margins margins, int firstLineMargin, String text, List<String> strings, boolean firstLine) {
		int index = getFullLineIndex(fontName, fontSize, margins, firstLineMargin, text, firstLine);
		if(index < text.length()) {
			strings.add(text.substring(0, index));
			splitToPageWidth(fontName, fontSize, margins, firstLineMargin, text.substring(index, text.length()), strings, false);
		} else {
			strings.add(text);
		}
	}
	
	protected int getFullLineIndex(String fontName, int fontSize, Margins margins, int firstLineMargin, String text, boolean firstLine) {
		int textWidth = Font.calculateWidth(fontName, fontSize, text);
		
		int pageMargins = currentPageStyle.getMargins().left + currentPageStyle.getMargins().right;
		int paragraphMargins = margins.left + margins.right;
		
		if(firstLine) {
			paragraphMargins += firstLineMargin;
		}
		
		if(textWidth > currentPage.getWidth() - pageMargins - paragraphMargins) {
			int firstLineIndex = text.lastIndexOf(' ');
			String shortedText = text.substring(0, firstLineIndex);
			return getFullLineIndex(fontName, fontSize, margins, firstLineMargin, shortedText, firstLine);
		}
		
		return text.length();
	}
}
