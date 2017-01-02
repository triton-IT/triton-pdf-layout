package com.web4enterprise.pdf.layout.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.page.Page;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.page.PageFooter;
import com.web4enterprise.pdf.layout.page.PageHeader;
import com.web4enterprise.pdf.layout.page.PageStyle;

public class Document {
	protected Pdf document = new Pdf();
	protected Page currentPage;
	protected int currentPageId;
	protected PageStyle pageStyle = PageStyle.A4_PORTRAIT;

	protected float blockStartX = 0;
	protected float blockStartY = 0;
	
	protected PageHeader pageHeader = null;
	protected PageFooter pageFooter = null;
	protected PageFootNotes pageFootNotes = new PageFootNotes();
	
	protected int currentFootNoteId = 1;
	
	/**
	 * List of stops per page.
	 */
	protected Map<Integer, List<Float>> stops = new HashMap<>();
	
	protected int currentStopId = 0;
	
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
		addPage(pageStyle, pageHeader, pageFooter);
	}
	
	public void addPage(PageStyle pageStyle) {
		addPage(pageStyle, pageHeader, pageFooter);
	}
	
	public void addPage(PageHeader pageHeader, PageFooter pageFooter) {
		addPage(pageStyle, pageHeader, pageFooter);
	}
	
	public void addPage(PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {
		float pageWidth = pageStyle.getFormat().getWidth() - 
				pageStyle.getMargins().getLeft() - 
				pageStyle.getMargins().getRight();
		
		this.pageHeader = pageHeader;
		if(currentPage != null) {
			layoutEndOfPage(pageWidth);
			
			//Clear and start a new footNotes now that page has been rendered.
			pageFootNotes.clear();
			pageFootNotes.setWidth(pageWidth);
		}
		
		clearFootNoteId();
		
		this.pageFooter = pageFooter;
		this.pageStyle = pageStyle;
		
		currentPageId++;
		currentStopId = 0;
		
		layoutNewPage(pageWidth);
	}
	
	public void addStopHeight(float position) {
		List<Float> pageStops = stops.get(currentPageId);
		
		if(pageStops == null) {
			pageStops = new ArrayList<Float>();
			stops.put(currentPageId, pageStops);
		}
		
		pageStops.add(position);
	}
	
	public void nextStopHeight() {
		List<Float> pageStops = stops.get(currentPageId);
		
		if(pageStops != null) {
			if(pageStops.size() > currentStopId) {
				float stopPosition = pageStops.get(currentStopId);
				if(stopPosition < blockStartY) {
					blockStartY = stopPosition;
				}
			}
		}
		
		currentStopId++;
	}
	
	public String generateFootNoteId() {
		return String.valueOf(currentFootNoteId++);
	}
	
	public void clearFootNoteId() {
		currentFootNoteId = 1;
	}
	
	protected void layoutFooter(float pageWidth) {
		///If we have a footer to layout.
		 if(pageFooter != null) {
			float pageFooterHeight = pageFooter.getHeight(this, pageWidth);

			//We do not allow footNotes on footers. This will change page layouting and its too late for this.
			pageFooter.layout(this, 
					new Rect(pageStyle.getMargins().getBottom() + pageFooterHeight, 
							pageStyle.getMargins().getLeft(),
							pageStyle.getMargins().getBottom(),
							pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
					pageStyle.getMargins().getBottom() + pageFooterHeight,
					null);
		}
	}
	
	protected void layoutFootNotes(float pageWidth) {
		///If we have a footer to layout.
		 if(!pageFootNotes.isEmpty()) {
			float pageFooterHeight = pageFooter.getHeight(this, pageWidth);
			float pageFootNotesHeight = pageFootNotes.getHeight(this, pageWidth);

			pageFootNotes.layout(this, 
					new Rect(pageStyle.getMargins().getBottom() + pageFooterHeight + pageFootNotesHeight, 
							pageStyle.getMargins().getLeft(),
							pageStyle.getMargins().getBottom() + pageFooterHeight,
							pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
					pageStyle.getMargins().getBottom() + pageFooterHeight + pageFootNotesHeight,
					pageFootNotes);
		}
	}
	
	protected void layoutNewPage(float pageWidth) {
		currentPage = document.createPage(pageStyle.getFormat().getWidth(), pageStyle.getFormat().getHeight());
		blockStartY = pageStyle.getFormat().getHeight() - pageStyle.getMargins().getTop();
		
		if(pageHeader != null) {
			pageHeader.layout(this, 
					new Rect(pageStyle.getMargins().getTop(), 
							pageStyle.getMargins().getLeft(),
							pageStyle.getMargins().getBottom(),
							pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
						blockStartY,
						pageFootNotes);
			blockStartY -= pageHeader.getHeight(this, pageWidth);
		}
	}
	
	protected void layoutEndOfPage(float pageWidth) {
		layoutFootNotes(pageWidth);
		layoutFooter(pageWidth);
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
			top -= pageFooter.getHeight(this, pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}
		
		float bottom = pageStyle.getMargins().getBottom();
		if(pageFooter != null) {
			bottom += pageFooter.getHeight(this, pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}
		
		blockStartY = element.layout(this, 
				new Rect(top, 
					pageStyle.getMargins().getLeft(),
					bottom,
					pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
				blockStartY,
				pageFootNotes);
	}

	public Image createImage(InputStream imageStream) throws PdfGenerationException {
		return new Image(document.createImage(imageStream));
	}
	
	public void finish() {
		float pageWidth = pageStyle.getFormat().getWidth() - 
				pageStyle.getMargins().getLeft() - 
				pageStyle.getMargins().getRight();
		
		if(currentPage != null) {
			layoutEndOfPage(pageWidth);
		}
	}

	public void write(OutputStream out) throws PdfGenerationException {
		document.write(out);
	}
}
