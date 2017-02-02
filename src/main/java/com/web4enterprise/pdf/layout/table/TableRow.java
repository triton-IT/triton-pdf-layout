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

import java.util.Arrays;
import java.util.List;

/**
 * Defines a table row on a table.
 * 
 * 
 * @author Régis Ramillien
 */
public class TableRow {
	/**
	 * The height of row.
	 */
	protected float height;
	/**
	 * The list of cells of this row.
	 */
	public List<TableCell> cells;
	
	/**
	 * Create a row with the specified cells.
	 * 
	 * @param cells The cells to add to this row.
	 */
	public TableRow(TableCell... cells) {		
		this.cells = Arrays.asList(cells);
	}

	/**
	 * Get the list of cells of this row.
	 * 
	 * @return The list of cells.
	 */
	public List<TableCell> getCells() {
		return cells;
	}
	
	/**
	 * Get the height of this row.
	 * 
	 * @return The height of this row.
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Set the height of this row.
	 * 
	 * @param height The height of this row.
	 */
	public void setHeight(float height) {
		this.height = height;
	}
}
