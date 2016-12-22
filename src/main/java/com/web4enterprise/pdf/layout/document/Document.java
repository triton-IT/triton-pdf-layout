package com.web4enterprise.pdf.layout.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageFooter;
import com.web4enterprise.pdf.layout.page.PageHeader;
import com.web4enterprise.pdf.layout.page.PageStyle;

public class Document {
	protected Pdf document = new Pdf();
	protected Page currentPage;
	protected PageStyle pageStyle = PageStyle.A4_PORTRAIT;

	protected float blockStartX = 0;
	protected float blockStartY = 0;
	
	protected PageHeader pageHeader = null;
	protected PageFooter pageFooter = null;
	
	public Document() {
		document.setCreator("http://simplypdf-layout.web4enterprise.com");
	}
	
	/**
	 * Set the title of document.
	 * 
	 * @param title The title of document meta-data.
	 */
	public void setTitle(String title) {
		document.setTitle(title);
	}
	
	/**
	 * Set the author of document.
	 * 
	 * @param author The author of document meta-data.
	 */
	public void setAuthor(String author) {
		document.setAuthor(author);
	}
	
	/**
	 * Set the subject of document.
	 * 
	 * @param subject The subject of document meta-data.
	 */
	public void setSubject(String subject) {
		document.setSubject(subject);
	}
	
	/**
	 * Add a keyword to document.
	 * 
	 * @param keyword The keyword to add to document meta-data.
	 */
	public void addKeyword(String keyword) {
		document.addKeyword(keyword);
	}

	/**
	 * Set the creator of document.
	 * 
	 * @param creator The creator of document meta-data.
	 */
	public void setCreator(String creator) {
		document.setCreator(creator);
	}

	/**
	 * Set the producer of document.
	 * 
	 * @param producer The producer of document meta-data.
	 */
	public void setProducer(String producer) {
		document.setProducer(producer);
	}

	/**
	 * Add a keyword to document.
	 * 
	 * @param keyword The keyword to add to document meta-data.
	 */
	public void setCreationDate(Date creationDate) {
		document.setCreationDate(creationDate);
	}

	/**
	 * Set the modification date of document.
	 * 
	 * @param modificationDate The modification date of document meta-data.
	 */
	public void setModificationDate(Date modificationDate) {
		document.setModificationDate(modificationDate);
	}
	
	/**
	 * Add a custom meta-data to document.
	 * 
	 * @param key The key of custom meta-data to add to document.
	 * @param value The value of custom meta-data to add to document.
	 */
	public void addMetaData(String key, String value) {
		document.addMetaData(key, value);
	}
	
	public void addPage() {
		layoutFooter();
		layoutNewPage();
	}
	
	public void addPage(PageStyle pageStyle) {
		addPage(pageStyle, pageHeader, pageFooter);
	}
	
	public void addPage(PageHeader pageHeader, PageFooter pageFooter) {
		addPage(pageStyle, pageHeader, pageFooter);
	}
	
	public void addPage(PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {
		this.pageHeader = pageHeader;
		layoutFooter();
		this.pageFooter = pageFooter;
		this.pageStyle = pageStyle;
		layoutNewPage();
	}
	
	protected void layoutFooter() {
		///If this is not the first page and if we have a footer to layout.
		if(currentPage != null && pageFooter != null) {			
			float pageFooterHeight = pageFooter.getHeight(pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
			
			pageFooter.layout(this, 
					new Rect(pageStyle.getMargins().getBottom() + pageFooterHeight, 
							pageStyle.getMargins().getLeft(),
							pageStyle.getMargins().getBottom(),
							pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
						blockStartY);
		}
	}
	
	protected void layoutNewPage() {
		currentPage = document.createPage(pageStyle.getFormat().getWidth(), pageStyle.getFormat().getHeight());
		blockStartY = pageStyle.getFormat().getHeight() - pageStyle.getMargins().getTop();
		
		if(pageHeader != null) {
			pageHeader.layout(this, 
					new Rect(pageStyle.getMargins().getTop(), 
							pageStyle.getMargins().getLeft(),
							pageStyle.getMargins().getBottom(),
							pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
						blockStartY);
			blockStartY -= pageHeader.getHeight(pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}
	}
	
	public Page getCurrentPage() {
		return currentPage;
	}
	
	public float getCurrentStartY() {
		return blockStartY;
	}
	
	public void addElement(Element element) {
		float top = pageStyle.getMargins().getTop();
		if(pageFooter != null) {
			top -= pageFooter.getHeight(pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}
		
		float bottom = pageStyle.getMargins().getBottom();
		if(pageFooter != null) {
			bottom += pageFooter.getHeight(pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}
		
		blockStartY = element.layout(this, 
				new Rect(top, 
					pageStyle.getMargins().getLeft(),
					bottom,
					pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
				blockStartY);
	}

	public Image createImage(InputStream imageStream) throws PdfGenerationException {
		return new Image(document.createImage(imageStream));
	}
	
	public void finish() {
		///If this is not the first page and if we have a footer to layout.
		if(pageFooter != null) {
			float pageFooterHeight = pageFooter.getHeight(pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
			
			pageFooter.layout(this, 
					new Rect(pageStyle.getMargins().getBottom() + pageFooterHeight, 
							pageStyle.getMargins().getLeft(),
							pageStyle.getMargins().getBottom(),
							pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
						blockStartY);
		}
	}

	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
