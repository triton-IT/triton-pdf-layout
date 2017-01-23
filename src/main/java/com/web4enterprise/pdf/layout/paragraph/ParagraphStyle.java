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

package com.web4enterprise.pdf.layout.paragraph;

import static com.web4enterprise.pdf.core.font.Font.TIMES_ROMAN;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.font.FontVariant;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.Margins;
import com.web4enterprise.pdf.layout.text.TextStyle;

public class ParagraphStyle extends TextStyle {
	protected Alignment alignment = Alignment.LEFT;
	protected float lineSpacing = 1.0f;
	protected Margins margins = new Margins(0);
	protected int firstLineMargin = 0;
	
	public ParagraphStyle() {
		super(TIMES_ROMAN, FontsVariant.PLAIN, 12, Color.BLACK);
	}
	
	public ParagraphStyle(Font font, float fontSize) {
		super(font, FontsVariant.PLAIN, fontSize, Color.BLACK);
	}
	
	public ParagraphStyle(Font font, FontsVariant style, float fontSize) {
		super(font, style, fontSize, Color.BLACK);
	}
	
	public ParagraphStyle(Font font, FontsVariant style, float fontSize, Color fontColor) {
		super(font, style, fontSize, fontColor);
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public float getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	public Margins getMargins() {
		return margins;
	}

	public void setMargins(Margins margins) {
		this.margins = margins;
	}

	public int getFirstLineMargin() {
		return firstLineMargin;
	}

	public void setFirstLineMargin(int firstLineMargin) {
		this.firstLineMargin = firstLineMargin;
	}
	
	public FontVariant getFontVariant() {
		return font.getVariant(fontVariant);
	}
	
	/**
	 * Do not let null as default style but PLAIN.
	 */
	@Override
	public FontsVariant getFontStyle() {
		return (fontVariant!=null)?fontVariant:FontsVariant.PLAIN;
	}
}
