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

import com.web4enterprise.pdf.layout.placement.Margins;

/**
 * Defines the style of a page.
 * 
 * 
 * @author Régis Ramillien
 */
public class PageStyle {
	/**
	 * A0 page style in portrait orientation.
	 */
	public static final PageStyle A0_PORTRAIT = new PageStyle(PageFormat.A0_PORTRAIT, Margins.A0);
	/**
	 * A0 page style in landscape orientation.
	 */
	public static final PageStyle A0_LANDSCAPE = new PageStyle(PageFormat.A0_LANDSCAPE, Margins.A0);
	/**
	 * A1 page style in portrait orientation.
	 */
	public static final PageStyle A1_PORTRAIT = new PageStyle(PageFormat.A1_PORTRAIT, Margins.A1);
	/**
	 * A1 page style in landscape orientation.
	 */
	public static final PageStyle A1_LANDSCAPE = new PageStyle(PageFormat.A1_LANDSCAPE, Margins.A1);
	/**
	 * A2 page style in portrait orientation.
	 */
	public static final PageStyle A2_PORTRAIT = new PageStyle(PageFormat.A2_PORTRAIT, Margins.A2);
	/**
	 * A2 page style in landscape orientation.
	 */
	public static final PageStyle A2_LANDSCAPE = new PageStyle(PageFormat.A2_LANDSCAPE, Margins.A2);
	/**
	 * A3 page style in portrait orientation.
	 */
	public static final PageStyle A3_PORTRAIT = new PageStyle(PageFormat.A3_PORTRAIT, Margins.A3);
	/**
	 * A3 page style in landscape orientation.
	 */
	public static final PageStyle A3_LANDSCAPE = new PageStyle(PageFormat.A3_LANDSCAPE, Margins.A3);
	/**
	 * A4 page style in portrait orientation.
	 */
	public static final PageStyle A4_PORTRAIT = new PageStyle();
	/**
	 * A4 page style in landscape orientation.
	 */
	public static final PageStyle A4_LANDSCAPE = new PageStyle(PageFormat.A4_LANDSCAPE, Margins.A4);
	/**
	 * A5 page style in portrait orientation.
	 */
	public static final PageStyle A5_PORTRAIT = new PageStyle(PageFormat.A5_PORTRAIT, Margins.A5);
	/**
	 * A5 page style in landscape orientation.
	 */
	public static final PageStyle A5_LANDSCAPE = new PageStyle(PageFormat.A5_LANDSCAPE, Margins.A5);
	/**
	 * A6 page style in portrait orientation.
	 */
	public static final PageStyle A6_PORTRAIT = new PageStyle(PageFormat.A6_PORTRAIT, Margins.A6);
	/**
	 * A6 page style in landscape orientation.
	 */
	public static final PageStyle A6_LANDSCAPE = new PageStyle(PageFormat.A6_LANDSCAPE, Margins.A6);
	/**
	 * A7 page style in portrait orientation.
	 */
	public static final PageStyle A7_PORTRAIT = new PageStyle(PageFormat.A7_PORTRAIT, Margins.A7);
	/**
	 * A7 page style in landscape orientation.
	 */
	public static final PageStyle A7_LANDSCAPE = new PageStyle(PageFormat.A7_LANDSCAPE, Margins.A7);
	/**
	 * A8 page style in portrait orientation.
	 */
	public static final PageStyle A8_PORTRAIT = new PageStyle(PageFormat.A8_PORTRAIT, Margins.A8);
	/**
	 * A8 page style in landscape orientation.
	 */
	public static final PageStyle A8_LANDSCAPE = new PageStyle(PageFormat.A8_LANDSCAPE, Margins.A8);
	
	/**
	 * The default format for a new page.
	 */
	protected PageFormat format = PageFormat.A4_PORTRAIT;
	/***
	 * The default margins for a new page.
	 */
	protected Margins margins = Margins.A4;
	
	/**
	 * Create a default page.
	 */
	public PageStyle() {
	}
	
	/**
	 * Generally, if page format, margins also change, so create a constructor with both.
	 * @param format The page format (width and height).
	 * @param margins
	 */
	public PageStyle(PageFormat format, Margins margins) {
		this.format = format;
		this.margins = margins;
	}
	
	/**
	 * Get page format of this style.
	 * 
	 * @return The page format.
	 */
	public PageFormat getFormat() {
		return format;
	}

	/**
	 * Set page format of this style.
	 * 
	 * @param format The page format.
	 */
	public void setFormat(PageFormat format) {
		this.format = format;
	}

	/**
	 * Get the margins of this style.
	 * 
	 * @return The margins.
	 */
	public Margins getMargins() {
		return margins;
	}

	/**
	 * Set the margins of this style.
	 * 
	 * @param margins The margins.
	 */
	public void setMargins(Margins margins) {
		this.margins = margins;
	}
	
	/**
	 * Get the inner width of this style.
	 * 
	 * @return The width.
	 */
	public float getInnerWidth() {
		return getFormat().getWidth() - getMargins().getLeft() - getMargins().getRight();
	}
	
	/**
	 * Get the inner height of this style.
	 * 
	 * @return the height.
	 */
	public float getInnerHeight() {
		return getFormat().getHeight() - getMargins().getTop() - getMargins().getBottom();
	}

	/**
	 * Get the inner top of this style.
	 * 
	 * @return the top.
	 */
	public float getInnerTop() {
		return getFormat().getHeight() - getMargins().getTop();
	}

	/**
	 * Get the inner left of this style.
	 * 
	 * @return the left.
	 */
	public float getInnerLeft() {
		return getMargins().getLeft();
	}

	/**
	 * Get the inner bottom of this style.
	 * 
	 * @return the bottom.
	 */
	public float getInnerBottom() {
		return getMargins().getBottom();
	}

	/**
	 * Get the inner right of this style.
	 * 
	 * @return the right.
	 */
	public float getInnerRight() {
		return getFormat().getWidth() - getMargins().getRight();
	}
}
