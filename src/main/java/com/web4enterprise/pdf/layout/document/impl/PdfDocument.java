package com.web4enterprise.pdf.layout.document.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.exception.BadOperationException;
import com.web4enterprise.pdf.layout.exception.BadResourceException;
import com.web4enterprise.pdf.layout.exception.DocumentGenerationException;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.page.PageFooter;
import com.web4enterprise.pdf.layout.page.PageHeader;
import com.web4enterprise.pdf.layout.page.PageStyle;

public class PdfDocument implements Document {
	private static final Logger LOGGER = Logger.getLogger(PdfDocument.class.getName());
	
	protected Pdf pdf = new Pdf();
	
	protected Layouter layouter = new Layouter(pdf);

	protected float blockStartX = 0;
	
	/**
	 * List of stops per page.
	 */
	protected Map<Page, List<Float>> verticalStops = new HashMap<>();
	
	protected int currentStopId = 0;
	
	public PdfDocument() {
		pdf.setCreator("http://simplypdf-layout.web4enterprise.com");
	}

	@Override
	public void setTitle(String title) {
		pdf.setTitle(title);
	}

	@Override
	public void setAuthor(String author) {
		pdf.setAuthor(author);
	}

	@Override
	public void setSubject(String subject) {
		pdf.setSubject(subject);
	}

	@Override
	public void setCreator(String creator) {
		pdf.setCreator(creator);
	}

	@Override
	public void setProducer(String producer) {
		pdf.setProducer(producer);
	}

	@Override
	public void setCreationDate(Date creationDate) {
		pdf.setCreationDate(creationDate);
	}

	@Override
	public void setModificationDate(Date modificationDate) {
		pdf.setModificationDate(modificationDate);
	}

	@Override
	public void addKeyword(String keyword) {
		pdf.addKeyword(keyword);
	}

	@Override
	public void addMetaData(String key, String value) {
		pdf.addMetaData(key, value);
	}

	@Override
	public void addPage() {
		Page currentPage = layouter.getCurrentPage();
		if(currentPage != null) {
			addPage(currentPage.getStyle(), currentPage.getHeader(), currentPage.getFooter());
		} else {
			addPage(PageStyle.A4_PORTRAIT, null, null);
		}
	}

	@Override
	public void addPage(PageStyle pageStyle) {
		Page currentPage = layouter.getCurrentPage();
		if(currentPage != null) {
			addPage(pageStyle, currentPage.getHeader(), currentPage.getFooter());
		} else {
			addPage(pageStyle, null, null);
		}
	}

	@Override
	public void addPage(PageHeader pageHeader, PageFooter pageFooter) {
		Page currentPage = layouter.getCurrentPage();
		if(currentPage != null) {
			addPage(currentPage.getStyle(), pageHeader, pageFooter);
		} else {
			addPage(null, pageHeader, pageFooter);
		}
	}

	@Override
	public void addPage(PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {		
		currentStopId = 0;
		layouter.addPage(pageStyle, pageHeader, pageFooter);
	}

	@Override
	public void addVerticalStop(float position) {
		List<Float> stops = verticalStops.get(layouter.getCurrentPage());
		
		if(stops == null) {
			stops = new ArrayList<Float>();
			verticalStops.put(layouter.getCurrentPage(), stops);
		}
		
		stops.add(position);
	}
	
	@Override
	public void nextVerticalStop() throws BadOperationException {
		List<Float> stops = verticalStops.get(layouter.getCurrentPage());
		
		if(stops != null && stops.size() > currentStopId) {
			float stopPosition = stops.get(currentStopId);
			if(stopPosition < layouter.getCursorPosition().getY()) {
				layouter.getCursorPosition().setY(stopPosition);
			}
		} else {
			throw new BadOperationException("There is no vertical stop available.");
		}
		
		currentStopId++;
	}

	@Override
	public Image createImage(InputStream imageInputStream) throws BadResourceException {
		try {
			return new Image(pdf.createImage(imageInputStream));
		} catch(PdfGenerationException e) {
			throw new BadResourceException("Cannot read image.", e); 
		}
	}

	@Override
	public void addElement(Element element) throws BadOperationException {
		Page page = layouter.getCurrentPage();
		if(page == null) {
			throw new BadOperationException("You can't add an element without having created a page first.");
		}
		
		PageStyle pageStyle = page.getStyle();
		float top = pageStyle.getMargins().getTop();
		
		PageFooter pageFooter = page.getFooter();
		if(pageFooter != null) {
			top -= pageFooter.getHeight(layouter, pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}
		
		float bottom = pageStyle.getMargins().getBottom();
		if(pageFooter != null) {
			bottom += pageFooter.getHeight(layouter, pageStyle.getFormat().getWidth() - 
					pageStyle.getMargins().getLeft() - 
					pageStyle.getMargins().getRight());
		}

		PageFootNotes pageFootNotes = page.getFootNotes();
		element.layout(layouter, 
				new Rect(top, 
					pageStyle.getMargins().getLeft(),
					bottom,
					pageStyle.getFormat().getWidth() - pageStyle.getMargins().getRight()),
				layouter.getCursorPosition().getY(),
				pageFootNotes);
	}

	@Override
	public void write(OutputStream out) throws DocumentGenerationException {
		finish();
		try {
			pdf.write(out);
		} catch(PdfGenerationException e) {
			throw new DocumentGenerationException("Cannot generate document", e);
		}
	}
	
	protected void finish() {
		Page page = layouter.getCurrentPage();
		if(page == null) {
			LOGGER.warning("Finishing a document without any page.");
		} else {
			page.layoutEndOfPage();
		}
	}
}
