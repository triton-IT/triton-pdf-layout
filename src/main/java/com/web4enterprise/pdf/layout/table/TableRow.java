package com.web4enterprise.pdf.layout.table;

import java.util.Arrays;
import java.util.List;

public class TableRow {	
	protected float height;
	
	public List<TableCell> cells;
	
	public TableRow(TableCell... cells) {		
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
