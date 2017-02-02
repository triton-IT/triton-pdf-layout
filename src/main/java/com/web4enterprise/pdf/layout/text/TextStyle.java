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

package com.web4enterprise.pdf.layout.text;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.style.Style;

/**
 * Defines a style for a text.
 * 
 * 
 * @author Régis Ramillien
 */
public class TextStyle implements Style, Cloneable {
	/**
	 * The font used to render text under this style.
	 */
	protected Font font;
	/**
	 * The font variant used to render text under this style.
	 */
	protected FontsVariant fontVariant;
	/**
	 * The font size used to render text under this style.
	 */
	protected Float fontSize;
	/**
	 * The font color used to render text under this style.
	 */
	protected Color fontColor;
	/**
	 * The underline status of the text under this style.
	 */
	protected Boolean underlined;
	/**
	 * The underline color if underline is present.
	 */
	protected Color underlineColor;
	/**
	 * The script status of text under this style.
	 */
	protected TextScript script = TextScript.NORMAL;
	
	/**
	 * Create a default text style.
	 * The default style is "no style" and then style is inherited fromm paragraph.
	 */
	public TextStyle() {
	}
	
	/**
	 * Create a style with the given font.
	 * 
	 * @param font The font used to render text under this style.
	 */
	public TextStyle(Font font) {
		this.font = font;
	}
	
	/**
	 * Create a style with given font variant.
	 * 
	 * @param fontVariant The font variant used to render text under this style.
	 */
	public TextStyle(FontsVariant fontVariant) {
		this.fontVariant = fontVariant;
	}
	
	/**
	 * Create a style with given font and font size.
	 * 
	 * @param font The font variant used to render text under this style.
	 * @param fontSize The font size used to render text under this style.
	 */
	public TextStyle(Font font, float fontSize) {
		this.font = font;
		this.fontSize = fontSize;
	}
	
	/**
	 * Create a style with given font, font variant and font size.
	 * 
	 * @param font The font variant used to render text under this style.
	 * @param fontVariant The font variant used to render text under this style.
	 * @param fontSize The font size used to render text under this style.
	 */
	public TextStyle(Font font, FontsVariant fontVariant, float fontSize) {
		this.font = font;
		this.fontVariant = fontVariant;
		this.fontSize = fontSize;
	}

	/**
	 * Create a style with given font, font variant, font size and font color.
	 * 
	 * @param font The font variant used to render text under this style.
	 * @param fontVariant The font variant used to render text under this style.
	 * @param fontSize The font size used to render text under this style.
	 * @param fontColor The font color used to render text under this style.
	 */
	public TextStyle(Font font, FontsVariant fontVariant, float fontSize, Color fontColor) {
		this.font = font;
		this.fontVariant = fontVariant;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}
	
	/**
	 * Get the font of this style.
	 * 
	 * @return The font.
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Set the font of this style.
	 * If set to null, the font will be inherited from paragraph style.
	 * 
	 * @param font The font to set to this style.
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Get the font variant of this style.
	 * 
	 * @return The font variant.
	 */
	public FontsVariant getFontsVariant() {
		return fontVariant;
	}

	/**
	 * Set the font variant of this style.
	 * If set to null, the font variant will be inherited from paragraph style.
	 * 
	 * @param fontVariant The font variant for this style.
	 */
	public void setFontsVariant(FontsVariant fontVariant) {
		this.fontVariant = fontVariant;
	}

	/**
	 * Get the font size of this style.
	 * 
	 * @return The font size.
	 */
	public Float getFontSize() {
		return fontSize;
	}

	/**
	 * Set the font size of this style.
	 * If set to null, the font size will be inherited from paragraph style.
	 * 
	 * @param fontSize The font size.
	 */
	public void setFontSize(Float fontSize) {
		this.fontSize = fontSize;
	}
	
	/**
	 * Get the font color of this style.
	 * 
	 * @return The font color.
	 */
	public Color getFontColor() {
		return fontColor;
	}

	/**
	 * Set the font color of this style.
	 * If set to null, the font color will be inherited from paragraph style.
	 * 
	 * @param fontColor The color of font.
	 */
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * Get the underline state of this style.
	 * 
	 * @return True if text under this style must be underlined.
	 */
	public Boolean isUnderlined() {
		return underlined;
	}
	
	/**
	 * Set the underline state of this style.
	 * If set to null, the underline state will be inherited from paragraph style.
	 * 
	 * @param isUnderlined The state of underlining.
	 */
	public void setUnderlined(Boolean isUnderlined) {
		this.underlined = isUnderlined;
	}

	/**
	 * Get the underline color of this style.
	 * 
	 * @return The underline color.
	 */
	public Color getUnderlineColor() {
		return underlineColor;
	}

	/**
	 * Set the underline color of this style.
	 * If set to null, the underline color will be inherited from paragraph style.
	 * 
	 * @param underlineColor The color of underline.
	 */
	public void setUnderlineColor(Color underlineColor) {
		this.underlineColor = underlineColor;
	}

	/**
	 * Get the script state of this style.
	 * 
	 * @return The script state.
	 */
	public TextScript getScript() {
		return script;
	}

	/**
	 * Set the script state of this style.
	 * If set to null, the script state will be inherited from paragraph style.
	 * 
	 * @param script The script state.
	 */
	public void setScript(TextScript script) {
		this.script = script;
	}
	
	@Override
	public TextStyle clone() {
		TextStyle clone = new TextStyle(font, fontVariant, fontSize, fontColor);
		clone.setScript(script);
		clone.setUnderlineColor(underlineColor);
		clone.setUnderlined(underlined);
		
		return clone;
	}
}
