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

package com.web4enterprise.pdf.layout.page;

/**
 * The size is in pt (point). This is the metric used byPDF.
 * https://en.wikipedia.org/wiki/Paper_size#Overview:_ISO_paper_Sizes
 */
public class PageFormat {
	/**
	 * A0 format in portrait orientation.
	 */
	public static PageFormat A0_PORTRAIT = new PageFormat(2384, 3370);
	/**
	 * A0 format in landscape orientation.
	 */
	public static PageFormat A0_LANDSCAPE = new PageFormat(3370, 2384);
	/**
	 * A1 format in portrait orientation.
	 */
	public static PageFormat A1_PORTRAIT = new PageFormat(1684, 2384);
	/**
	 * A1 format in landscape orientation.
	 */
	public static PageFormat A1_LANDSCAPE = new PageFormat(2384, 1684);
	/**
	 * A2 format in portrait orientation.
	 */
	public static PageFormat A2_PORTRAIT = new PageFormat(1191, 1684);
	/**
	 * A2 format in landscape orientation.
	 */
	public static PageFormat A2_LANDSCAPE = new PageFormat(1684, 1191);
	/**
	 * A3 format in portrait orientation.
	 */
	public static PageFormat A3_PORTRAIT = new PageFormat(842, 1191);
	/**
	 * A3 format in landscape orientation.
	 */
	public static PageFormat A3_LANDSCAPE = new PageFormat(1191, 842);
	/**
	 * A4 format in portrait orientation.
	 */
	public static PageFormat A4_PORTRAIT = new PageFormat(595, 842);
	/**
	 * A4 format in landscape orientation.
	 */
	public static PageFormat A4_LANDSCAPE = new PageFormat(842, 595);
	/**
	 * A5 format in portrait orientation.
	 */
	public static PageFormat A5_PORTRAIT = new PageFormat(420, 595);
	/**
	 * A5 format in landscape orientation.
	 */
	public static PageFormat A5_LANDSCAPE = new PageFormat(595, 420);
	/**
	 * A6 format in portrait orientation.
	 */
	public static PageFormat A6_PORTRAIT = new PageFormat(298, 420);
	/**
	 * A6 format in landscape orientation.
	 */
	public static PageFormat A6_LANDSCAPE = new PageFormat(420, 298);
	/**
	 * A7 format in portrait orientation.
	 */
	public static PageFormat A7_PORTRAIT = new PageFormat(210, 298);
	/**
	 * A7 format in landscape orientation.
	 */
	public static PageFormat A7_LANDSCAPE = new PageFormat(298, 210);
	/**
	 * A8 format in portrait orientation.
	 */
	public static PageFormat A8_PORTRAIT = new PageFormat(147, 210);
	/**
	 * A8 format in landscape orientation.
	 */
	public static PageFormat A8_LANDSCAPE = new PageFormat(210, 147);
	
	/**
	 * The width of this page format.
	 */
	protected int width;
	/**
	 * The height of this page format.
	 */
	protected int height;
	
	/**
	 * Construct a page format from width and height.
	 * 
	 * @param width The width of the page.
	 * @param height The height of the page.
	 */
	public PageFormat(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Get width of the format.
	 * 
	 * @return The width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set width of the format.
	 * 
	 * @param width The width.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get height of the format.
	 * 
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set width of the format.
	 * 
	 * @param height The height.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
