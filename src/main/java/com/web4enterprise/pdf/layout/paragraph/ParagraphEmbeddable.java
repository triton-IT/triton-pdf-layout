package com.web4enterprise.pdf.layout.paragraph;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;

public interface ParagraphEmbeddable {	
	void setLink(DocumentEmbeddable documentEmbeddable);
	
	void addFootNote(FootNote footNote);
	void addFootNote(Paragraph... paragraphs);
}
