/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.paragraph.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfDocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.image.ImageData;
import com.web4enterprise.pdf.layout.page.impl.PdfPageFootNotes;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.text.impl.PdfText;
import com.web4enterprise.report.commons.exception.BadOperationException;
import com.web4enterprise.report.commons.geometry.Point;
import com.web4enterprise.report.commons.geometry.Rect;
import com.web4enterprise.report.commons.utils.CompositeList;

/**
 * Implements a Paragraph for a PDF document.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfParagraph extends PdfDocumentEmbeddable implements Paragraph {
	/**
	 * The style of this paragraph.
	 */
	protected ParagraphStyle style = null;
	/**
	 * The list of stops of this paragraph.
	 */
	protected List<Stop> stops = new ArrayList<>();
	/**
	 * List of stops and theirs paragraph embeddables.
	 */
	protected CompositeList<PdfParagraphEmbeddable> embeddablesByStop = new CompositeList<>();
	/**
	 * The next stop identifier to meet.
	 */
	protected int nextStop = 0;
	/**
	 * A cache of embeddables for the current stop.
	 */
	protected List<PdfParagraphEmbeddable> stopEmbeddables = new ArrayList<PdfParagraphEmbeddable>();
	/**
	 * The element linked to this paragraph.
	 */
	protected DocumentEmbeddable linkedElement;
	
	/**
	 * Constructs an empty paragraph.
	 */
	public PdfParagraph() {
		this((String[]) null);
	}
	
	/**
	 * Constructs a paragraphs from given strings.
	 * 
	 * @param texts The strings to add to this paragraph.
	 */
	public PdfParagraph(String... texts) {
		this(new ParagraphStyle(), texts);
	}
	
	/**
	 * Constructs a paragraph from given {@link Style}.
	 * 
	 * @param style The {@link Style} to set to paragraph.
	 */
	public PdfParagraph(ParagraphStyle style) {
		this(style, (String[]) null);
	}
	
	/**
	 * Constructs a pararaph from {@link Style} and strings.
	 * 
	 * @param style The {@link Style} to set to paragraph.
	 * @param texts The strings to add to this paragraph.
	 */
	public PdfParagraph(ParagraphStyle style, String... texts) {
		embeddablesByStop.addList(stopEmbeddables);
		this.style = style;
		if(texts != null) {
			for(String text : texts) {
				this.stopEmbeddables.add(new PdfText(text));
			}
		}
	}
	
	/**
	 * Constructs a paragraph from a list of {@link ParagraphEmbeddable}
	 * 
	 * @param paragraphEmbeddables The list of {@link ParagraphEmbeddable} to add to paragraph.
	 */
	public PdfParagraph(PdfParagraphEmbeddable... paragraphEmbeddables) {
		this(new ParagraphStyle(), paragraphEmbeddables);
	}
	
	/**
	 * Construct a paragraph from a {@link Style} and a list of {@link ParagraphEmbeddable}.
	 * 
	 * @param style The {@link Style} to set to paragraph.
	 * @param paragraphEmbeddables The list of {@link ParagraphEmbeddable} to add to paragraph.
	 */
	public PdfParagraph(ParagraphStyle style, PdfParagraphEmbeddable... paragraphEmbeddables) {
		embeddablesByStop.addList(stopEmbeddables);
		this.style = style;
		this.stopEmbeddables.addAll(Arrays.asList(paragraphEmbeddables));
	}
	
	/**
	 * Used only for cloning.
	 * 
	 * @param style The {@link Style} of paragraph.
	 * @param embeddables The list of {@link ParagraphEmbeddable} to reference.
	 */
	private PdfParagraph(ParagraphStyle style, CompositeList<PdfParagraphEmbeddable> embeddables) {
		this.style = style;
		this.embeddablesByStop = embeddables;
	}

	@Override
	public Image createImage(ImageData imageData) {
		return imageData.createImage();
	}

	@Override
	public Image createImage(ImageData imageData, int width, int height) {
		Image clone = imageData.createImage();
		
		clone.setWidth(width);
		clone.setHeight(height);
		
		return clone;
	}

	@Override
	public Image createImageForWidth(ImageData imageData, int width) {
		Image clone = imageData.createImage();
		
		clone.setWidth(width, true);
		
		return clone;
	}

	@Override
	public Image createImageForHeight(ImageData imageData, int height) {
		Image clone = imageData.createImage();
		
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
			if(!(embeddable instanceof PdfParagraphEmbeddable)) {
				throw new BadOperationException("You must add an embeddable useable by this API.");
			}
			stopEmbeddables.add((PdfParagraphEmbeddable) embeddable);
		}		
	}

	@Override
	public void setStyle(ParagraphStyle style) {
		this.style = style;
	}
	
	@Override
	public void addStop(Stop stop) {
		stops.add(stop);
		embeddablesByStop.addList(new ArrayList<PdfParagraphEmbeddable>());
	}

	@Override
	public void nextStop(String... values) {
		nextStop++;
		stopEmbeddables = embeddablesByStop.getList(nextStop);
		for(String value : values) {
			stopEmbeddables.add((PdfText) createText(value));
		}
	}

	@Override
	public void nextStop(ParagraphEmbeddable... embeddables) {
		nextStop++;
		stopEmbeddables = this.embeddablesByStop.getList(nextStop);
		for(ParagraphEmbeddable embeddable : embeddables) {
			if(!(embeddable instanceof PdfParagraphEmbeddable)) {
				throw new BadOperationException("You must add an embeddable useable by this API.");
			}
			stopEmbeddables.add((PdfParagraphEmbeddable) embeddable);
		}
	}

	@Override
	public String getTOCText() {
		return this.getEmbeddables().get(0).getTOCText();
	}

	@Override
	public void layOut(PdfPager pdfPager, Rect boundingBox, PdfPageFootNotes pdfPageFootNotes) {
		//super.layOut(pdfPager, boundingBox, pageFootNotes);
		pageNumber = pdfPager.getCurrentPageNumber();
		
		//Get all lines of text (composed of text of different style).
		List<PdfParagraphEmbeddableLine> embeddableLines = getEmbeddableLines();
		
		//Get the paragraph style which is the default style of text which compose it.
		ParagraphStyle paragraphStyle = getStyle();

		float fontBaseLine = paragraphStyle.getFontVariant().getBaseLine(paragraphStyle.getFontSize());

		float startY = pdfPager.getCursorPosition().getY();
		
		//Apply top margin to paragraph.
		float nextY = startY - paragraphStyle.getMargins().getTop();
		
		//Get this paragraph style. 
		float fontSize = paragraphStyle.getFontSize();
		
		//If this is the first line, some special behavior will have to be performed, so set it to true for now.
		boolean isFirstLine = true;
		
		//Iterate of each line of text to display them.
		for(PdfParagraphEmbeddableLine textLine : embeddableLines) {
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
			List<PdfParagraphEmbeddableLine> embeddableSubLines = textLine.splitToMaxWidth(pdfPager, paragraphStyle, fontSize, firstLineMaxWidth, maxWidth, relativeStops);
			
			boolean firstSubLine = true;
			for(PdfParagraphEmbeddableLine embeddableSubLine : embeddableSubLines) {
				//Calculate text base line.
				float baseLine = nextY - fontBaseLine;

				//Calculate page bottom with already added footnotes  
				float bottom = boundingBox.getBottom();
				float existingFootNotesHeight = 0.0f;
				if(pdfPageFootNotes != null) {
					existingFootNotesHeight = pdfPageFootNotes.getHeight(pdfPager, pdfPageFootNotes.getWidth());
					
					//FootNote must be rendered on same page than line, add height of the footNote to the bottom of page.
					for(PdfParagraphEmbeddable paragraphEmbeddable : embeddableSubLine) {
						for(PdfFootNote footNote : paragraphEmbeddable.getFootNotes()) {
							bottom += footNote.getHeight(pdfPager, pdfPageFootNotes.getWidth());
						}
					}
				}
				
				//If text and its footNote does not fit in page, add a new page.
				if(baseLine <= bottom + existingFootNotesHeight) {
					pdfPager.addPage();
					startY = pdfPager.getCursorPosition().getY();
					//Add page reinitialize blockStart, so calculate it again.
					baseLine = startY - fontBaseLine;
					nextY =  startY - paragraphStyle.getMargins().getTop();
				} else {
					//if it fits in page, add existing foot notes height to this page.
					bottom += existingFootNotesHeight;
				}

				//Add footNotes of the line to page.
				if(pdfPageFootNotes != null) {
					for(PdfParagraphEmbeddable paragraphEmbeddable : embeddableSubLine) {
						for(PdfFootNote footNote : paragraphEmbeddable.getFootNotes()) {
							pdfPageFootNotes.addEmbeddables(footNote);
						}
					}
					//Because an "empty" footNote has already been computed with this same size, we force it to re-compute with new content. 
					pdfPageFootNotes.compute(pdfPager, pdfPageFootNotes.getWidth());
				}
				
				//If this is the first line, set the link.
				if(pageId == null) {
					pageId = pdfPager.getCurrentPage().getCorePage().getId();
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
							- embeddableSubLine.getWidth(paragraphStyle, fontSize);
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
							+ (freeSpace - embeddableSubLine.getWidth(paragraphStyle, fontSize)) / 2;
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
				CompositeList<PdfParagraphEmbeddable>.CompositeListIterator paragraphIterator = embeddableSubLine.iterator();
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
							float paragraphWidth = paragraphEmbeddable.getWidth(paragraphStyle, fontSize);
							stopX -= paragraphWidth;
							break;
						case CENTER:
							paragraphWidth = paragraphEmbeddable.getWidth(paragraphStyle, fontSize);
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
					
					if(!paragraphEmbeddable.isLinked()) {
						paragraphEmbeddable.setLink(linkedElement);
					}
					Point embeddableSize = paragraphEmbeddable.layOut(pdfPager.getCurrentPage(), paragraphStyle, fontSize, startX, baseLine);
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
		
		pdfPager.getCursorPosition().setY(nextY);
	}

	@Override
	public PdfParagraph clone() {
		int index = 0;
		CompositeList<PdfParagraphEmbeddable> listEmbeddablesClone = new CompositeList<>();
		
		while(index < embeddablesByStop.getLists().size()) {
			List<PdfParagraphEmbeddable> currentEmbeddables = embeddablesByStop.getList(index);
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
	
	@Override
	public void setLink(DocumentEmbeddable documentEmbeddable) {
		linkedElement = documentEmbeddable;
	}
	
	/**
	 * Get the list of {@link ParagraphEmbeddable}.
	 * 
	 * @return The list of {@link ParagraphEmbeddable}.
	 */
	public CompositeList<PdfParagraphEmbeddable> getEmbeddables() {
		return embeddablesByStop;
	}
	
	/**
	 * Prepend a liist of {@link ParagraphEmbeddable} to this paragraph.
	 * 
	 * @param embeddables The list of {@link ParagraphEmbeddable} to prepend.
	 */
	public void prependEmbeddable(PdfParagraphEmbeddable... embeddables) {
		this.embeddablesByStop.getList(0).addAll(0, Arrays.asList(embeddables));
	}
	
	@Override
	public ParagraphStyle getStyle() {
		return style;
	}
	
	/**
	 * Get the a list of {@link PdfParagraphEmbeddableLine} for this paragraph.
	 * 
	 * @return The list of {@link PdfParagraphEmbeddableLine} for this paragraph.
	 */
	public List<PdfParagraphEmbeddableLine> getEmbeddableLines() {
		return getEmbeddableLines(embeddablesByStop);
	}
	
	/**
	 * Get the list of {@link PdfParagraphEmbeddableLine} of this paragraph. 
	 * 
	 * @param pdfPager The pager to use to split lines.
	 * @param maxWidth The maximum width of the line.
	 * @return The list of {@link PdfParagraphEmbeddableLine}.
	 */
	public List<PdfParagraphEmbeddableLine> getEmbeddableLines(PdfPager pdfPager, float maxWidth) {
		List<PdfParagraphEmbeddableLine> embeddableSubLines = new ArrayList<>();
		
		boolean isFirstLine = true;		
		for(PdfParagraphEmbeddableLine embeddableLine : getEmbeddableLines()) {
			float firstLineMaxWidth = maxWidth;
	
			if(isFirstLine) {
				firstLineMaxWidth -= getStyle().getFirstLineMargin();
			}
			
			//Split text to maximum space.
			embeddableSubLines.addAll(embeddableLine.splitToMaxWidth(pdfPager, getStyle(), getStyle().getFontSize(), firstLineMaxWidth, maxWidth, stops));
			
			if(isFirstLine) {
				isFirstLine = false;
			}
		}
		
		return embeddableSubLines;
	}

	@Override
	public void compute(PdfPager pdfPager, float width) {
		height = 0.0f;
		
		for(PdfParagraphEmbeddableLine embeddableLine : getEmbeddableLines(pdfPager, width)) {
			height += embeddableLine.getHeight(getStyle());
		}
		
		height += getStyle().getMargins().getTop() + getStyle().getMargins().getBottom();
		
		computedWidth = width;
	}
	
	/**
	 * Convert a list of elements to a list of ElementLine.
	 * List of elements is parsed to search for new line in each Element. 
	 * Elements are added to ElementLine elements until a new line is found.
	 * If a new line is found in an element, a new ElementLine is created and following elements are added until new line.
	 * @param elements The elements to parse.
	 * @return The list of ElementLine.
	 */
	private List<PdfParagraphEmbeddableLine> getEmbeddableLines(CompositeList<PdfParagraphEmbeddable> elements) {
		//Initialize the array of lines of elements.
		List<PdfParagraphEmbeddableLine> pdfParagraphEmbeddableLines = new ArrayList<>();
		//Create a new element line and add it to array. This will contain the elements of the first line, within a different list for each stop.
		PdfParagraphEmbeddableLine pdfParagraphEmbeddableLine = new PdfParagraphEmbeddableLine();
		ArrayList<PdfParagraphEmbeddable> stopElementLine = new ArrayList<PdfParagraphEmbeddable>();
		pdfParagraphEmbeddableLine.addList(stopElementLine);
		pdfParagraphEmbeddableLines.add(pdfParagraphEmbeddableLine);
		
		//Iterate over each paragraphEmbeddable to search for new lines and create a new List on a new Stop.
		CompositeList<PdfParagraphEmbeddable>.CompositeListIterator paragraphIterator = elements.iterator();
		while(paragraphIterator.hasNext()) {
			PdfParagraphEmbeddable element = paragraphIterator.next();
			//If list has changed, it means that a stop has been inserted. So, do the same for line, insert a new List to simulate Stop.
			if(paragraphIterator.hasListChanged()) {
				stopElementLine = new ArrayList<PdfParagraphEmbeddable>();
				pdfParagraphEmbeddableLine.addList(stopElementLine);
			}
			List<PdfParagraphEmbeddable> currentElementLines = element.getLines();
			Iterator<PdfParagraphEmbeddable> currentElementLinesIterator = currentElementLines.iterator();
			while(currentElementLinesIterator.hasNext()) {
				PdfParagraphEmbeddable currentElement = currentElementLinesIterator.next();
				stopElementLine.add(currentElement);
				if(currentElementLinesIterator.hasNext()) {
					pdfParagraphEmbeddableLine = new PdfParagraphEmbeddableLine();
					stopElementLine = new ArrayList<PdfParagraphEmbeddable>();
					pdfParagraphEmbeddableLine.addList(stopElementLine);
					pdfParagraphEmbeddableLines.add(pdfParagraphEmbeddableLine);
				}				
			}
		}
		
		return pdfParagraphEmbeddableLines;
	}
}
