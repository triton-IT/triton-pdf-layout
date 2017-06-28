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

package com.web4enterprise.pdf.layout.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.web4enterprise.pdf.layout.image.ImageData;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.toc.TableOfContent;
import com.web4enterprise.report.commons.exception.BadOperationException;
import com.web4enterprise.report.commons.exception.BadResourceException;
import com.web4enterprise.report.commons.exception.DocumentGenerationException;

/**
 * The interface defining a whole document. 
 * 
 * @author Régis Ramillien
 */
public interface Document {	
	/**
	 * Set the title of document.
	 * 
	 * @param title The title of document meta-data.
	 */
	void setTitle(String title);
	
	/**
	 * Set the author of document.
	 * 
	 * @param author The author of document meta-data.
	 */
	void setAuthor(String author);
	
	/**
	 * Set the subject of document.
	 * 
	 * @param subject The subject of document meta-data.
	 */
	void setSubject(String subject);

	/**
	 * Set the creator of document.
	 * 
	 * @param creator The creator of document meta-data.
	 */
	void setCreator(String creator);

	/**
	 * Set the producer of document.
	 * 
	 * @param producer The producer of document meta-data.
	 */
	void setProducer(String producer);

	/**
	 * Add a date of creation to document meta-data.
	 * 
	 * @param creationDate The date of creation to add to document meta-data.
	 */
	void setCreationDate(Date creationDate);

	/**
	 * Set the modification date of document.
	 * 
	 * @param modificationDate The modification date of document meta-data.
	 */
	void setModificationDate(Date modificationDate);
	
	/**
	 * Add a keyword to document.
	 * 
	 * @param keyword The keyword to add to document meta-data.
	 */
	void addKeyword(String keyword);
	
	/**
	 * Add a custom meta-data to document.
	 * 
	 * @param key The key of custom meta-data to add to document.
	 * @param value The value of custom meta-data to add to document.
	 */
	void addMetaData(String key, String value);
	
	/**
	 * Add a page to the document.
	 * All further operation of layout will starts on this new page.
	 * 
	 * @return The next section.
	 */
	Section nextPage();
	
	/**
	 * Add a page to the document.
	 * All further operation  of layout will starts on this new page.
	 * All next pages will use the same styling, headers and footers than the one defined in parameter unless otherwise specified.
	 * 
	 * @param pdfSection The section definition for next pages.
	 * @return The next section.
	 */
	Section nextPage(Section pdfSection);
	
	/**
	 * Tell the document to place the next element on the next defined vertical stop.
	 * If there is no more vertical stop defined, the method throws a {@link BadOperationException}.
	 * 
	 * @throws BadOperationException When no vertical stop is defined or no more vertical stop is available.
	 */
	void nextVerticalStop() throws BadOperationException;
	
	/**
	 * Create an {@link ImageData} from an input stream.
	 * 
	 * @param imageInputStream The input stream where image data is available.
	 * @return The {@link ImageData} created in document.
	 * @throws BadResourceException When image input stream cannot be read correctly.
	 */
	ImageData createImage(InputStream imageInputStream) throws BadResourceException;
	
	/**
	 * Create a paragraph with default values.
	 * 
	 * @return A new paragraph.
	 */
	Paragraph createParagraph();
	/**
	 * Create a paragraph from given style.
	 * 
	 * @param style The style to apply to paragraph.
	 * @return A new styled paragraph.
	 */
	Paragraph createParagraph(ParagraphStyle style);
	/**
	 * Create a paragraph from given strings as text.
	 * 
	 * @param texts The strings to add to paragraph.
	 * @return A new paragraph populated with strings.
	 */
	Paragraph createParagraph(String... texts);
	/**
	 * Create a paragraph from given style and strings as text.
	 * 
	 * @param style The style to apply to paragraph.
	 * @param texts The strings to add to paragraph.
	 * @return A new styleld paragraph populated with strings.
	 */
	Paragraph createParagraph(ParagraphStyle style, String... texts);
	/**
	 * Create a paragraph from given embeddables.
	 * 
	 * @param paragraphEmbeddables The embeddables to add to paragraph.
	 * @return A new paragraph populated with embeddables.
	 */
	Paragraph createParagraph(ParagraphEmbeddable... paragraphEmbeddables);
	/**
	 * Create a paragraph from given style and embeddables.
	 * 
	 * @param style The style to apply to paragraph.
	 * @param ParagraphEmbeddable The embeddables to add to paragraph.
	 * @return A new styled paragraph populated with embeddables.
	 */
	Paragraph createParagraph(ParagraphStyle style, ParagraphEmbeddable... ParagraphEmbeddable);
	
	/**
	 * Create an empty table.
	 * 
	 * @return A new empty table.
	 */
	Table createTable();
	
	/**
	 * Create a page header.
	 * 
	 * @return A new empty page header.
	 */
	PageHeader createPageHeader();
	
	/**
	 * Create a page footer.
	 * 
	 * @return A new empty page footer.
	 */
	PageFooter createPageFooter();
	
	/**
	 * Create a table of content.
	 * 
	 * @return A new empty table of content.
	 */
	TableOfContent createTableOfContent();
	
	/**
	 * Layout an element to the document.
	 * 
	 * @param embeddable The {@link DocumentEmbeddable} to add to this document.
	 * @throws BadOperationException When an illegal operation is performed.
	 */
	void addEmbeddable(DocumentEmbeddable embeddable) throws BadOperationException;
	
	/**
	 * Write document to an outputStream.
	 * 
	 * @param out The output stream to write document to.
	 * @throws DocumentGenerationException When document cannot be generated.
	 */
	void write(OutputStream out) throws DocumentGenerationException;
}
