package com.web4enterprise.pdf.layout.image;

import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;

public interface Image extends ParagraphEmbeddable {
	void setWidth(int width);
	
	void setWidth(int width, boolean keepRatio);
	
	void setHeight(int height);	
	
	void setHeight(int height, boolean keepRatio);
	
	Image clone();
}
