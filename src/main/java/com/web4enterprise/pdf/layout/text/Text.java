package com.web4enterprise.pdf.layout.text;

import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;

public interface Text extends ParagraphEmbeddable {	
	public static final String NEW_LINE = "\n";	
	
	void setStyle(TextStyle style);
	
	void setString(String string);	
}
