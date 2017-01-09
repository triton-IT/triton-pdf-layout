package com.web4enterprise.pdf.layout.document.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.exception.BadOperationException;
import com.web4enterprise.pdf.layout.exception.BadResourceException;
import com.web4enterprise.pdf.layout.exception.DocumentGenerationException;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.image.impl.PdfImage;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.page.PageFootNotes;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.VerticalStopsList;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.footer.impl.PdfPageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.header.impl.PdfPageHeader;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.impl.PdfTable;
import com.web4enterprise.pdf.layout.toc.TableOfContent;
import com.web4enterprise.pdf.layout.toc.impl.PdfTableOfContent;

public class PdfDocument implements Document {
	private static final Logger LOGGER = Logger.getLogger(PdfDocument.class.getName());
	
	protected Pdf pdf = new Pdf();
	
	protected Pager pager = new Pager(pdf);
	
	/**
	 * List of stops per page.
	 */
	protected Map<Page, VerticalStopsList> pagesVerticalStops = new HashMap<>();
	
	protected boolean finished = false;
	
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
		Page currentPage = pager.getCurrentPage();
		if(currentPage != null) {
			addPage(currentPage.getStyle(), currentPage.getHeader(), currentPage.getFooter());
		} else {
			addPage(PageStyle.A4_PORTRAIT, null, null);
		}
	}

	@Override
	public void addPage(PageStyle pageStyle) {
		Page currentPage = pager.getCurrentPage();
		if(currentPage != null) {
			addPage(pageStyle, currentPage.getHeader(), currentPage.getFooter());
		} else {
			addPage(pageStyle, null, null);
		}
	}

	@Override
	public void addPage(PageHeader pageHeader, PageFooter pageFooter) {
		Page currentPage = pager.getCurrentPage();
		if(currentPage != null) {
			addPage(currentPage.getStyle(), pageHeader, pageFooter);
		} else {
			addPage(null, pageHeader, pageFooter);
		}
	}

	@Override
	public void addPage(PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {
		pager.addPage(pageStyle, pageHeader, pageFooter);
	}

	@Override
	public void addVerticalStop(float position) {
		VerticalStopsList stops = pagesVerticalStops.get(pager.getCurrentPage());
		
		if(stops == null) {
			stops = new VerticalStopsList();
			pagesVerticalStops.put(pager.getCurrentPage(), stops);
		}
		
		stops.add(position);
	}
	
	@Override
	public void nextVerticalStop() {
		VerticalStopsList stops = pagesVerticalStops.get(pager.getCurrentPage());
		int currentStopIndex = stops.getCurrentIndex();
		
		if(stops != null && stops.size() > currentStopIndex) {
			float stopPosition = stops.get(currentStopIndex);
			if(stopPosition < pager.getCursorPosition().getY()) {
				pager.getCursorPosition().setY(stopPosition);
			}
		} else {
			throw new BadOperationException("There is no vertical stop available.");
		}
		
		stops.next();
	}

	@Override
	public Image createImage(InputStream imageInputStream) throws BadResourceException {
		try {
			return new PdfImage(pdf.createImage(imageInputStream));
		} catch(PdfGenerationException e) {
			throw new BadResourceException("Cannot read image.", e); 
		}
	}
	
	@Override
	public Paragraph createParagraph() {
		return new PdfParagraph();
	}

	@Override
	public Paragraph createParagraph(ParagraphStyle style) {
		return new PdfParagraph(style);
	}

	@Override
	public Paragraph createParagraph(String... texts) {
		return new PdfParagraph(texts);
	}

	@Override
	public Paragraph createParagraph(ParagraphStyle style, String... texts) {
		return new PdfParagraph(style, texts);
	}

	@Override
	public Paragraph createParagraph(ParagraphEmbeddable... paragraphEmbeddables) {
		PdfParagraph paragraph = new PdfParagraph();
		
		for(ParagraphEmbeddable embeddable : paragraphEmbeddables) {
			paragraph.addEmbeddable((PdfParagraphEmbeddable) embeddable);
		}
		
		return paragraph;
	}

	@Override
	public Paragraph createParagraph(ParagraphStyle style, ParagraphEmbeddable... paragraphEmbeddables) {
		PdfParagraph paragraph = new PdfParagraph(style);
		
		for(ParagraphEmbeddable embeddable : paragraphEmbeddables) {
			paragraph.addEmbeddable((PdfParagraphEmbeddable) embeddable);
		}
		
		return paragraph;
	}
	
	@Override
	public Table createTable() {
		return new PdfTable();
	}

	@Override
	public PageHeader createPageHeader() {
		return new PdfPageHeader();
	}

	@Override
	public PageFooter createPageFooter() {
		return new PdfPageFooter();
	}

	@Override
	public TableOfContent createTableOfContent() {
		return new PdfTableOfContent();
	}

	@Override
	public void addEmbeddable(DocumentEmbeddable embeddable) {
		Page page = pager.getCurrentPage();
		if(page == null) {
			throw new BadOperationException("You can't add an element without having created a page first.");
		}

		PageFootNotes pageFootNotes = page.getFootNotes();
		((PdfDocumentEmbeddable) embeddable).layout(pager, 
				page.getInnerRect(),
				pager.getCursorPosition().getY(),
				pageFootNotes);
	}

	@Override
	public void write(OutputStream out) throws DocumentGenerationException {
		//If write is called multiple times, call finish() only once.
		if(!finished) {
			finish();
			finished = true;
		}
		
		try {
			pdf.write(out);
		} catch(PdfGenerationException e) {
			throw new DocumentGenerationException("Cannot generate document", e);
		}
	}
	
	protected void finish() {
		Page page = pager.getCurrentPage();
		if(page == null) {
			LOGGER.warning("Finishing a document without any page.");
		} else {
			page.layoutEndOfPage();
		}
	}
}
