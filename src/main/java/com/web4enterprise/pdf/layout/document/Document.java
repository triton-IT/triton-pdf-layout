package com.web4enterprise.pdf.layout.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.core.path.StraightPath;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphElement;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.paragraph.ElementLine;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.LineStyle;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.TableCell;
import com.web4enterprise.pdf.layout.table.TableCellStyle;
import com.web4enterprise.pdf.layout.table.TableRow;

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
		currentPage = document.createPage(currentPageStyle.getFormat().getWidth(), currentPageStyle.getFormat().getHeight());
		blockStartY = currentPageStyle.getFormat().getHeight() - currentPageStyle.getMargins().getTop();
	}
	
	public void addPage(PageStyle pageStyle) {
		currentPageStyle = pageStyle;
		addPage();
	}
	
	public void addParagraph(Paragraph paragraph) {
		blockStartY = addParagraph(paragraph, new Rect(currentPageStyle.getMargins().getTop(), 
				currentPageStyle.getMargins().getLeft(),
				currentPageStyle.getMargins().getBottom(),
				currentPageStyle.getFormat().getWidth() - currentPageStyle.getMargins().getRight()), blockStartY);
	}
	
	public int addParagraph(Paragraph paragraph, Rect boundingBox, int startY) {
		//Get all lines of text (composed of text of different style).
		List<ElementLine> textLines = paragraph.getElementLines();
		
		//Get the paragraph style which is the default style of text which compose it.
		ParagraphStyle paragraphStyle = paragraph.getStyle();
		
		//Apply top margin to paragraph.
		int currentBlockStartY = startY - paragraphStyle.getMargins().getTop();
		
		//Get this paragraph style. 
		int textSize = paragraphStyle.getFontSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		//Iterate of each line of text to display them.
		for(ElementLine textLine : textLines) {
			//Calculate the maximum size allowed for text.
			int maxWidth =  boundingBox.getWidth()
					- (paragraphStyle.getMargins().getLeft() + paragraphStyle.getMargins().getRight());
			int firstLineMaxWidth = maxWidth;

			if(isFirstLine) {
				firstLineMaxWidth -= paragraphStyle.getFirstLineMargin();
			}
			//Split text to get-in maximum space.
			List<ElementLine> elementSubLines = textLine.splitToMaxWidth(paragraphStyle, textSize, firstLineMaxWidth, maxWidth);
			
			boolean firstSubLine = true;
			for(ElementLine elementSubLine : elementSubLines) {
				if(currentBlockStartY < boundingBox.getBottom()) {
					addPage();
					//Add page reinitialize blockStart, so calculate it again.
					currentBlockStartY = blockStartY - textSize / 2;
				}
				
				blockStartX = 0;
				if(paragraphStyle.getAlignment() == Alignment.LEFT) {
					blockStartX = boundingBox.getLeft() + paragraphStyle.getMargins().getLeft();
					if(isFirstLine) {
						blockStartX += paragraphStyle.getFirstLineMargin();
					}
				} else if(paragraphStyle.getAlignment() == Alignment.RIGHT) {
					blockStartX = boundingBox.getRight() 
							- paragraphStyle.getMargins().getRight()
							- elementSubLine.getWidth(paragraphStyle, textSize);
				} else if(paragraphStyle.getAlignment() == Alignment.CENTER) {
					//Calculate the maximum free space for paragraph.
					int freeSpace = boundingBox.getWidth()
							- paragraphStyle.getMargins().getRight()
							- paragraphStyle.getMargins().getLeft();
					if(isFirstLine) {
						freeSpace -= paragraphStyle.getFirstLineMargin();
					}
					
					// Then calculate the position based to left constraints and text centered on free space.
					blockStartX = boundingBox.getLeft()
							+ paragraphStyle.getMargins().getLeft()
							+ (freeSpace - elementSubLine.getWidth(paragraphStyle, textSize)) / 2;
				}
				
				if(isFirstLine) {
					isFirstLine = false;
				}
				
				if(firstSubLine) {
					firstSubLine = false;
				}
				
				float lineSpacing = 0;
				for(ParagraphElement element : elementSubLine) {
					Point elementSize = element.layout(currentPage, paragraphStyle, textSize, blockStartX, currentBlockStartY);
					blockStartX += elementSize.getX();
					
					//Keep greatest line spacing to not overlap elements of other lines.
					float currentLineSpacing = element.getLineSpacing(paragraphStyle);
					if(currentLineSpacing > lineSpacing) {
						lineSpacing = currentLineSpacing;
					}
				}
				currentBlockStartY -= lineSpacing;
			}
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		//Apply bottom margin to paragraph.
		currentBlockStartY -= paragraphStyle.getMargins().getBottom();
		
		return currentBlockStartY;
	}
	
	public void addTable(Table table) {
		table.calculateInnerLayout();
		//FIXME: recalculate columns widths to fit page length.
		
		for(TableRow row : table.getRows()) {
			int columnIndex = 0;
			//FIXME: Calculate real table cells bounds before rendering them.
			
			int startX = currentPageStyle.getMargins().getLeft();
			for(TableCell cell : row.getCells()) {
				TableCellStyle cellStyle = cell.getStyle();
				//Top
				if(cellStyle.getTopBorderStyle().width > 0 && cellStyle.getTopBorderStyle().lineStyle != LineStyle.NONE) {
					currentPage.addPath(new StraightPath(new Point(startX, blockStartY), 
							new Point(startX + table.getColumnWidth(columnIndex), blockStartY)));
				}
				
				//Left
				if(cellStyle.getLeftBorderStyle().width > 0 && cellStyle.getLeftBorderStyle().lineStyle != LineStyle.NONE) {
					currentPage.addPath(new StraightPath(new Point(startX, blockStartY), 
							new Point(startX, (int) (blockStartY + row.getHeight()))));
				}
				
				//Bottom
				if(cellStyle.getBottomBorderStyle().width > 0 && cellStyle.getBottomBorderStyle().lineStyle != LineStyle.NONE) {
					currentPage.addPath(new StraightPath(new Point(startX, (int) (blockStartY + row.getHeight())), 
							new Point(startX + table.getColumnWidth(columnIndex), (int) (blockStartY + row.getHeight()))));
				}
				
				//Right
				if(cellStyle.getRightBorderStyle().width > 0 && cellStyle.getRightBorderStyle().lineStyle != LineStyle.NONE) {
					currentPage.addPath(new StraightPath(new Point(startX + table.getColumnWidth(columnIndex), blockStartY), 
							new Point(startX + table.getColumnWidth(columnIndex), (int) (blockStartY + row.getHeight()))));
				}
				
				for(Paragraph paragraph : cell.getParagraphs()) {
					addParagraph(paragraph, new Rect(blockStartY, startX, blockStartY - row.getHeight(), startX + table.getColumnWidth(columnIndex)), blockStartY);
				}
				
				startX += table.getColumnWidth(columnIndex);
				columnIndex++;
			}
			blockStartY -= row.getHeight();
		}
	}

	public Image createImage(InputStream imageStream) throws PdfGenerationException {
		return new Image(document.createImage(imageStream));
	}
	
	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
