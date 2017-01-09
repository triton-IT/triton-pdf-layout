package com.web4enterprise.pdf.layout.page;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.Pager;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.footer.impl.PdfPageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.page.header.impl.PdfPageHeader;

public class Page {
	protected com.web4enterprise.pdf.core.page.Page corePage;
	
	protected Pager pager;
	
	protected PageStyle style = PageStyle.A4_PORTRAIT;
	protected PdfPageHeader header = null;
	protected PdfPageFooter footer = null;
	protected PageFootNotes footNotes = new PageFootNotes();
	
	protected float pageWidth;
	
	protected int currentFootNoteId = 1;
	
	public Page(Pager pager, com.web4enterprise.pdf.core.page.Page corePage, PageStyle pageStyle) {
		this(pager, corePage, pageStyle, null, null);
	}
	
	public Page(Pager pager, com.web4enterprise.pdf.core.page.Page corePage, PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {
		this.corePage = corePage;
		
		this.pager = pager;
		
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
	
	public void layoutNewPage() {
		float yPosition = style.getFormat().getHeight() - style.getMargins().getTop();
		pager.getCursorPosition().setY(yPosition);
		
		if(header != null) {
			header.layout(pager, 
					new Rect(style.getMargins().getTop(), 
							style.getMargins().getLeft(),
							style.getMargins().getBottom(),
							style.getFormat().getWidth() - style.getMargins().getRight()),
						pager.getCursorPosition().getY(),
						footNotes);
			pager.getCursorPosition().setY(yPosition - header.getHeight(pager, pageWidth));
		}
	}
	
	public void layoutEndOfPage() {
		layoutFootNotes();
		layoutFooter();
	}
	
	public float getInnerTop() {
		float top = style.getInnerTop();
		
		PdfPageFooter pageFooter = getFooter();
		if(pageFooter != null) {
			top -= pageFooter.getHeight(pager, style.getInnerWidth());
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
			bottom += pageFooter.getHeight(pager, style.getInnerWidth());
		}
		
		return bottom;
	}
	
	public float getInnerLeft() {
		return style.getInnerLeft();
	}
	
	public float getInnerRight() {
		return style.getInnerRight();
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
	
	protected void layoutFootNotes() {
		///If we have a footer to layout.
		 if(!footNotes.isEmpty()) {
			float footerHeight = footer.getHeight(pager, pageWidth);
			float footNotesHeight = footNotes.getHeight(pager, pageWidth);

			footNotes.layout(pager, 
					new Rect(style.getInnerBottom() + footerHeight + footNotesHeight, 
							style.getInnerLeft(),
							style.getInnerBottom() + footerHeight,
							style.getInnerRight()),
					style.getInnerBottom() + footerHeight + footNotesHeight,
					footNotes);
		}
	}
	
	protected void layoutFooter() {
		///If we have a footer to layout.
		 if(footer != null) {
			float footerHeight = footer.getHeight(pager, pageWidth);

			//We do not allow footNotes on footers. This will change page layouting and its too late for this.
			footer.layout(pager, 
					new Rect(style.getInnerBottom() + footerHeight, 
							style.getInnerLeft(),
							style.getInnerBottom(),
							style.getInnerRight()),
					style.getInnerBottom() + footerHeight,
					null);
		}
	}
}
