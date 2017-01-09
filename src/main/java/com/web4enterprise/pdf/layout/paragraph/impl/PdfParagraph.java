package com.web4enterprise.pdf.layout.paragraph.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;
import com.web4enterprise.pdf.layout.utils.CompositeList;
import com.web4enterprise.pdf.layout.utils.CompositeList.CompositeListIterator;

public class PdfParagraph implements Paragraph, PdfDocumentEmbeddable {
	protected ParagraphStyle style = null;
	protected List<Stop> stops = new ArrayList<>();
	//List of stops and theirs paragraph embeddables.
	protected CompositeList<PdfParagraphEmbeddable> embeddables = new CompositeList<>();
	protected int currentStop = 0;
	protected List<PdfParagraphEmbeddable> currentEmbeddables = new ArrayList<PdfParagraphEmbeddable>();

	protected float linkX = 0.0f;
	protected float linkY = 0.0f;
	protected Integer pageId = null;
	
	public PdfParagraph() {
		this((String[]) null);
	}
	
	public PdfParagraph(String... texts) {
		this(new ParagraphStyle(), texts);
	}
	
	public PdfParagraph(ParagraphStyle style) {
		this(style, (String[]) null);
	}
	
	public PdfParagraph(ParagraphStyle style, String... texts) {
		embeddables.addList(currentEmbeddables);
		this.style = style;
		if(texts != null) {
			for(String text : texts) {
				this.currentEmbeddables.add(new PdfText(text));
			}
		}
	}
	
	public PdfParagraph(PdfParagraphEmbeddable... paragraphEmbeddables) {
		this(new ParagraphStyle(), paragraphEmbeddables);
	}
	
	public PdfParagraph(ParagraphStyle style, PdfParagraphEmbeddable... paragraphEmbeddables) {
		embeddables.addList(currentEmbeddables);
		this.style = style;
		this.currentEmbeddables.addAll(Arrays.asList(paragraphEmbeddables));
	}
	
	/**
	 * Used only for cloning.
	 * 
	 * @param style The style of paragraph.
	 * @param embeddables The embeddables to reference.
	 */
	private PdfParagraph(ParagraphStyle style, CompositeList<PdfParagraphEmbeddable> embeddables) {
		this.style = style;
		this.embeddables = embeddables;
	}

	@Override
	public Image createImage(Image image) {
		return image.clone();
	}

	@Override
	public Image createImage(Image image, int width, int height) {
		Image clone = image.clone();
		
		clone.setWidth(width);
		clone.setHeight(height);
		
		return clone;
	}

	@Override
	public Image createImageForWidth(Image image, int width) {
		Image clone = image.clone();
		
		clone.setWidth(width, true);
		
		return clone;
	}

	@Override
	public Image createImageForHeight(Image image, int height) {
		Image clone = image.clone();
		
		clone.setHeight(height, true);
		
		return clone;
	}

	@Override
	public Text createText(String value) {
		return new PdfText(value);
	}

	@Override
	public Text[] createText(String... values) {
		Text[] texts;
		
		if(values != null) {
			texts = new Text[values.length];
			for(int i = 0; i < values.length; i++) {
				texts[i] = createText(values[i]);
			}
		} else {
			texts = new Text[0];
		}

		return texts;
	}

	@Override
	public Text createText(TextStyle style, String value) {
		return new PdfText(style, value);
	}

	@Override
	public Text[] createText(TextStyle style, String... values) {
		Text[] texts;
		
		if(values != null) {
			texts = new Text[values.length];
			for(int i = 0; i < values.length; i++) {
				texts[i] = createText(style, values[i]);
			}
		} else {
			texts = new Text[0];
		}

		return texts;
	}

	@Override
	public void addEmbeddable(ParagraphEmbeddable... embeddables) {
		for(ParagraphEmbeddable embeddable : embeddables) {
			currentEmbeddables.add((PdfParagraphEmbeddable) embeddable);
		}		
	}

	@Override
	public void setStyle(ParagraphStyle style) {
		this.style = style;
	}
	
	@Override
	public void addStop(Stop stop) {
		stops.add(stop);
		embeddables.addList(new ArrayList<PdfParagraphEmbeddable>());
	}

	@Override
	public void nextStop(String... values) {
		currentStop++;
		currentEmbeddables = embeddables.getList(currentStop);
		for(String value : values) {
			currentEmbeddables.add((PdfText) createText(value));
		}
	}

