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

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.image.ImageData;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.style.Style;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;

/**
 * Defines a paragraph on a document.
 * 
 * 
 * @author Régis Ramillien
 */
public interface Paragraph extends DocumentEmbeddable {
	/**
	 * Create an {@link Image} from an {@link ImageData}.
	 * 
	 * @param imageData The data to create {@link Image} from.
	 * @return The image with default values.
	 */
	Image createImage(ImageData imageData);
	/**
	 * Create an {@link Image} from an {@link ImageData} with given width and height.
	 * 
	 * @param imageData The data to create {@link Image} from.
	 * @param width The width to set to the image.
	 * @param height The height to set to the image.
	 * @return The image with given width and height.
	 */
	Image createImage(ImageData imageData, int width, int height);
	/**
	 * Create an {@link Image} from an {@link ImageData} with given width.
	 * The height is calculated so that ratio of the image is kept.
	 * 
	 * @param imageData The data to create {@link Image} from.
	 * @param width The width to set to the image.
	 * @return The image with given width and calculated height.
	 */
	Image createImageForWidth(ImageData imageData, int width);
	/**
	 * Create an {@link Image} from an {@link ImageData} with given height.
	 * The width is calculated so that ratio of the image is kept.
	 * 
	 * @param imageData The data to create {@link Image} from.
	 * @param height The height to set to the image.
	 * @return The image with given height and calculated width.
	 */
	Image createImageForHeight(ImageData imageData, int height);
	/**
	 * Create a {@link Text} for the paragraph.
	 * 
	 * @param value The value of text to create.
	 * @return The {@link Text} created.
	 */
	Text createText(String value);
	/**
	 * Create some {@link Text} for the paragraph.
	 * 
	 * @param values The values of texts to create.
	 * @return A {@link Text} for each given value.
	 */
	Text[] createText(String... values);
	/**
	 * Create a {@link Text} with given {@link Style} for the paragraph.
	 * 
	 * @param style The style to set to text. 
	 * @param value The value of text to create.
	 * @return The {@link Text} created.
	 */
	Text createText(TextStyle style, String value);
	/**
	 * Create some {@link Text} for the paragraph with given values.
	 * 
	 * @param style The style to set to text.
	 * @param values The values of texts to create.
	 * @return A {@link Text} for each given value.
	 */
	Text[] createText(TextStyle style, String... values);
	
	/**
	 * Add some {@link ParagraphEmbeddable} to this paragraph.
	 * 
	 * @param embeddables The paragraph embeddables to add to.
	 */
	void addEmbeddable(ParagraphEmbeddable... embeddables);

	/**
	 * Set the {@link Style} of this paragraph.
	 * 
	 * @param style The style to set.
	 */
	void setStyle(ParagraphStyle style);

	/**
	 * Add a stop to this paragraph.
	 * 
	 * @param stop The stop to add.
	 */
	void addStop(Stop stop);
	
	/**
	 * Go to next stop of this paragraph and add some texts.
	 * 
	 * @param texts The texts to add after next stop.
	 */
	void nextStop(String... texts);
	
	/**
	 * Go to next stop of this paragraph and add some {@link ParagraphEmbeddable}.
	 * 
	 * @param embeddables Paragraph embeddables to add after next stop.
	 */
	void nextStop(PdfParagraphEmbeddable... embeddables);
	
	/**
	 * Defines a link to a {@link DocumentEmbeddable}.
	 * 
	 * @param documentEmbeddable The target of the link.
	 */
	void setLink(DocumentEmbeddable documentEmbeddable);
}
