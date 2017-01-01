package com.web4enterprise.pdf.layout.paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.utils.CompositeList;
import com.web4enterprise.pdf.layout.utils.CompositeList.CompositeListIterator;

public class Paragraph implements Element {
	protected ParagraphStyle style = null;
	protected List<Stop> stops = new ArrayList<>();
	//List of stops and theirs paragraph elements.
	protected CompositeList<ParagraphElement> elements = new CompositeList<>();
	protected int currentStop = 0;
	protected List<ParagraphElement> currentElements = new ArrayList<ParagraphElement>();

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
	public Paragraph(String... texts) {
		this(new ParagraphStyle(), texts);
	}
	
	public Paragraph(ParagraphStyle style, String... texts) {
		elements.addList(currentElements);
		this.style = style;
		for(String text : texts) {
			this.currentElements.add(new Text(text));
		}
	}
	
	public Paragraph(ParagraphElement... paragraphElements) {
		this(new ParagraphStyle(), paragraphElements);
	}
	
	public Paragraph(ParagraphStyle style, ParagraphElement... paragraphElements) {
		elements.addList(currentElements);
		this.style = style;
		this.currentElements.addAll(Arrays.asList(paragraphElements));
	}
	
	/**
	 * Used only for cloning.
	 * 
	 * @param style The style of paragraph.
	 * @param elements The elements to reference.
	 */
	private Paragraph(ParagraphStyle style, CompositeList<ParagraphElement> elements) {
		this.style = style;
		this.elements = elements;
	}
	
	public CompositeList<ParagraphElement> getElements() {
		return elements;
	}
	
	public void prependElement(ParagraphElement... elements) {
		this.elements.getList(0).addAll(0, Arrays.asList(elements));
	}
	
	public void addElement(ParagraphElement... elements) {
		this.currentElements.addAll(Arrays.asList(elements));
	}
	
	public ParagraphStyle getStyle() {
		return style;
	}

	public void setStyle(ParagraphStyle style) {
		this.style = style;
	}
	
	public List<ElementLine> getElementLines() {
		return ElementLine.getElementLines(elements);
	}
	
