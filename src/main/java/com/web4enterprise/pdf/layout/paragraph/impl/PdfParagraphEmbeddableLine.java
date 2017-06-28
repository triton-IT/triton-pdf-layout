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
import java.util.Iterator;
import java.util.List;

import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable.SplitInformation;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.report.commons.utils.CompositeList;

/**
 * An ElementLine is a line of different Elements without any line return.
 * A line of element can be composed of one or more Elements. Elements can have different styles and associated to different stops.
 * All elements for a given stop are gathered into a same list, itself gathered into a "meta" {@link CompositeList}.
 */
public class PdfParagraphEmbeddableLine extends CompositeList<PdfParagraphEmbeddable> {
	/**
	 * Split this list of {@link PdfParagraphEmbeddable} into independent lines.
	 * 
	 * @param pdfPager The pager to get information from.
	 * @param defaultStyle The style to use if no other style has been defined.
	 * @param defaultFontSize The font size to use if no other style has been defined.
	 * @param firstLineMaxWidth The maximum of the first line.
	 * @param maxWidth The maximum width of other lines of this paragraph embeddable.
	 * @param stops The list of stops to get position from.
	 * @return A list of independent {@link PdfParagraphEmbeddableLine}.
	 */
	public List<PdfParagraphEmbeddableLine> splitToMaxWidth(PdfPager pdfPager, ParagraphStyle defaultStyle, float defaultFontSize, float firstLineMaxWidth, Float maxWidth, List<Stop> stops) {
		List<PdfParagraphEmbeddableLine> pdfParagraphEmbeddableLines = new ArrayList<>();
		
		float currentX = 0.0f;
		PdfParagraphEmbeddableLine currentElementLine = new PdfParagraphEmbeddableLine();
		ArrayList<PdfParagraphEmbeddable> stopElementLine = new ArrayList<PdfParagraphEmbeddable>();
		currentElementLine.addList(stopElementLine);
		pdfParagraphEmbeddableLines.add(currentElementLine);
		
		//Defines if the line being processed is the first or not.
		boolean isFirstLine = true;

		//The index of current stop.
		int currentStopIndex = 0;
		//Iterate over each paragraphEmbeddable to search for new lines and create a new List on a new Stop.
		CompositeListIterator paragraphIterator = iterator();
		while(paragraphIterator.hasNext()) {
			PdfParagraphEmbeddable element = paragraphIterator.next();

			//If list has changed, it means that a stop has been inserted. So, do the same for line, insert a new List to simulate Stop.
			if(paragraphIterator.hasListChanged()) {
				//Get next stop position and increment its index accordingly.
				Stop currentStop = stops.get(currentStopIndex);
				currentStopIndex++;

				//If we already passed stop, do not increment position. Continue to write where we are.
				float stopX = currentStop.getPosition();
				if(stopX > currentX) {
					currentX = stopX;
				}
				
				//Add a list to element line to simulate this new stop.
				stopElementLine = new ArrayList<PdfParagraphEmbeddable>();
				currentElementLine.addList(stopElementLine);
			}
			
			SplitInformation splitInformation = element.split(pdfPager, defaultStyle, defaultFontSize, currentX, firstLineMaxWidth, maxWidth);
			
			//Insert end of line on current stop line.
			Iterator<PdfParagraphEmbeddable> iterator = splitInformation.splitEmbeddables.iterator();
			stopElementLine.add(iterator.next());
			
			//Create new line for each new line.
			while(iterator.hasNext()) {
				if(isFirstLine) {
					isFirstLine = false;
				}
				//Create a new Element line with a new list inside for first stop.
				currentElementLine = new PdfParagraphEmbeddableLine();
				stopElementLine = new ArrayList<PdfParagraphEmbeddable>();
				currentElementLine.addList(stopElementLine);
				
				//Add current Element line to resulting lines.
				pdfParagraphEmbeddableLines.add(currentElementLine);
				
				//Reinitialize current position and stop index.
				currentX = 0.0f;
				currentStopIndex = 0;
				
				//Add PdfParagraphEmbeddable to current stop Element line.
				PdfParagraphEmbeddable splitElement = iterator.next();
				stopElementLine.add(splitElement);
			}
			
			currentX += splitInformation.positionX;
		}
		
		return pdfParagraphEmbeddableLines;
	}

	/**
	 * Get the width of this line.
	 * 
	 * @param defaultStyle The style to use if no other style has been defined.
	 * @param defaultFontSize The font size to use if no other style has been defined.
	 * @return The width of this line.
	 */
	public int getWidth(ParagraphStyle defaultStyle, float defaultFontSize) {
		int width = 0;
		
		for(PdfParagraphEmbeddable element : this) {
			width += element.getWidth(defaultStyle, defaultFontSize);
		}
		
		return width;
	}
	
	/**
	 * Get the height of this line.
	 * 
	 * @param paragraphStyle The style to use as default to get height.
	 * @return The height of this line.
	 */
	public float getHeight(ParagraphStyle paragraphStyle) {
		float lineSpacing = 0;
		for(PdfParagraphEmbeddable element : this) {
			//Keep greatest line spacing to not overlap elements of other lines.
			float currentLineSpacing = element.getLineSpacing(paragraphStyle);
			if(currentLineSpacing > lineSpacing) {
				lineSpacing = currentLineSpacing;
			}
		}
		return lineSpacing;
	}
}
