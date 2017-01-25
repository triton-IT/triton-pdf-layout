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

import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.page.impl.Page;

/**
 * The pager responsible for controlling layout of document.
 * 
 * 
 * @author Régis Ramillien
 */
public interface PdfPager {
	
	/**
	 * Add a page (based on previous page style) to document.
	 */
	public void addPage();
	
	/**
	 * Get the current number of the page in document.
	 * 
	 * @return The number of current page.
	 */
	public int getCurrentPageNumber();

	/**
	 * Get page under lay-out.
	 * 
	 * @return The page currently under lay-out.
	 */
	public Page getCurrentPage();
	
	/**
	 * Get the cursor position on page.
	 * 
	 * @return The current cursor position.
	 */
	public Point getCursorPosition();
}
