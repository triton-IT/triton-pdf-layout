/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.web4enterprise.pdf.layout.table;

import com.web4enterprise.pdf.layout.document.DocumentEmbeddable;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;

/**
 * Defines a Table on a document.
 * 
 * 
 * @author Régis Ramillien
 */
public interface Table extends DocumentEmbeddable {
	/**
	 * Add a row to table.
	 * 
	 * @param cells The cells to add on the new row
	 * @return The row created.
	 */
	TableRow addRow(TableCell...cells);
	
	/**
	 * Set the width of the given column.
	 * 
	 * @param columnIndex The index of column to set width for.
	 * @param columnWidth The width for the given column.
	 */
	void setColumnWidth(int columnIndex, float columnWidth);

	/**
	 * Defines if table must repeat its header on new pages.
	 * 
	 * @param repeatHeader True if header must be repeated on a new page.
	 */
	void setRepeatHeader(boolean repeatHeader);

	/**
	 * Set the number of first rows that defines the table header. 
	 * 
	 * @param nbHeaderRows The number of rows in header.
	 */
	void setNbHeaderRows(int nbHeaderRows);
	
	/**
	 * Create an empty cell in the table.
	 * 
	 * @return The new cell.
	 */
	TableCell createTableCell();

	/**
	 * Create a cell filled-in with paragraphs with specified values.
	 * 
	 * @param values The values to add as paragraph to this cell.
	 * @return The new table cell.
	 */
	TableCell createTableCell(String... values);

	/**
	 * Create a cell filled-in with specified paragraphs.
	 * 
	 * @param paragraphs The paragraphs to add to this cell.
	 * @return The new table cell.
	 */
	TableCell createTableCell(Paragraph... paragraphs);

	/**
	 * Create a cell filled-in with paragraphs with specified values.
	 * 
	 * @param style The style to set to this paragraph.
	 * @param values The values to add as paragraph to this cell.
	 * @return The new table cell.
	 */
	TableCell createTableCell(TableCellStyle style, String... values);

	/**
	 * Create a cell filled-in with specified paragraphs.
	 * 
	 * @param style The style to set to this cell.
	 * @param paragraphs The paragraphs to set to this cell.
	 * @return The new table cell.
	 */
	TableCell createTableCell(TableCellStyle style, Paragraph... paragraphs);
}
