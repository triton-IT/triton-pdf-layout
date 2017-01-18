package com.web4enterprise.pdf.layout.page.impl;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.footer.impl.PdfPageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.header.impl.PdfPageHeader;

public class Page {
	protected com.web4enterprise.pdf.core.page.Page corePage;
	
	protected PdfPager pdfPager;
	
	protected PageStyle style = PageStyle.A4_PORTRAIT;
	protected PdfPageHeader header = null;
	protected PdfPageFooter footer = null;
	protected PageFootNotes footNotes = new PageFootNotes();
	
	protected float pageWidth;
	
	protected int currentFootNoteId = 1;
	
	public Page(PdfPager pdfPager, com.web4enterprise.pdf.core.page.Page corePage, PageStyle pageStyle) {
		this(pdfPager, corePage, pageStyle, null, null);
	}
	
	public Page(PdfPager pdfPager, com.web4enterprise.pdf.core.page.Page corePage, PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {
		this.corePage = corePage;
		
		this.pdfPager = pdfPager;
		
		this.style = pageStyle;
		this.header = (PdfPageHeader) pageHeader;
		this.footer = (PdfPageFooter) pageFooter;
		
		pageWidth = pageStyle.getFormat().getWidth() - 
				pageStyle.getMargins().getLeft() - 
				pageStyle.getMargins().getRight();
	}
	
	public com.web4enterprise.pdf.core.page.Page getCorePage() {
		return corePage;
	}
	
	public PageStyle getStyle() {
		return style;
	}
	
	public PdfPageHeader getHeader() {
		return header;
	}
	
	public PdfPageFooter getFooter() {
		return footer;
	}
	
	public PageFootNotes getFootNotes() {
		return footNotes;
	}
	
	public String generateFootNoteId() {
		return String.valueOf(currentFootNoteId++);
	}
	
	public void layOutNewPage() {
		float yPosition = style.getFormat().getHeight() - style.getMargins().getTop();
		pdfPager.getCursorPosition().setY(yPosition);
		
		if(header != null) {
			header.layOut(pdfPager, 
					new Rect(style.getMargins().getTop(), 
							style.getMargins().getLeft(),
							style.getMargins().getBottom(),
							style.getFormat().getWidth() - style.getMargins().getRight()),
						footNotes);
			pdfPager.getCursorPosition().setY(yPosition - header.getHeight(pdfPager, pageWidth));
		}
	}
	
	public void layOutEndOfPage() {
		layOutFootNotes();
		layOutFooter();
	}
	
	public float getInnerTop() {
		float top = style.getInnerTop();
		
		PdfPageFooter pageFooter = getFooter();
		if(pageFooter != null) {
			top -= pageFooter.getHeight(pdfPager, style.getInnerWidth());
		}
		
		return top;
	}
	
	/**
	 * Get inner bottom of page. (i.e. bottom of page + footer. Foot notes are not added to bottom calculation). 
	 * 
	 * @return
	 */
	public float getInnerBottom() {
		float bottom = style.getInnerBottom();

		PdfPageFooter pageFooter = getFooter();
		if(pageFooter != null) {
			bottom += pageFooter.getHeight(pdfPager, style.getInnerWidth());
		}
		
		return bottom;
	}
	
	public float getInnerLeft() {
		return style.getInnerLeft();
	}
	
	public float getInnerRight() {
		return style.getInnerRight();
	}
	
	public float getInnerWidth() {
		return style.getInnerRight() - style.getInnerLeft();
	}
	
	/**
	 * Get inner rectangle of page. (i.e. use margins and header/footer in calculation. Foot notes are not used in calculation).
	 * 
	 * @return
	 */
	public Rect getInnerRect() {		
		return new Rect(getInnerTop(), 
				getInnerLeft(),
				getInnerBottom(),
				getInnerRight());
	}
	
	protected void layOutFootNotes() {
		///If we have a footer to lay out.
		 if(!footNotes.isEmpty()) {
			float footerHeight = footer.getHeight(pdfPager, pageWidth);
			float footNotesHeight = footNotes.getHeight(pdfPager, pageWidth);

			footNotes.layOut(pdfPager, 
					new Rect(style.getInnerBottom() + footerHeight + footNotesHeight, 
							style.getInnerLeft(),
							style.getInnerBottom() + footerHeight,
							style.getInnerRight()),
					footNotes);
		}
	}
	
	protected void layOutFooter() {
		///If we have a footer to lay out.
		 if(footer != null) {
			float footerHeight = footer.getHeight(pdfPager, pageWidth);

			pdfPager.getCursorPosition().setY(style.getInnerBottom() + footerHeight);
			//We do not allow footNotes on footers. This will change page layouting and its too late for this.
			footer.layOut(pdfPager, 
					new Rect(style.getInnerBottom() + footerHeight, 
							style.getInnerLeft(),
							style.getInnerBottom(),
							style.getInnerRight()),
					null);
		}
	}
}
