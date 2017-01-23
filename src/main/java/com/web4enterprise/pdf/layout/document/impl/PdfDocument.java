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

import com.web4enterprise.pdf.core.document.Pdf;
import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
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

/**
 * PDF implementation of Document API.
 * 
 * 
 * @author Régis Ramillien
 */
public class PdfDocument implements Document {	
	/**
	 * Document from low-level API.
	 */
	protected Pdf pdf = new Pdf();
	/**
	 * Used for creating page and controlling lay-out.
	 */
	protected PdfPager pdfPager = new PdfPager(pdf);
	/**
	 * The list of embeddables for document.
	 */
	protected List<DocumentEmbeddable> embeddables = new ArrayList<>();
	/**
	 * The list of table of content of document.
	 */
	protected List<PdfTableOfContent> tablesOfContent = new ArrayList<>();
	
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
		return pdfPager.nextSection();
	}

	@Override
	public Section nextPage(Section section) {
		return pdfPager.nextSection(section);
	}
	
	@Override
	public void nextVerticalStop() {
		pdfPager.getCurrentSection().add(new PdfNextVerticalStopCommand());
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
		PdfTableOfContent toc = new PdfTableOfContent();
		tablesOfContent.add(toc);
		return toc;
	}

	@Override
	public void addEmbeddable(DocumentEmbeddable embeddable) {
		if(pdfPager.getCurrentSection() == null) {
			throw new BadOperationException("You can't add an element without having created a page first.");
		}
		if(!(embeddable instanceof PdfDocumentEmbeddable)) {
			throw new BadOperationException("You must add an embeddable useable by this API.");
		}
		
		embeddables.add(embeddable);
		pdfPager.getCurrentSection().add(new PdfAddEmbeddableCommand((PdfDocumentEmbeddable) embeddable));
	}

	@Override
	public void write(OutputStream out) throws DocumentGenerationException {
		for(PdfTableOfContent toc : tablesOfContent) {
			toc.addEmbeddables(embeddables);
		}
		
		pdfPager.layOut();
		
		try {
			pdf.write(out);
		} catch(PdfGenerationException e) {
			throw new DocumentGenerationException("Cannot generate document", e);
		}
	}
}
