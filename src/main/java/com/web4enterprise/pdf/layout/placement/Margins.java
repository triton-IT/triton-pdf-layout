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

package com.web4enterprise.pdf.layout.placement;

/**
 * Defines the margin of an element.
 * 
 * 
 * @author Régis Ramillien
 */
public class Margins {
	/**
	 * Around 13.333 inch and 33.867 cm.
	 */
	public static Margins A0 = new Margins(960);
	/**
	 * Around 6.666 inch and 16.933 cm.
	 */
	public static Margins A1 = new Margins(480);
	/**
	 * Around 3.333 inch and 8.467 cm.
	 */
	public static Margins A2 = new Margins(240);
	/**
	 * Around 1.666 inch and 4.233 cm.
	 */
	public static Margins A3 = new Margins(120);
	/**
	 * 60 means about 0.833 inch and 2.116 cm.
	 */
	public static Margins A4 = new Margins(60);
	/**
	 * Around 0.416 inch and 1.058 cm.
	 */
	public static Margins A5 = new Margins(30);
	/**
	 * Around 0.208 inch and 0.529 cm.
	 */
	public static Margins A6 = new Margins(15);
	/**
	 * Around 0.111 inch and 0.282 cm.
	 */
	public static Margins A7 = new Margins(8);
	/**
	 * Around 0.055 inch and 0.141 cm.
	 */
	public static Margins A8 = new Margins(4);
	/**
	 * Around 0 inch and 0 cm.
	 */
	public static Margins Zero = new Margins(0);
	
	/**
	 * Size of left border.
	 */
	protected float left;
	/**
	 * Size of right border.
	 */
	protected float right;
	/**
	 * Size of top border.
	 */
	protected float top;
	/**
	 * Size of bottom border.
	 */
	protected float bottom;
	
	/**
	 * Construct margins with same value for all around.
	 * 
	 * @param allAround The size of all borders.
	 */
	public Margins(float allAround) {
		this.left = allAround;
		this.right = allAround;
		this.top = allAround;
		this.bottom = allAround;
	}
	
	/**
	 * Construct a margin with one size for each border.
	 * 
	 * @param top The size for top border.
	 * @param left The size for left border.
	 * @param bottom The size for bottom border.
	 * @param right The size for right border.
	 */
	public Margins(float top, float left, float bottom, float right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}
	
	/**
	 * Get the size of the left border.
	 * 
	 * @return The size of left border.
	 */
	public float getLeft() {
		return left;
	}
	
	/**
	 * Set the size of the left border.
	 * 
	 * @param size The size to set.
	 */
	public void setLeft(float size) {
		this.left = size;
	}

	/**
	 * Get the size of the right border.
	 * 
	 * @return The size of right border.
	 */
	public float getRight() {
		return right;
	}

	/**
	 * Set the size of the right border.
	 * 
	 * @param size The size to set.
	 */
	public void setRight(float size) {
		this.right = size;
	}

	/**
	 * Get the size of the top border.
	 * 
	 * @return The size of top border.
	 */
	public float getTop() {
		return top;
	}

	/**
	 * Set the size of the top border.
	 * 
	 * @param size The size to set.
	 */
	public void setTop(float size) {
		this.top = size;
	}

	/**
	 * Get the size of the bottom border.
	 * 
	 * @return The size of bottom border.
	 */
	public float getBottom() {
		return bottom;
	}

	/**
	 * Set the size of the bottom border.
	 * 
	 * @param size The size to set.
	 */
	public void setBottom(float size) {
		this.bottom = size;
	}
}
