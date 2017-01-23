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
