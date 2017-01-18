package com.web4enterprise.pdf.layout.table;

import java.util.List;

import com.web4enterprise.pdf.layout.paragraph.Paragraph;

public interface TableCell {	
	TableCellStyle getStyle();
	
	List<Paragraph> getParagraphs();

	int getMergedRows();

	TableCell setMergedRows(int mergedRows);

	int getMergedColumns();

	TableCell setMergedColumns(int mergedColumns);

	boolean isMerged();

	void setMerged(boolean merged);
}
