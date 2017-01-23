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

package com.web4enterprise.pdf.layout.document.impl;

import java.util.ArrayList;

import com.web4enterprise.pdf.layout.document.Section;
import com.web4enterprise.pdf.layout.document.impl.command.PdfNextVerticalStopCommand;

/**
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfSection extends ArrayList<PdfSectionCommand> {
	/**
	 * The UID for this version of class.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The section definition (style).
	 */
	protected Section section;
	
	/**
	 * Creates a new PDF section from a section.
	 * 
	 * @param section THe section to use for this PDF.
	 */
	public PdfSection(Section section) {
		this.section = section;
	}
	
	/**
	 * Add a vertical stop to this section.
	 * 
	 * @param pdfNextVerticalStopCommand The command for next stop.
	 */
	public void add(PdfNextVerticalStopCommand pdfNextVerticalStopCommand) {
		pdfNextVerticalStopCommand.setStops(section.getVerticalStops());
		super.add(pdfNextVerticalStopCommand);
	}

	/**
	 * Get section of this PDF.
	 * 
	 * @return The section.
	 */
	public Section getSection() {
		return section;
	}
}