	public List<ElementLine> getElementLines(Document document, float maxWidth) {
		List<ElementLine> elementSubLines = new ArrayList<>();
		
		boolean isFirstLine = true;		
		for(ElementLine elementLine : getElementLines()) {
			float firstLineMaxWidth = maxWidth;
	
			if(isFirstLine) {
				firstLineMaxWidth -= getStyle().getFirstLineMargin();
			}
			
			//Split text to maximum space.
			elementSubLines.addAll(elementLine.splitToMaxWidth(document, getStyle(), getStyle().getFontSize(), firstLineMaxWidth, maxWidth, stops));
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		return elementSubLines;
	}
	
	public void addStop(Stop stop) {
		stops.add(stop);
		elements.addList(new ArrayList<ParagraphElement>());
	}
	
	public void nextStop(String... texts) {
		currentStop++;
		currentElements = elements.getList(currentStop);
		for(String text : texts) {
			currentElements.add(new Text(text));
		}
	}
	
	public void nextStop(ParagraphElement... elements) {
		currentStop++;
		currentElements = this.elements.getList(currentStop);
		currentElements.addAll(Arrays.asList(elements));
	}
	
	@Override
	public float getHeight(Document document, float width) {
		float height = 0;
		
		for(ElementLine elementLine : getElementLines(document, width)) {
			height += elementLine.getHeight(getStyle());
		}
		
		height += getStyle().getMargins().getTop() + getStyle().getMargins().getBottom();
		
		return height;
	}

	@Override
	public float layout(Document document, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		//Get all lines of text (composed of text of different style).
		List<ElementLine> elementLines = getElementLines();
		
		//Get the paragraph style which is the default style of text which compose it.
		ParagraphStyle paragraphStyle = getStyle();

		float fontBaseLine = paragraphStyle.getFontVariant().getBaseLine(paragraphStyle.getFontSize());

		//Apply top margin to paragraph.
		float nextY = startY - paragraphStyle.getMargins().getTop();
		
		//Get this paragraph style. 
		float textSize = paragraphStyle.getFontSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		
		//Iterate of each line of text to display them.
		for(ElementLine textLine : elementLines) {
			//Calculate the maximum size allowed for text.
			float maxWidth =  boundingBox.getWidth()
					- (paragraphStyle.getMargins().getLeft() + paragraphStyle.getMargins().getRight());
			float firstLineMaxWidth = maxWidth;

			if(isFirstLine) {
				firstLineMaxWidth -= paragraphStyle.getFirstLineMargin();
			}
			//Create a list of stops with relative positioning (by removing left borders).
			List<Stop> relativeStops = new ArrayList<>();
			for(Stop stop : stops) {
				Stop relativeStop = new Stop(stop.getType(), stop.getPosition() - boundingBox.getLeft() - paragraphStyle.getMargins().getLeft());
				if(isFirstLine) {
					relativeStop.setPosition(relativeStop.getPosition() - paragraphStyle.getFirstLineMargin());
				}
				relativeStops.add(relativeStop);
			}
			//Split text to get-in maximum space.
			List<ElementLine> elementSubLines = textLine.splitToMaxWidth(document, paragraphStyle, textSize, firstLineMaxWidth, maxWidth, relativeStops);
			
			boolean firstSubLine = true;
			for(ElementLine elementSubLine : elementSubLines) {
				//Calculate text base line.
				float baseLine = nextY - fontBaseLine;

				//Calculate page bottom with already added footnotes  
				float bottom = boundingBox.getBottom();
				float existingFootNotesHeight = 0.0f;
				if(pageFootNotes != null) {
					existingFootNotesHeight = pageFootNotes.getHeight(document, pageFootNotes.getWidth());
					
					//FootNote must be rendered on same page than line, add height of the footNote to the bottom of page.
					for(ParagraphElement paragraphElement : elementSubLine) {
						for(FootNote footNote : paragraphElement.getFootNotes()) {
							bottom += footNote.getHeight(document, pageFootNotes.getWidth());
						}
					}
				}
				
				//If text and its footNote does not fit in page, add a new page.
				if(baseLine < bottom + existingFootNotesHeight) {
					document.addPage();
					startY = document.getCurrentStartY();
					//Add page reinitialize blockStart, so calculate it again.
					baseLine = startY - fontBaseLine;
					nextY =  startY - paragraphStyle.getMargins().getTop();
				} else {
					//if it fits in page, add existing foot notes height to this page.
					bottom += existingFootNotesHeight;
				}

				//Add footNotes of the line to page.
				if(pageFootNotes != null) {
					for(ParagraphElement paragraphElement : elementSubLine) {
						for(FootNote footNote : paragraphElement.getFootNotes()) {
							pageFootNotes.addElement(footNote);
						}
					}
					pageFootNotes.compute(document, pageFootNotes.getWidth());
				}
				
				//If this is the first line, set the link.
				if(pageId == null) {
					pageId = document.getCurrentPage().getId();
					linkX = boundingBox.getLeft();
					linkY = startY;
				}
				
				float startX = 0.0f;
				if(paragraphStyle.getAlignment() == Alignment.LEFT) {
					startX = boundingBox.getLeft() + paragraphStyle.getMargins().getLeft();
					if(isFirstLine) {
						startX += paragraphStyle.getFirstLineMargin();
					}
				} else if(paragraphStyle.getAlignment() == Alignment.RIGHT) {
					startX = boundingBox.getRight() 
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
					startX = boundingBox.getLeft()
							+ paragraphStyle.getMargins().getLeft()
							+ (freeSpace - elementSubLine.getWidth(paragraphStyle, textSize)) / 2;
				}
				
				if(isFirstLine) {
					isFirstLine = false;
				}
				
				if(firstSubLine) {
					firstSubLine = false;
				}

				//The index of current stop.
				int currentStopIndex = 0;
				float lineSpacing = 0;
				//Iterate over each paragraphElement to display new lines.
				CompositeListIterator paragraphIterator = elementSubLine.iterator();
				while(paragraphIterator.hasNext()) {
					ParagraphElement paragraphElement = (ParagraphElement) paragraphIterator.next();

					//If list has changed, it means that a stop has been inserted. So, go to next stop for X position.
					if(paragraphIterator.hasListChanged()) {
						//Get next stop position and increment its index accordingly.
						Stop currentStop = stops.get(currentStopIndex);
						currentStopIndex++;
						float stopX = currentStop.getPosition();

						switch(currentStop.getType()) {
						case LEFT:
							float paragraphWidth = paragraphElement.getWidth(paragraphStyle, textSize);;
							stopX -= paragraphWidth;
							break;
						case CENTER:
							paragraphWidth = paragraphElement.getWidth(paragraphStyle, textSize);
							stopX -= (paragraphWidth / 2.0f);
							break;
						default:
							break;
						}
						
						//If we already passed stop, do not increment position. Continue to write where we are.
						if(stopX > startX) {
							startX = stopX;
						}
					}
					
					Point elementSize = paragraphElement.layout(document.getCurrentPage(), paragraphStyle, textSize, startX, baseLine);
					startX += elementSize.getX();
					
					//Keep greatest line spacing to not overlap elements of other lines.
					float currentLineSpacing = paragraphElement.getLineSpacing(paragraphStyle);
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

	@Override
	public Paragraph clone() {
		int index = 0;
		CompositeList<ParagraphElement> listElementsClone = new CompositeList<>();
		
		while(index < elements.getLists().size()) {
			List<ParagraphElement> currentElements = elements.getList(index);
			//Start by cloning elements.
			List<ParagraphElement> elementsClones = new ArrayList<ParagraphElement>(currentElements.size());
		    for (ParagraphElement element : currentElements) {
		    	elementsClones.add(element.clone());
		    }
		    listElementsClone.addList(elementsClones);
			index++;
		}
	    
	    //Clone create a new Paragraph with clones.
		return new Paragraph(style, listElementsClone);
	}

	@Override
	public int getPage() {
		return pageId;
	}

	@Override
	public float getLinkX() {
		return linkX;
	}

	@Override
	public float getLinkY() {
		return linkY;
	}
}
