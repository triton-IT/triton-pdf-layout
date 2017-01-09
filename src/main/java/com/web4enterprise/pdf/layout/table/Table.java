package com.web4enterprise.pdf.layout.table;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;

public interface Table extends DocumentEmbeddable {	
	TableRow addRow(TableCell...cells);
	
	void setColumnWidth(int columnIndex, float columnWidth);

	void setRepeatHeader(boolean repeatHeader);

	void setNbHeaderRows(int nbHeaderRows);
}
