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

package com.web4enterprise.pdf.layout.page.impl;

import java.util.ArrayList;

/**
 * List of vertical stops.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfVerticalStopsList extends ArrayList<Float> {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Index of the next stop to stop to.
	 */
	protected int currentIndex = 0;
	
	/*
	 * Get the identifier of next stop to stop to.
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	/**
	 * Advance to next stop.
	 */
	public void next() {
		currentIndex++;
	}
	
	/**
	 * Add stops.
	 * 
	 * @param stops The stops to add.
	 */
	public void add(float... stops) {
		for(float stop : stops) {
			add(stop);
		}
	}

	/**
	 * Reset the index of current stop.
	 */
	public void reset() {
		currentIndex = 0;
	}
}
