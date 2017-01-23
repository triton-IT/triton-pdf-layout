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

public class BorderStyle {
	public static final BorderStyle NONE = new BorderStyle();
	static {
		NONE.width = 0;
		NONE.lineStyle = LineStyle.NONE;
	}
	public static final BorderStyle THIN_SOLID = new BorderStyle();
	
	public float width = 1.0f;
	public LineStyle lineStyle = LineStyle.SOLID;
	public Color color = Color.BLACK;
	
	public BorderStyle() {
	}
	
	public BorderStyle(float width) {
		this.width = width;
	}

	public BorderStyle(Color color) {
		this.color = color;
	}

	public BorderStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public BorderStyle(float width, Color color) {
		this.width = width;
		this.color = color;
	}

	public BorderStyle(float width, LineStyle lineStyle) {
		this.width = width;
		this.lineStyle = lineStyle;
	}

	public BorderStyle(float width, Color color, LineStyle lineStyle) {
		this.width = width;
		this.lineStyle = lineStyle;
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
