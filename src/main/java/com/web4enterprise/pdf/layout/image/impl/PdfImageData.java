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

package com.web4enterprise.pdf.layout.image.impl;

import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.image.ImageData;

/**
 * PDF Implementation of the Image API.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfImageData implements ImageData {
	/**
	 * The core PDF image.
	 */
	protected com.web4enterprise.pdf.core.image.Image coreImage;
	
	/**
	 * Constructor from an existing image definition.
	 * 
	 * @param coreImage The existing image.
	 */
	public PdfImageData(com.web4enterprise.pdf.core.image.Image coreImage) {
		this.coreImage = coreImage.cloneReference();
	}
	
	@Override
	public Image createImage() {
		return new PdfImage(coreImage);
	}
}
