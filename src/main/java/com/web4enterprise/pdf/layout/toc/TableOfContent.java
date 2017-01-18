package com.web4enterprise.pdf.layout.toc;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.style.Style;

public interface TableOfContent extends DocumentEmbeddable {	
	/**
	 * Associate a level to a paragraph style.
	 * 
	 * @param level
	 * @param style
	 */
	public void addLevel(int level, Style... style);
}
