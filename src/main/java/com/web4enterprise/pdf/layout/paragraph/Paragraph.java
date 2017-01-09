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
}
