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

package com.web4enterprise.pdf.layout.text;

import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;

/**
 * Defines a Text taht can be embedded in any containers.
 * 
 * 
 * @author Régis Ramillien
 */
public interface Text extends ParagraphEmbeddable {
	/**
	 * Constant for a new line.
	 */
	public static final String NEW_LINE = "\n";	
	
	/**
	 * Set the style of this text.
	 * 
	 * @param style The style to set.
	 */
	void setStyle(TextStyle style);
	
	/**
	 * Set the string value of this text.
	 * 
	 * @param string The value as string to set to this text.
	 */
	void setString(String string);	
}
