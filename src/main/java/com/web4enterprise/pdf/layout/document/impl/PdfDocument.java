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

package com.web4enterprise.pdf.layout.document.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.layout.document.Document;
import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.document.Section;
import com.web4enterprise.pdf.layout.document.impl.command.PdfAddEmbeddableCommand;
import com.web4enterprise.pdf.layout.document.impl.command.PdfNextVerticalStopCommand;
import com.web4enterprise.pdf.layout.exception.BadOperationException;
import com.web4enterprise.pdf.layout.exception.BadResourceException;
import com.web4enterprise.pdf.layout.exception.DocumentGenerationException;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.image.impl.PdfImage;
import com.web4enterprise.pdf.layout.page.PageFormat;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.footer.impl.PdfPageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.header.impl.PdfPageHeader;
import com.web4enterprise.pdf.layout.page.impl.PdfPage;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraph;
import com.web4enterprise.pdf.layout.paragraph.impl.PdfParagraphEmbeddable;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.impl.PdfTable;
import com.web4enterprise.pdf.layout.toc.TableOfContent;
import com.web4enterprise.pdf.layout.toc.impl.PdfTableOfContent;

/**
 * PDF implementation of Document API.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfDocument implements Document, PdfPager {
	/**
	 * Logger for class.
	 */
	private static final Logger LOGGER = Logger.getLogger(PdfDocument.class.getName());
	
	/**
	 * Document from low-level API.
	 */
	protected Pdf pdf = new Pdf();
	/**
	 * The list images to embed in document.
	 */
	protected List<com.web4enterprise.pdf.core.image.Image> images = new ArrayList<>();
	/**
	 * The list of embeddables for document.
	 */
	protected List<DocumentEmbeddable> embeddables = new ArrayList<>();
	/**
	 * The list of table of content of document.
	 */
	protected List<PdfTableOfContent> tablesOfContent = new ArrayList<>();
	/**
	 * The list of sections of document.
	 */
	protected List<PdfSection> pdfSections = new ArrayList<>();
	/**
	 * The section where document is currently writing on.
	 */
	protected PdfSection currentWriteSection = null;
	/**
	 * The section which is currently layed-out. 
	 */
	protected PdfSection currentLayOutSection = null;
	/**
	 * The page currently layed-out.
	 */
	protected PdfPage currentPage = null;
	/**
	 * The number of the page currently layed-out.
	 */
	protected int currentPageNumber = 0;
	/**
	 * The current position of cursor in current page.
	 */
	protected Point cursorPosition = new Point(0.0f, 0.0f);
	
	/**
	 * Create a document with default properties.
	 */
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
	public Section nextPage() {
		return nextSection();
	}

	@Override
	public Section nextPage(Section section) {
		return nextSection(section);
	}
	
	@Override
	public void nextVerticalStop() {
		getCurrentSection().add(new PdfNextVerticalStopCommand());
	}

	@Override
	public Image createImage(InputStream imageInputStream) throws BadResourceException {
		try {
			com.web4enterprise.pdf.core.image.Image image = pdf.createImage(imageInputStream);
			images.add(image);
			return new PdfImage(image);
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
		PdfTableOfContent toc = new PdfTableOfContent();
		tablesOfContent.add(toc);
		return toc;
	}

	@Override
	public void addEmbeddable(DocumentEmbeddable embeddable) {
		if(getCurrentSection() == null) {
			throw new BadOperationException("You can't add an element without having created a page first.");
		}
		if(!(embeddable instanceof PdfDocumentEmbeddable)) {
			throw new BadOperationException("You must add an embeddable useable by this API.");
		}
		
		embeddables.add(embeddable);
		getCurrentSection().add(new PdfAddEmbeddableCommand((PdfDocumentEmbeddable) embeddable));
	}

	@Override
	public void write(OutputStream out) throws DocumentGenerationException {
		for(PdfTableOfContent toc : tablesOfContent) {
			toc.addEmbeddables(embeddables);
		}
		
		layOut();
		
		try {
			pdf.write(out);
		} catch(PdfGenerationException e) {
			throw new DocumentGenerationException("Cannot generate document", e);
		}
	}

	@Override
	public void addPage() {
		//currentPage can never be null because first page of document is created using addPage(PageStyle style, PageHeader header, PageFooter footer). 
		nextPage(currentLayOutSection);
	}

	@Override
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	@Override
	public PdfPage getCurrentPage() {
		return currentPage;
	}

	@Override
	public Point getCursorPosition() {
		return cursorPosition;
	}

	/**
	 * Add a new section to document.
	 * 
	 * @param section The section to add.
	 * @return The section added.
	 */
	public Section nextSection(Section section) {
		//Create a new section from an existing one.
		currentWriteSection = new PdfSection(section);
		pdfSections.add(currentWriteSection);
		return currentWriteSection.getSection();
	}
	
	/**
	 * Add a new section (based on previous section style) to document.
	 * 
	 * @return The new section.
	 */
	public Section nextSection() {
		//If we already have a section, create one from existing one.
		if(currentWriteSection != null) {
			nextSection(currentWriteSection.getSection());
		} else {
			//Else create a default section.
			nextSection(new Section());
		}
		
		return currentWriteSection.getSection();
	}
	
	/**
	 * Get the section of document currently under write.
	 * 
	 * @return The current section.
	 */
	public PdfSection getCurrentSection() {
		return currentWriteSection;
	}
	
	/**
	 * Finish to lay-out the current page and start to lay-out a new one.
	 * 
	 * @param pdfSection The current section under lay-out.
	 */
	public void nextPage(PdfSection pdfSection) {
		if(currentPage != null) {
			currentPage.layOutEndOfPage();
		}
		
		currentLayOutSection = pdfSection;
		Section section = currentLayOutSection.getSection();
		
		PageFormat currentPageFormat = section.getStyle().getFormat();
		com.web4enterprise.pdf.core.page.Page corePage = pdf.createPage(currentPageFormat.getWidth(), currentPageFormat.getHeight());
		
		currentPageNumber++;
		currentPage = new PdfPage(this, corePage, section.getStyle(), (PdfPageHeader) section.getHeader(), (PdfPageFooter) section.getFooter());
		
		currentPage.getFootNotes().setWidth(currentPage.getInnerWidth());
		
		currentPage.layOutNewPage();
	}
	
	/**
	 * Lay-out the document document.
	 * This method iterate over each embeddable of document for rendering them.
	 * While the rendering of each embeddable is not verify, the rendering and verification is triggered again.
	 */
	public void layOut() {
		//We layout until all references are up-to-date.
		//Without TOC, it will be done in one run.
		//With TOC or any other dynamic element, this could last 2 or more runs.
		boolean verified = false;
		while(!verified) {
			//Clear all pages that could have been rendered on a previous run.
			pdf.clear();
			
			for(com.web4enterprise.pdf.core.image.Image image : images) {
				pdf.rebindImage(image);
			}
			
			//Tell each embeddable to prepare for next layouting.
			//They usually reset themselves.
			for(PdfSection pdfSection : pdfSections) {
				for(PdfSectionCommand command : pdfSection) {
					command.prepareNextLayOut(this);
				}
			}
			
			//Render all embeddables of document.
			for(PdfSection pdfSection : pdfSections) {
				nextPage(pdfSection);
				for(PdfSectionCommand command : pdfSection) {
					command.layOut(this);
				}
			}
			//Finish to lay-out document.
			finish();
		
			//Tell all embeddables of document to verify if they have been layed-out correctly.
			verified = true;
			for(PdfSection pdfSection : pdfSections) {
				for(PdfSectionCommand command : pdfSection) {
					verified = command.verifyLayOut(this);
					if(!verified) {
						break;
					}
				}
				if(!verified) {
					break;
				}
			}
		}
	}
	
	/**
	 * Finish to lay-out document by rendering last footer.
	 */
	protected void finish() {
		PdfPage pdfPage = getCurrentPage();
		if(pdfPage == null) {
			LOGGER.warning("Finishing a document without any page.");
		} else {
			pdfPage.layOutEndOfPage();
		}
	}
}