	@Override
	public void nextStop(PdfParagraphEmbeddable... embeddables) {
		currentStop++;
		currentEmbeddables = this.embeddables.getList(currentStop);
		currentEmbeddables.addAll(Arrays.asList(embeddables));
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
	
	@Override
	public float getHeight(Pager pager, float width) {
		float height = 0;
		
		for(ParagraphEmbeddableLine embeddableLine : getEmbeddableLines(pager, width)) {
			height += embeddableLine.getHeight(getStyle());
		}
		
		height += getStyle().getMargins().getTop() + getStyle().getMargins().getBottom();
		
		return height;
	}

	@Override
	public void layout(Pager pager, Rect boundingBox, float startY, PageFootNotes pageFootNotes) {
		//Get all lines of text (composed of text of different style).
		List<ParagraphEmbeddableLine> embeddableLines = getEmbeddableLines();
		
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
		for(ParagraphEmbeddableLine textLine : embeddableLines) {
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
			List<ParagraphEmbeddableLine> embeddableSubLines = textLine.splitToMaxWidth(pager, paragraphStyle, textSize, firstLineMaxWidth, maxWidth, relativeStops);
			
			boolean firstSubLine = true;
			for(ParagraphEmbeddableLine embeddableSubLine : embeddableSubLines) {
				//Calculate text base line.
				float baseLine = nextY - fontBaseLine;

				//Calculate page bottom with already added footnotes  
				float bottom = boundingBox.getBottom();
				float existingFootNotesHeight = 0.0f;
				if(pageFootNotes != null) {
					existingFootNotesHeight = pageFootNotes.getHeight(pager, pageFootNotes.getWidth());
					
					//FootNote must be rendered on same page than line, add height of the footNote to the bottom of page.
					for(PdfParagraphEmbeddable paragraphEmbeddable : embeddableSubLine) {
						for(PdfFootNote footNote : paragraphEmbeddable.getFootNotes()) {
							bottom += footNote.getHeight(pager, pageFootNotes.getWidth());
						}
					}
				}
				
				//If text and its footNote does not fit in page, add a new page.
				if(baseLine < bottom + existingFootNotesHeight) {
					pager.addPage();
					startY = pager.getCursorPosition().getY();
					//Add page reinitialize blockStart, so calculate it again.
					baseLine = startY - fontBaseLine;
					nextY =  startY - paragraphStyle.getMargins().getTop();
				} else {
					//if it fits in page, add existing foot notes height to this page.
					bottom += existingFootNotesHeight;
				}

				//Add footNotes of the line to page.
				if(pageFootNotes != null) {
					for(PdfParagraphEmbeddable paragraphEmbeddable : embeddableSubLine) {
						for(PdfFootNote footNote : paragraphEmbeddable.getFootNotes()) {
							pageFootNotes.addEmbeddable(footNote);
						}
					}
					pageFootNotes.compute(pager, pageFootNotes.getWidth());
				}
				
				//If this is the first line, set the link.
				if(pageId == null) {
					pageId = pager.getCurrentPage().getCorePage().getId();
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
							- embeddableSubLine.getWidth(paragraphStyle, textSize);
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
							+ (freeSpace - embeddableSubLine.getWidth(paragraphStyle, textSize)) / 2;
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
				//Iterate over each paragraphEmbeddable to display new lines.
				CompositeListIterator paragraphIterator = embeddableSubLine.iterator();
				while(paragraphIterator.hasNext()) {
					PdfParagraphEmbeddable paragraphEmbeddable = (PdfParagraphEmbeddable) paragraphIterator.next();

					//If list has changed, it means that a stop has been inserted. So, go to next stop for X position.
					if(paragraphIterator.hasListChanged()) {
						//Get next stop position and increment its index accordingly.
						Stop currentStop = stops.get(currentStopIndex);
						currentStopIndex++;
						float stopX = currentStop.getPosition();

						switch(currentStop.getType()) {
						case LEFT:
							float paragraphWidth = paragraphEmbeddable.getWidth(paragraphStyle, textSize);;
							stopX -= paragraphWidth;
							break;
						case CENTER:
							paragraphWidth = paragraphEmbeddable.getWidth(paragraphStyle, textSize);
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
					
					Point embeddableSize = paragraphEmbeddable.layout(pager.getCurrentPage(), paragraphStyle, textSize, startX, baseLine);
					startX += embeddableSize.getX();
					
					//Keep greatest line spacing to not overlap embeddables of other lines.
					float currentLineSpacing = paragraphEmbeddable.getLineSpacing(paragraphStyle);
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
		
		pager.getCursorPosition().setY(nextY);
	}

	@Override
	public PdfParagraph clone() {
		int index = 0;
		CompositeList<PdfParagraphEmbeddable> listEmbeddablesClone = new CompositeList<>();
		
		while(index < embeddables.getLists().size()) {
			List<PdfParagraphEmbeddable> currentEmbeddables = embeddables.getList(index);
			//Start by cloning embeddables.
			List<PdfParagraphEmbeddable> embeddablesClones = new ArrayList<PdfParagraphEmbeddable>(currentEmbeddables.size());
		    for (PdfParagraphEmbeddable embeddable : currentEmbeddables) {
		    	embeddablesClones.add(embeddable.clone());
		    }
		    listEmbeddablesClone.addList(embeddablesClones);
			index++;
		}
	    
	    //Clone create a new Paragraph with clones.
		return new PdfParagraph(style, listEmbeddablesClone);
	}
	
	public CompositeList<PdfParagraphEmbeddable> getEmbeddables() {
		return embeddables;
	}
	
	public void prependEmbeddable(PdfParagraphEmbeddable... embeddables) {
		this.embeddables.getList(0).addAll(0, Arrays.asList(embeddables));
	}
	
	public ParagraphStyle getStyle() {
		return style;
	}
	
	public List<ParagraphEmbeddableLine> getEmbeddableLines() {
		return ParagraphEmbeddableLine.getEmbeddableLines(embeddables);
	}
	
	public List<ParagraphEmbeddableLine> getEmbeddableLines(Pager pager, float maxWidth) {
		List<ParagraphEmbeddableLine> embeddableSubLines = new ArrayList<>();
		
		boolean isFirstLine = true;		
		for(ParagraphEmbeddableLine embeddableLine : getEmbeddableLines()) {
			float firstLineMaxWidth = maxWidth;
	
			if(isFirstLine) {
				firstLineMaxWidth -= getStyle().getFirstLineMargin();
			}
			
			//Split text to maximum space.
			embeddableSubLines.addAll(embeddableLine.splitToMaxWidth(pager, getStyle(), getStyle().getFontSize(), firstLineMaxWidth, maxWidth, stops));
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		return embeddableSubLines;
	}
}
