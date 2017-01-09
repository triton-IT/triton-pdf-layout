package com.web4enterprise.pdf.layout.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.web4enterprise.pdf.layout.exception.BadOperationException;
import com.web4enterprise.pdf.layout.exception.BadResourceException;
import com.web4enterprise.pdf.layout.exception.DocumentGenerationException;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.toc.TableOfContent;

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
	 * Add a keyword to document.
	 * 
	 * @param keyword The keyword to add to document meta-data.
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
	 * All further layouting operation will starts on this new page.
	 */
	void addPage();
	
	/**
	 * Add a page to the document.
	 * All further layouting operation will starts on this new page.
	 * All next pages will use the same styling than the one defined in parameter unless otherwise specified. 
	 * 
	 * @param pageStyle The style of page to set for this and subsequent pages.
	 */
	void addPage(PageStyle pageStyle);
	
	/**
	 * Add a page to the document.
	 * All further layouting operation will starts on this new page.
	 * All next pages will use the same headers and footers than the one defined in parameter unless otherwise specified. 
	 * 
	 * @param pageHeader The page header to set to this page.
	 * @param pageFooter The page footer to set to this page.
	 */
	void addPage(PageHeader pageHeader, PageFooter pageFooter);
	
	/**
	 * Add a page to the document.
	 * All further layouting operation will starts on this new page.
	 * All next pages will use the same styling, headers and footers than the one defined in parameter unless otherwise specified.
	 * 
	 * @param pageStyle The style of page to set for this and subsequent pages.
	 * @param pageHeader The page header to set to this page.
	 * @param pageFooter The page footer to set to this page.
	 */
	void addPage(PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter);
	
	/**
	 * Add a vertical stop to the current page.
	 * On a call to {@see nextVerticalStop}, the next element will be add to the next available vertical stop.
	 * 
	 * @param position The position in number of units of the vertical stopp to place.
	 */
	void addVerticalStop(float position);
	
	/**
	 * Tell the document to place the next element on the next defined vertical stop.
	 * If there is no more vertical stop defined, the method throws a {@see BadOperationException}.
	 * 
	 * @throws BadOperationException When no vertical stop is defined or no more vertical stop is available.
	 */
	void nextVerticalStop() throws BadOperationException;
	
	/**
	 * Create an image from an input stream.
	 * 
	 * @param imageStream The input stream where image data is available.
	 * @return The PdfImage created in document.
	 * @throws BadResourceException When image input stream cannot be read correctly.
	 */
	Image createImage(InputStream imageInputStream) throws BadResourceException;
	
	/**
	 * Create a paragraph with default values.
	 * 
	 * @return A new paragraph.
	 */
	Paragraph createParagraph();
	Paragraph createParagraph(ParagraphStyle style);
	Paragraph createParagraph(String... texts);
	Paragraph createParagraph(ParagraphStyle style, String... texts);
	Paragraph createParagraph(ParagraphEmbeddable... paragraphEmbeddables);
	Paragraph createParagraph(ParagraphStyle style, ParagraphEmbeddable... ParagraphEmbeddable);
	
	Table createTable();

	PageHeader createPageHeader();
	
	PageFooter createPageFooter();
	
	TableOfContent createTableOfContent();
	
	/**
	 * Layout an element to the document.
	 * 
	 * @param embeddable The {@see DocumentEmbeddable} to add to this document.
	 * @throws BadOperationException When an illegal operation is performed.
	 */
	void addEmbeddable(DocumentEmbeddable embeddable) throws BadOperationException;
	
	/**
	 * Write document to an outputStream.
	 * 
	 * @param out The output stream to write document to.
	 */
	void write(OutputStream out) throws DocumentGenerationException;
}
