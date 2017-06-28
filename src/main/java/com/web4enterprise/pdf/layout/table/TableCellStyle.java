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

package com.web4enterprise.pdf.layout.table;

import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.style.Style;

/**
 * Defines the style of a table cell.
 * 
 * 
 * @author Régis Ramillien
 */
public class TableCellStyle implements Style {
	/**
	 * A predefined style with thin and solid borders.
	 */
	public static final TableCellStyle THIN_SOLID_BORDERS = new TableCellStyle();
	
	/**
	 * The style of borders.
	 */
	protected BorderStyle[] bordersStyle = {BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID};
	/**
	 * The color of background.
	 */
	protected Color backgroundColor = null;
	
	/**
	 * Create a default style.
	 */
	public TableCellStyle() {
	}
	
	/**
	 * Create a style with default borders and specified background color.
	 * 
	 * @param backgroundColor The color to affect to cell background. 
	 */
	public TableCellStyle(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Create a style with specified border style for all borders and no background color and default background color.
	 * 
	 * @param borderStyle The style of border to set to each border.
	 */
	public TableCellStyle(BorderStyle borderStyle) {
		bordersStyle[0] = borderStyle;
		bordersStyle[1] = borderStyle;
		bordersStyle[2] = borderStyle;
		bordersStyle[3] = borderStyle;
	}
	
	/**
	 * Create a style with specified border style for all borders and specified background color.
	 * 
	 * @param borderStyle The style of border to set to each border.
	 * @param backgroundColor The background color.
	 */
	public TableCellStyle(BorderStyle borderStyle, Color backgroundColor) {
		bordersStyle[0] = borderStyle;
		bordersStyle[1] = borderStyle;
		bordersStyle[2] = borderStyle;
		bordersStyle[3] = borderStyle;
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Get the style for top border.
	 * 
	 * @return The border's style.
	 */
	public BorderStyle getTopBorderStyle() {
		return bordersStyle[0];
	}

	/**
	 * Get the style for left border.
	 * 
	 * @return The border's style.
	 */
	public BorderStyle getLeftBorderStyle() {
		return bordersStyle[1];
	}

	/**
	 * Get the style for bottom border.
	 * 
	 * @return The border's style.
	 */
	public BorderStyle getBottomBorderStyle() {
		return bordersStyle[2];
	}

	/**
	 * Get the style for right border.
	 * 
	 * @return The border's style.
	 */
	public BorderStyle getRightBorderStyle() {
		return bordersStyle[3];
	}
	
	/**
	 * Set the style of borders.
	 * 
	 * @param top The style for top border.
	 * @param left The style for left border.
	 * @param bottom The style for bottom border.
	 * @param right The style for right border.
	 */
	public void setBordersStyle(BorderStyle top, BorderStyle left, BorderStyle bottom, BorderStyle right) {
		bordersStyle[0] = top;
		bordersStyle[1] = left;
		bordersStyle[2] = bottom;
		bordersStyle[3] = right;
	}

	/**
	 * Get the background color.
	 * 
	 * @return The color.
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set the background color.
	 * 
	 * @param backgroundColor The background color.
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
