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
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;

public interface Paragraph extends DocumentEmbeddable {
	Image createImage(Image image);
	Image createImage(Image image, int width, int height);
	Image createImageForWidth(Image image, int width);
	Image createImageForHeight(Image image, int height);
	Text createText(String value);
	Text[] createText(String... values);
	Text createText(TextStyle style, String value);
	Text[] createText(TextStyle style, String... values);
	
	void addEmbeddable(ParagraphEmbeddable... embeddables);

	void setStyle(ParagraphStyle style);

	void addStop(Stop stop);
	
	void nextStop(String... texts);
	
	void nextStop(PdfParagraphEmbeddable... embeddables);
	
	void setLink(DocumentEmbeddable documentEmbeddable);
}
