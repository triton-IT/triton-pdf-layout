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

/**
 * Interface for defining commands available for a section.
 * 
 * 
 * @author Régis Ramillien
 */
public interface PdfSectionCommand {
	/**
	 * Prepare section for next layout.
	 * 
	 * @param pdfPager The pager to get information from.
	 */
	void prepareNextLayOut(PdfPager pdfPager);
	/**
	 * Perform rendering of this section.
	 * 
	 * @param pdfPager The page to get information from.
	 */
	void layOut(PdfPager pdfPager);
	/**
	 * Verify the rendering of this section.
	 * 
	 * @param pdfPager The page to get information from.
	 * @return [@code true} if rendering is OK, {@code false} if rendering needs to be performed again.
	 */
	boolean verifyLayOut(PdfPager pdfPager);
}
