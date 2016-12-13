package com.web4enterprise.pdf.layout.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableRow extends ArrayList<TableCell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected float height;
	
	public List<TableCell> cells;
	
	public TableRow(TableCell... cells) {
		super(cells.length);
		
		this.cells = Arrays.asList(cells);
	}

	public List<TableCell> getCells() {
		return cells;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
}
