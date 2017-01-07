package com.web4enterprise.pdf.layout.page;

import java.util.ArrayList;

public class VerticalStopsList extends ArrayList<Float> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int currentIndex = 0;
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void next() {
		currentIndex++;
	}
}
