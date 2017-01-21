package com.web4enterprise.pdf.layout.document.impl.command;

import com.web4enterprise.pdf.layout.document.impl.PdfPager;
import com.web4enterprise.pdf.layout.document.impl.PdfSectionCommand;
import com.web4enterprise.pdf.layout.exception.BadOperationException;
import com.web4enterprise.pdf.layout.page.impl.VerticalStopsList;

public class PdfNextVerticalStopCommand implements PdfSectionCommand {
	protected VerticalStopsList verticalStops = new VerticalStopsList();

	@Override
	public void prepareNextLayOut(PdfPager pdfPager) {
		verticalStops.reset();
	}
	
	@Override
	public boolean verifyLayOut(PdfPager pdfPager) {
		return true;
	}

	@Override
	public void layOut(PdfPager pdfPager) {
		int currentStopIndex = verticalStops.getCurrentIndex();
		
		if(verticalStops.size() > currentStopIndex) {
			float stopPosition = verticalStops.get(currentStopIndex);
			if(stopPosition < pdfPager.getCursorPosition().getY()) {
				pdfPager.getCursorPosition().setY(stopPosition);
			}
		} else {
			throw new BadOperationException("There is no vertical stop available.");
		}
		
		verticalStops.next();
	}
	
	public void setStops(VerticalStopsList verticalStops) {
		this.verticalStops = verticalStops;
	}
}
