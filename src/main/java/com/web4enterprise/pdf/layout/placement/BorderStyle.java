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

import com.web4enterprise.pdf.core.styling.Color;

/**
 * Definens a style for borders.
 * 
 * 
 * @author Régis Ramillien
 */
public class BorderStyle {
	/**
	 * No border.
	 */
	public static final BorderStyle NONE = new BorderStyle(0, LineStyle.NONE);

	/**
	 * Thin and solid borders.
	 */
	public static final BorderStyle THIN_SOLID = new BorderStyle();
	
	/**
	 * The width of a border.
	 * Defaults to 1.0.
	 */
	public float width = 1.0f;
	/**
	 * The line style of the border
	 * Defaults to {@link LineStyle#SOLID}.
	 */
	public LineStyle lineStyle = LineStyle.SOLID;
	/**
	 * The color of the border.
	 * Defaults to {@link Color#BLACK}.
	 */
	public Color color = Color.BLACK;
	
	/**
	 * Creates a border style with default values.
	 * Equivalent to {@link BorderStyle#THIN_SOLID}.
	 */
	public BorderStyle() {
	}
	
	/**
	 * Creates a border style with default color and line style.
	 * 
	 * @param width The width of the border.
	 */
	public BorderStyle(float width) {
		this.width = width;
	}

	/**
	 * Creates a border with default width and line style.
	 * 
	 * @param color The color of border.
	 */
	public BorderStyle(Color color) {
		this.color = color;
	}

	/**
	 * Creates a border with default width and color.
	 * 
	 * @param lineStyle The line style of the border.
	 */
	public BorderStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	/**
	 * Creates a border with default line style.
	 * 
	 * @param width The width of the border.
	 * @param color The color of border.
	 */
	public BorderStyle(float width, Color color) {
		this.width = width;
		this.color = color;
	}

	/**
	 * Creates a border with default color.
	 * 
	 * @param width The width of the border.
	 * @param lineStyle The line style of the border.
	 */
	public BorderStyle(float width, LineStyle lineStyle) {
		this.width = width;
		this.lineStyle = lineStyle;
	}

	/**
	 * Creates a border with specified width, color and line style.
	 * 
	 * @param width The width of the border.
	 * @param color The color of border.
	 * @param lineStyle The line style of the border.
	 */
	public BorderStyle(float width, Color color, LineStyle lineStyle) {
		this.width = width;
		this.lineStyle = lineStyle;
		this.color = color;
	}

	/**
	 * Get the width of the border.
	 * 
	 * @return The width.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Set the width of the border.
	 * 
	 * @param width The width.
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Get the line style of the border.
	 * 
	 * @return The line style.
	 */
	public LineStyle getLineStyle() {
		return lineStyle;
	}

	/**
	 * Set the line style of the border.
	 * 
	 * @param lineStyle The line style.
	 */
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	/**
	 * Get the color of the border.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the color of the border.
	 * 
	 * @param color The color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}
