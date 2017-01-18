package com.web4enterprise.pdf.layout.table;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;

public interface Table extends DocumentEmbeddable {	
	TableRow addRow(TableCell...cells);
	
	void setColumnWidth(int columnIndex, float columnWidth);

	void setRepeatHeader(boolean repeatHeader);

	void setNbHeaderRows(int nbHeaderRows);
	
	TableCell createTableCell();
	
	TableCell createTableCell(String... values);
	
	TableCell createTableCell(Paragraph... paragraphs);
	
	TableCell createTableCell(TableCellStyle style, String... values);
	
	TableCell createTableCell(TableCellStyle style, Paragraph... paragraphs);
}
