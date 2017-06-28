package com.web4enterprise.pdf.layout.image;

import com.web4enterprise.pdf.layout.paragraph.Paragraph;

/**
 * Data of an image.
 * This image data must be converted to an {@link Image} to be rendered in a {@link Paragraph}
 * 
 * 
 * @author Régis Ramillien
 */
public interface ImageData {
	/**
	 * Create an image from this data.
	 * 
	 * @return The image with a reference to this image data.
	 */
	Image createImage();
}
