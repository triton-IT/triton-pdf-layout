package com.web4enterprise.pdf.layout.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.core.path.StraightPath;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.paragraph.ElementLine;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphElement;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.placement.LineStyle;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.TableCell;
import com.web4enterprise.pdf.layout.table.TableCellStyle;
import com.web4enterprise.pdf.layout.table.TableRow;

public class Document {
	protected Pdf document = new Pdf();
	protected Page currentPage;
	protected PageStyle currentPageStyle;

	protected float blockStartX = 0;
	protected float blockStartY = 0;
	
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
	
	public float addParagraph(Paragraph paragraph, Rect boundingBox, float startY) {
		//Get all lines of text (composed of text of different style).
		List<ElementLine> textLines = paragraph.getElementLines();
		
		//Get the paragraph style which is the default style of text which compose it.
		ParagraphStyle paragraphStyle = paragraph.getStyle();

		float fontBaseLine = paragraphStyle.getFontVariant().getBaseLine(paragraphStyle.getFontSize());

		//Apply top margin to paragraph.
		float nextY = startY - paragraphStyle.getMargins().getTop();
		
		//Get this paragraph style. 
		int textSize = paragraphStyle.getFontSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		//Iterate of each line of text to display them.
		for(ElementLine textLine : textLines) {
			//Calculate the maximum size allowed for text.
			float maxWidth =  boundingBox.getWidth()
					- (paragraphStyle.getMargins().getLeft() + paragraphStyle.getMargins().getRight());
			float firstLineMaxWidth = maxWidth;

			if(isFirstLine) {
				firstLineMaxWidth -= paragraphStyle.getFirstLineMargin();
			}
			//Split text to get-in maximum space.
			List<ElementLine> elementSubLines = textLine.splitToMaxWidth(paragraphStyle, textSize, firstLineMaxWidth, maxWidth);
			
			boolean firstSubLine = true;
			for(ElementLine elementSubLine : elementSubLines) {
				//Calculate text base line.
				float baseLine = nextY - fontBaseLine;
				
				if(baseLine < boundingBox.getBottom()) {
					addPage();
					//Add page reinitialize blockStart, so calculate it again.
					baseLine = blockStartY - fontBaseLine;
					nextY =  blockStartY - paragraphStyle.getMargins().getTop();
				}
				
				blockStartX = 0.0f;
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
					float freeSpace = boundingBox.getWidth()
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
					Point elementSize = element.layout(currentPage, paragraphStyle, textSize, blockStartX, baseLine);
					blockStartX += elementSize.getX();
					
					//Keep greatest line spacing to not overlap elements of other lines.
					float currentLineSpacing = element.getLineSpacing(paragraphStyle);
					if(currentLineSpacing > lineSpacing) {
						lineSpacing = currentLineSpacing;
					}
				}
				nextY -= lineSpacing;
			}
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		//Apply bottom margin to paragraph.
		nextY -= paragraphStyle.getMargins().getBottom();
		
		return nextY;
	}
	
	public void addTable(Table table) {
		table.calculateInnerLayout();
		//FIXME: recalculate columns widths to fit page width.
		
		for(TableRow row : table.getRows()) {
			createRow(table, row);
		}
	}

	private void createRow(Table table, TableRow row) {
		int columnIndex = 0;
		
		float startX = currentPageStyle.getMargins().getLeft();
		
		float rowHeight = row.getHeight();
		if(blockStartY - rowHeight < currentPageStyle.getMargins().getBottom()) {
			addPage();
			if(table.isRepeatHeaderOnNewPage()) {
				for(int i = 0; i < table.getNbHeaderRows(); i++) {
					createRow(table, table.getRows().get(i));
				}
			}
		}
		
		for(TableCell cell : row.getCells()) {
			TableCellStyle cellStyle = cell.getStyle();

			float bottom = blockStartY - row.getHeight();
			
			//Fill background color.
			if(cellStyle.getBackgroundColor() != null) {
				StraightPath path = new StraightPath(new Point(startX, blockStartY), 
						new Point(startX + table.getColumnWidth(columnIndex), blockStartY), 
						new Point(startX + table.getColumnWidth(columnIndex), bottom),
						new Point(startX, bottom));
				path.setStroked(false);
				path.setFilled(true);
				path.setClosed(true);
				path.setFillColor(cellStyle.getBackgroundColor());
				currentPage.addPath(path);
			}
			
			float paragraphStartY = blockStartY;
			for(Paragraph paragraph : cell.getParagraphs()) {					
				addParagraph(paragraph, new Rect(paragraphStartY, startX, paragraphStartY - row.getHeight(), startX + table.getColumnWidth(columnIndex)), paragraphStartY);
				paragraphStartY -= paragraph.getHeight(table.getColumnWidth(columnIndex));
			}
			
			//Top
			if(cellStyle.getTopBorderStyle().width > 0 && cellStyle.getTopBorderStyle().lineStyle != LineStyle.NONE) {
				BorderStyle borderStyle = cellStyle.getTopBorderStyle();
				StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
						new Point(startX - cellStyle.getLeftBorderStyle().width / 2, blockStartY), 
						new Point(startX + table.getColumnWidth(columnIndex) + cellStyle.getRightBorderStyle().width / 2, blockStartY));
				currentPage.addPath(path);
			}
			
			//Left
			if(cellStyle.getLeftBorderStyle().width > 0 && cellStyle.getLeftBorderStyle().lineStyle != LineStyle.NONE) {
				BorderStyle borderStyle = cellStyle.getLeftBorderStyle();
				StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
						new Point(startX, blockStartY - cellStyle.getTopBorderStyle().width / 2), 
						new Point(startX, bottom + cellStyle.getBottomBorderStyle().width / 2));
				currentPage.addPath(path);
			}
			
			//Bottom
			if(cellStyle.getBottomBorderStyle().width > 0 && cellStyle.getBottomBorderStyle().lineStyle != LineStyle.NONE) {
				BorderStyle borderStyle = cellStyle.getBottomBorderStyle();
				StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
						new Point(startX - cellStyle.getLeftBorderStyle().width / 2, bottom), 
						new Point(startX + table.getColumnWidth(columnIndex) + cellStyle.getRightBorderStyle().width / 2, bottom));
				currentPage.addPath(path);
			}
			
			//Right
			if(cellStyle.getRightBorderStyle().width > 0 && cellStyle.getRightBorderStyle().lineStyle != LineStyle.NONE) {
				BorderStyle borderStyle = cellStyle.getRightBorderStyle();
				StraightPath path = new StraightPath(borderStyle.getWidth(), borderStyle.getColor(),
						new Point(startX + table.getColumnWidth(columnIndex), blockStartY - cellStyle.getTopBorderStyle().width / 2), 
						new Point(startX + table.getColumnWidth(columnIndex), bottom + cellStyle.getBottomBorderStyle().width / 2));
				currentPage.addPath(path);
			}
			
			startX += table.getColumnWidth(columnIndex);
			columnIndex++;
		}
		blockStartY -= rowHeight;
	}

	public Image createImage(InputStream imageStream) throws PdfGenerationException {
		return new Image(document.createImage(imageStream));
	}
	
	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
