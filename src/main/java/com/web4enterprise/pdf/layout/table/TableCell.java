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

import java.util.List;

import com.web4enterprise.pdf.layout.paragraph.Paragraph;

/**
 * Defines a table cell on a table.
 * 
 * 
 * @author Régis Ramillien
 */
public interface TableCell {
	/**
	 * Get style to cell.
	 * 
	 * @return The style of this cell.
	 */
	TableCellStyle getStyle();
	
	/**
	 * Get the paragraphs conttained in this cell.
	 * 
	 * @return The paragraphs of this cell.
	 */
	List<Paragraph> getParagraphs();

	/**
	 * Get the number of rows merged after this cell.
	 * 
	 * @return The number of merged rows.
	 */
	int getMergedRows();

	/**
	 * Set the number of rows to merge after this cell.
	 * 
	 * @param mergedRows The number of rows merged after this cell.
	 * @return This table cell.
	 */
	TableCell setMergedRows(int mergedRows);

	/**
	 * Get the number of columns merged belo this cell.
	 * 
	 * @return The number of columns merged.
	 */
	int getMergedColumns();

	/**
	 * Set the number of columns to merge below this cell.
	 * 
	 * @param mergedColumns The number of columns to merge.
	 * @return This cell.
	 */
	TableCell setMergedColumns(int mergedColumns);

	/**
	 * Check if this cell is merged.
	 * 
	 * @return True if this cell is merged, false otherwise.
	 */
	boolean isMerged();

	/**
	 * Set the merge state of this cell.
	 * 
	 * @param merged True if this cell is merged.
	 */
	void setMerged(boolean merged);
}
