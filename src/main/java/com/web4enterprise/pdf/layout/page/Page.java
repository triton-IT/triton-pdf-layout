package com.web4enterprise.pdf.layout.page;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.layout.document.impl.Layouter;

public class Page {
	protected com.web4enterprise.pdf.core.page.Page corePage;
	
	protected Layouter layouter;
	
	protected PageStyle style = PageStyle.A4_PORTRAIT;
	protected PageHeader header = null;
	protected PageFooter footer = null;
	protected PageFootNotes footNotes = new PageFootNotes();
	
	protected float pageWidth;
	
	protected int currentFootNoteId = 1;
	
	public Page(Layouter layouter, com.web4enterprise.pdf.core.page.Page corePage, PageStyle pageStyle) {
		this(layouter, corePage, pageStyle, null, null);
	}
	
	public Page(Layouter layouter, com.web4enterprise.pdf.core.page.Page corePage, PageStyle pageStyle, PageHeader pageHeader, PageFooter pageFooter) {
		this.corePage = corePage;
		
		this.layouter = layouter;
		
		this.style = pageStyle;
		this.header = pageHeader;
		this.footer = pageFooter;
		
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
	
	public PageHeader getHeader() {
		return header;
	}
	
	public PageFooter getFooter() {
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
		layouter.getCursorPosition().setY(yPosition);
		
		if(header != null) {
			header.layout(layouter, 
					new Rect(style.getMargins().getTop(), 
							style.getMargins().getLeft(),
							style.getMargins().getBottom(),
							style.getFormat().getWidth() - style.getMargins().getRight()),
						layouter.getCursorPosition().getY(),
						footNotes);
			layouter.getCursorPosition().setY(yPosition - header.getHeight(layouter, pageWidth));
		}
	}
	
	public void layoutEndOfPage() {
		layoutFootNotes();
		layoutFooter();
	}
	
	protected void layoutFootNotes() {
		///If we have a footer to layout.
		 if(!footNotes.isEmpty()) {
			float footerHeight = footer.getHeight(layouter, pageWidth);
			float footNotesHeight = footNotes.getHeight(layouter, pageWidth);

			footNotes.layout(layouter, 
					new Rect(style.getMargins().getBottom() + footerHeight + footNotesHeight, 
							style.getMargins().getLeft(),
							style.getMargins().getBottom() + footerHeight,
							style.getFormat().getWidth() - style.getMargins().getRight()),
					style.getMargins().getBottom() + footerHeight + footNotesHeight,
					footNotes);
		}
	}
	
	protected void layoutFooter() {
		///If we have a footer to layout.
		 if(footer != null) {
			float footerHeight = footer.getHeight(layouter, pageWidth);

			//We do not allow footNotes on footers. This will change page layouting and its too late for this.
			footer.layout(layouter, 
					new Rect(style.getMargins().getBottom() + footerHeight, 
							style.getMargins().getLeft(),
							style.getMargins().getBottom(),
							style.getFormat().getWidth() - style.getMargins().getRight()),
					style.getMargins().getBottom() + footerHeight,
					null);
		}
	}
}
