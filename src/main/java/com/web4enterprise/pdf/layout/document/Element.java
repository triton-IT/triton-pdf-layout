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
package com.web4enterprise.pdf.layout.document;

import com.web4enterprise.pdf.core.geometry.Rect;
import com.web4enterprise.pdf.core.link.Linkable;
import com.web4enterprise.pdf.layout.document.impl.Layouter;
import com.web4enterprise.pdf.layout.page.PageFootNotes;

public interface Element extends Cloneable, Linkable {
	float getHeight(Layouter documentLayouter, float width);
	void layout(Layouter documentLayouter, Rect boundingBox, float startY, PageFootNotes pageFootNotes);
	
	Element clone();
}
