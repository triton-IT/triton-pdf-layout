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

import com.web4enterprise.pdf.layout.style.Style;

/**
 * A "widget" that can be embedded to a document.
 * 
 * 
 * @author Régis Ramillien
 */
public interface DocumentEmbeddable {
	/**
	 * Get the style of the widget.
	 * 
	 * @return The style.
	 */
	Style getStyle();
	/**
	 * Get the text to display on table of content.
	 * 
	 * @return The text to display.
	 */
	String getTOCText();
	/**
	 * Get the number of the page in document.
	 * 
	 * @return The number of the page.
	 */
	Integer getPageNumber();
}
