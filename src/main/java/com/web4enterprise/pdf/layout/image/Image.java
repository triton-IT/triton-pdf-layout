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

package com.web4enterprise.pdf.layout.image;

import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;

/**
 * Defines an Image to embed in document.
 * 
 * 
 * @author Régis Ramillien
 */
public interface Image extends ParagraphEmbeddable {
	/**
	 * Set width of image without keeping ration.
	 * 
	 * @param width The new width of image.
	 */
	void setWidth(int width);
	
	/***
	 * Set width of image with or without keeping ratio for height.
	 * 
	 * @param width The new width of image.
	 * @param keepRatio True if ratio must be kept on height.
	 */
	void setWidth(int width, boolean keepRatio);
	
	/**
	 * Set height of image without keeping ratio.
	 * 
	 * @param height The new height of image.
	 */
	void setHeight(int height);	
	
	/**
	 * Set the height of image with or without keeping ratio for width.
	 * 
	 * @param height The height of image.
	 * @param keepRatio True if ratio must be kept on width.
	 */
	void setHeight(int height, boolean keepRatio);

	/**
	 * Get the width of the image.
	 * 
	 * @return The width in PDF unit.
	 */
	int getWidth();

	/**
	 * Get the height of the image.
	 * 
	 * @return The height in PDF unit.
	 */
	int getHeight();
	
	/**
	 * Partially clone the image.
	 * This clone does not clone raw data but jjust meta-data to save memory.
	 * 
	 * @return The image's with only meta-data.
	 */
	Image clone();
}
