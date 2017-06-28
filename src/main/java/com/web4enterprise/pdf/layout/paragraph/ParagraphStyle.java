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

import static com.web4enterprise.report.commons.font.FontCache.TIMES_ROMAN;

import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.Margins;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.report.commons.font.Font;
import com.web4enterprise.report.commons.font.FontCache;
import com.web4enterprise.report.commons.font.FontVariant;

/**
 * Defines the style of a {@link Paragraph}.
 * 
 * 
 * @author Régis Ramillien
 */
public class ParagraphStyle extends TextStyle {
	/**
	 * The alignment of the {@link Paragraph}.
	 * Defaults to {@link Alignment#LEFT}
	 */
	protected Alignment alignment = Alignment.LEFT;
	/**
	 * The line spacing of the {@link Paragraph}.
	 * Defaults to 1.0.
	 */
	protected float lineSpacing = 1.0f;
	/**
	 * The margins to apply on the {@link Paragraph}.
	 * Defaults to 0 for each margin.
	 */
	protected Margins margins = new Margins(0);
	/**
	 * The margin of first line of the {@link Paragraph}.
	 * Defaults to 0;
	 */
	protected int firstLineMargin = 0;
	
	/**
	 * Creates a paragraph style with default values.
	 * Font defaults to Times-new-roman.
	 * Variant defaults to plain.
	 * Color defaults to black.
	 */
	public ParagraphStyle() {
		super(TIMES_ROMAN, FontVariant.PLAIN, 12, Color.BLACK);
	}
	
	/**
	 * Creates a paragraph style with given {@link FontCache} and font size.
	 * 
	 * @param font The {@link FontCache} to set to style.
	 * @param fontSize The font size to set to style.
	 */
	public ParagraphStyle(Font font, float fontSize) {
		super(font, FontVariant.PLAIN, fontSize, Color.BLACK);
	}

	/**
	 * Creates a paragraph style with given {@link FontCache}, {@link FontVariant} and font size.
	 * 
	 * @param font The {@link FontCache} to set to style.
	 * @param style The {@link FontVariant} to set to paragraph style.
	 * @param fontSize The font size to set to style.
	 */
	public ParagraphStyle(Font font, int style, float fontSize) {
		super(font, style, fontSize, Color.BLACK);
	}

	/**
	 * Creates a paragraph style with given {@link FontCache}, {@link FontVariant}, font size and font {@link Color}.
	 * 
	 * @param font The {@link FontCache} to set to style.
	 * @param style The {@link FontVariant} to set to paragraph style.
	 * @param fontSize The font size to set to style.
	 * @param fontColor The font {@link Color} to set to style.
	 */
	public ParagraphStyle(Font font, int style, float fontSize, Color fontColor) {
		super(font, style, fontSize, fontColor);
	}

	/**
	 * Get the {@link Alignment} of this style.
	 * 
	 * @return The {@link Alignment} of this style.
	 */
	public Alignment getAlignment() {
		return alignment;
	}

	/**
	 * Set the {@link Alignment} of this style.
	 * 
	 * @param alignment The {@link Alignment} to set.
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	/**
	 * Get the line spacing of this style.
	 * 
	 * @return The line spacing
	 */
	public float getLineSpacing() {
		return lineSpacing;
	}

	/**
	 * Set line spacing of this style.
	 * 
	 * @param lineSpacing The line spacing.
	 */
	public void setLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	/**
	 * Get the {@link Margins} of this style.
	 * 
	 * @return The {@link Margins}.
	 */
	public Margins getMargins() {
		return margins;
	}

	/**
	 * Set the {@link Margins} of this style.
	 * 
	 * @param margins The {@link Margins} to set to this style.
	 */
	public void setMargins(Margins margins) {
		this.margins = margins;
	}
	
	/**
	 * Get the left margin of the first line of this paragraph.
	 * 
	 * @return The margin.
	 */
	public int getFirstLineMargin() {
		return firstLineMargin;
	}

	/**
	 * Set the left margin of first line.
	 * 
	 * @param firstLineMargin The margin.
	 */
	public void setFirstLineMargin(int firstLineMargin) {
		this.firstLineMargin = firstLineMargin;
	}
	
	/**
	 * Get the font variant of this style.
	 * 
	 * @return The font variant.
	 */
	public FontVariant getFontVariant() {
		return font.getVariant(fontVariantId);
	}
}
