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

package com.web4enterprise.pdf.layout.document.impl.command;

import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.document.impl.PdfSectionCommand;
import com.web4enterprise.pdf.layout.page.impl.PdfVerticalStopsList;
import com.web4enterprise.report.commons.exception.BadOperationException;

/**
 * PDF command to go to next vertical stop of section.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfNextVerticalStopCommand implements PdfSectionCommand {
	/**
	 * The list of vertical stop to add to section.
	 */
	protected PdfVerticalStopsList verticalStops = new PdfVerticalStopsList();

	@Override
	public void prepareNextLayOut(PdfPager pdfPager) {
		verticalStops.reset();
	}
	
	@Override
	public boolean verifyLayOut(PdfPager pdfPager) {
		return true;
	}

	@Override
	public void layOut(PdfPager pdfPager) {
		int currentStopIndex = verticalStops.getCurrentIndex();
		
		if(verticalStops.size() > currentStopIndex) {
			float stopPosition = verticalStops.get(currentStopIndex);
			if(stopPosition < pdfPager.getCursorPosition().getY()) {
				pdfPager.getCursorPosition().setY(stopPosition);
			}
		} else {
			throw new BadOperationException("There is no vertical stop available.");
		}
		
		verticalStops.next();
	}
	
	/**
	 * Set the list of vertical stops for section.
	 * 
	 * @param verticalStops The vertial stops to set for section.
	 */
	public void setStops(PdfVerticalStopsList verticalStops) {
		this.verticalStops = verticalStops;
	}
}
