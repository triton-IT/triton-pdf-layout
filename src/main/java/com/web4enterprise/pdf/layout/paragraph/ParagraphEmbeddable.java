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

package com.web4enterprise.pdf.layout.paragraph;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;

/**
 * Defines an element which can be embedde ina {@link Paragraph}.
 * 
 * 
 * @author Régis Ramillien
 */
public interface ParagraphEmbeddable {
	/**
	 * Defines a link to a target {@link DocumentEmbeddable}.
	 * 
	 * @param documentEmbeddable The target of the link.
	 */
	void setLink(DocumentEmbeddable documentEmbeddable);
	
	/**
	 * Add a foot-note to this paragraph embeddable.
	 * 
	 * @param footNote The foot-note to add.
	 */
	void addFootNote(FootNote footNote);
	/**
	 * Add a foot-note to this paragraph embeddable.
	 * 
	 * @param paragraphs The lists of {@link Paragraph} to add as foot-note.
	 */
	void addFootNote(Paragraph... paragraphs);
}
