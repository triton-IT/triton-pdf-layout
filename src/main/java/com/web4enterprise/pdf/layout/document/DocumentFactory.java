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

import com.web4enterprise.pdf.layout.document.impl.PdfDocument;

/**
 * Simple factory to create a new document.
 * This factory is used to avoid to instantiate the implementation and to hide all implementation complexity to end-developer.
 * 
 * @author Régis Ramillien
 */
public class DocumentFactory {
	/**
	 * Create an empty PDF document.
	 * 
	 * @return The empty PDF document.
	 */
	public static Document createPdfDocument() {
		return new PdfDocument();
	}
}
