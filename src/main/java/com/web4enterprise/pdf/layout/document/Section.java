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

package com.web4enterprise.pdf.layout.document;

import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.impl.VerticalStopsList;

/**
 * Defines a section in the document.
 * 
 * 
 * @author Régis Ramillien
 */
public class Section {
	/**
	 * The style of the page applicable to this section.
	 */
	protected PageStyle style;
	/**
	 * The header of the page.
	 */
	protected PageHeader header;
	/**
	 * The footer of the page.
	 */
	protected PageFooter footer;
	/**
	 * The list of stops of this section.
	 */
	protected VerticalStopsList verticalStops = new VerticalStopsList();

	/**
	 * Create a new section nowith default values.
	 */
	public Section() {
		this(PageStyle.A4_PORTRAIT, null, null);
	}

	/**
	 * Create a new section with given style, header/footer and stops.
	 * 
	 * @param style The style to use for this section.
	 * @param header The header to add on each page of this section.
	 * @param footer The footer to add on each page of this section
	 * @param verticalStops The stops to set to this section.
	 */
	public Section(PageStyle style, PageHeader header, PageFooter footer,
			float... verticalStops) {
		this.style = style;
		this.header = header;
		this.footer = footer;
		this.verticalStops.add(verticalStops);
	}
	
	/**
	 * Add a vertical stop to the current page.
	 * On a call to {@link Document#nextVerticalStop}, the next element will be add to the next available vertical stop.
	 * 
	 * @param position The position in number of units of the vertical stopp to place.
	 */
	public void addVerticalStop(float position) {
		this.verticalStops.add(position);
	}

	/**
	 * Get the style of this sectiono.
	 * 
	 * @return The style of this section.
	 */
	public PageStyle getStyle() {
		return style;
	}

	/**
	 * Get the header displayed on all pages of this section.
	 * 
	 * @return The header of this section.
	 */
	public PageHeader getHeader() {
		return header;
	}

	/**
	 * Get the footer displayed on all pages of this section.
	 * 
	 * @return The footer of this section.
	 */
	public PageFooter getFooter() {
		return footer;
	}

	/**
	 * Get the list of stops of this section.
	 * 
	 * @return The list of stops of this section.
	 */
	public VerticalStopsList getVerticalStops() {
		return verticalStops;
	}
}
