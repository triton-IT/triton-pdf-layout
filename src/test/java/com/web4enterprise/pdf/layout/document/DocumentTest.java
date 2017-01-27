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

import static com.web4enterprise.pdf.core.font.Font.TIMES_ROMAN;
import static com.web4enterprise.pdf.layout.text.Text.NEW_LINE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.junit.Test;

import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.exception.DocumentException;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.page.footer.PageFooter;
import com.web4enterprise.pdf.layout.page.header.PageHeader;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.placement.Margins;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.placement.StopType;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.TableCellStyle;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;
import com.web4enterprise.pdf.layout.toc.TableOfContent;

public class DocumentTest {
	@Test
	public void writeTest() throws IOException, DocumentException {
		//Defines output PDF file name.
		OutputStream out = new FileOutputStream("documentation.pdf");

		//Defines colors used in document.
		Color titleColor = new Color(204, 85, 89);
		Color codeColor = new Color(128, 80, 128);
		Color lightCodeColor = new Color(255, 250, 255);
		Color darkCodeColor = new Color(100, 0, 100);
		Color emphaseColor = new Color(128, 128, 60);
		Color lightEmphaseColor = new Color(250, 250, 240);
		Color darkEmphaseColor = new Color(100, 100, 0);
		
		//Defines paragraph styles used in document.
		ParagraphStyle codeStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.ITALIC, 10, codeColor);
		
		ParagraphStyle titleStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 24, titleColor);
		titleStyle.setAlignment(Alignment.CENTER);
		
		ParagraphStyle subTitleStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 18, emphaseColor);
		subTitleStyle.setAlignment(Alignment.CENTER);
		
		ParagraphStyle title1Style = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 14, titleColor);
		title1Style.setMargins(new Margins(20, 0, 10, 0));
		
		ParagraphStyle title2Style = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 12, titleColor);
		title2Style.setMargins(new Margins(10, 0, 5, 0));
		
		ParagraphStyle emphaseStyle = new ParagraphStyle();
		emphaseStyle.setFontColor(emphaseColor);
		
		ParagraphStyle footNoteStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.ITALIC, 10, codeColor);
		footNoteStyle.setFontColor(darkEmphaseColor);
		ParagraphStyle internalLinkStyle = new ParagraphStyle();
		internalLinkStyle.setUnderlined(true);
		
		ParagraphStyle simpleBoldStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD_ITALIC, 12);
		ParagraphStyle marginsStyle = new ParagraphStyle(TIMES_ROMAN, 14);
		marginsStyle.setMargins(new Margins(20));
		marginsStyle.setFirstLineMargin(20);
		
		ParagraphStyle marginsRightStyle = new ParagraphStyle();
		marginsRightStyle.setMargins(new Margins(20, 20, 0, 0));
		marginsRightStyle.setFirstLineMargin(50);
		marginsRightStyle.setAlignment(Alignment.RIGHT);
		
		ParagraphStyle marginsCenterStyle = new ParagraphStyle();
		marginsCenterStyle.setMargins(new Margins(50, 50, 0, 0));
		marginsCenterStyle.setFirstLineMargin(50);
		marginsCenterStyle.setAlignment(Alignment.CENTER);
		
		ParagraphStyle lineSpacingStyle = new ParagraphStyle();
		lineSpacingStyle.setLineSpacing(1.5f);

		ParagraphStyle tableHeaderStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.PLAIN, 12, titleColor);
		
		ParagraphStyle paragraphHeaderStyle = new ParagraphStyle();
		paragraphHeaderStyle.setMargins(new Margins(0.0f, 0.0f, 15.0f, 0.0f));
		
		ParagraphStyle paragraphFooterStyle = new ParagraphStyle();
		paragraphFooterStyle.setMargins(new Margins(15.0f, 0.0f, 0.0f, 0.0f));
		
		Document document = DocumentFactory.createPdfDocument();
		
		document.setAuthor("Regis Ramillien");
		document.setModificationDate(new Date());
		document.setProducer("web4enterprise");
		document.setSubject("documentation for simplyPDF-layout library");
		document.setTitle("simplyPDF-layout documentation");
		document.addKeyword("http://web4enterprise.com");
		document.addKeyword("simplyPDF-layout");
		document.addKeyword("Documentation");
		document.addMetaData("Customer-specific", "meta-data");
		
		Image logo = document.createImage(this.getClass().getResourceAsStream("/logo.png"));
		logo.setHeight(16, true);
		
		//Add first blank empty page to add content to it.
		Section section = document.nextPage();
		
		//Title page.
		section.addVerticalStop(550);
		section.addVerticalStop(500);
		section.addVerticalStop(400);
		
		document.nextVerticalStop();
		Image titleLogo = logo.clone();
		titleLogo.setWidth(150, true);
		Paragraph paragraph = document.createParagraph(titleStyle, titleLogo);
		document.addEmbeddable(paragraph);

		document.nextVerticalStop();
		paragraph = document.createParagraph(titleStyle, "SimplyPDF-layout documentation");
		document.addEmbeddable(paragraph);
		
		document.nextVerticalStop();
		paragraph = document.createParagraph(subTitleStyle, "Starting guide");
		document.addEmbeddable(paragraph);

		PageHeader pageHeader = document.createPageHeader();
		Paragraph headerParagraph = document.createParagraph(paragraphHeaderStyle, logo);
		headerParagraph.addEmbeddable(headerParagraph.createText("SimplyPDF-layout - web4enterprise"));
		pageHeader.addEmbeddables(headerParagraph);
		
		PageFooter pageFooter = document.createPageFooter();
		Paragraph footerParagraph = document.createParagraph(paragraphFooterStyle, new Date().toString());
		pageFooter.addEmbeddables(footerParagraph);
		
		//Page for table of content
		document.nextPage(new Section(PageStyle.A4_PORTRAIT, pageHeader, pageFooter));
		TableOfContent tableOfContent = document.createTableOfContent();
		tableOfContent.addLevel(0, title1Style);
		tableOfContent.addLevel(1, title2Style);
		document.addEmbeddable(tableOfContent);
		
		//Page for paragraphs
		document.nextPage(new Section(PageStyle.A4_PORTRAIT, pageHeader, pageFooter));

		//Creating a document.
		paragraph = document.createParagraph(title1Style, "Creating a document");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph("A PDF is created with:", NEW_LINE);
		paragraph.addEmbeddable(paragraph.createText(codeStyle, "Document document = Document.create();"));
		paragraph.addEmbeddable(paragraph.createText(NEW_LINE));
		paragraph.addEmbeddable(paragraph.createText("and rendered with;"));
		paragraph.addEmbeddable(paragraph.createText(NEW_LINE));
		paragraph.addEmbeddable(paragraph.createText(codeStyle, "document.write(out);"));
		document.addEmbeddable(paragraph);

		//Adding texts, paragraphs and pages.
		paragraph = document.createParagraph(title1Style, "Adding texts, paragraphs and pages");
		document.addEmbeddable(paragraph);

		paragraph = document.createParagraph("A document created with no page style uses the default one: A4 portrait.", 
			NEW_LINE, 
			"No need to add new pages, when an element does not fit into the page, a new page with same style than the previous one is created.", 
			NEW_LINE,
			"The same is applicable to lines. When a line does not fit into the page width, the text is automatically splitted on the last space "
			+ "that fits the page and the text that left is wrapped to a new line. This same principle is applicable indefinitively to all new lines created.", 
			NEW_LINE,
			"Of course, new lines and new pages can be created at any time.");
		document.addEmbeddable(paragraph);
		
		TextStyle superScripted = new TextStyle();
		superScripted.setScript(TextScript.SUPER);
		TextStyle subScripted = new TextStyle();
		subScripted.setScript(TextScript.SUB);
		paragraph = document.createParagraph("Text can be ");
		paragraph.addEmbeddable(paragraph.createText(superScripted, "super-scripted"));
		paragraph.addEmbeddable(paragraph.createText(" or "));
		paragraph.addEmbeddable(paragraph.createText(subScripted, "sub-scripted."));
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph("A paragraph is created with:", NEW_LINE);
		paragraph.addEmbeddable(paragraph.createText(codeStyle, "Paragraph paragraph = document.createParagraph(\"Text of paragraph\");"));
		paragraph.addEmbeddable(paragraph.createText(NEW_LINE, "A new page is created with;", NEW_LINE));
		paragraph.addEmbeddable(paragraph.createText(codeStyle, "document.addPage();"));
		paragraph.addEmbeddable(paragraph.createText(NEW_LINE, "Paragraph style (margins, first line margin, text color, size, font, etc.) can be defined using the constructor for most of parameters." + 
			"For others, getters and setters can be used.", NEW_LINE,
			"The same applies to most of the classes (Page, Text, etc.)."));
		document.addEmbeddable(paragraph);

		//Adding images and graphics
		paragraph = document.createParagraph(title1Style, "Adding images and graphics");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph("A paragraph can contain text but also images like this one:");
		paragraph.addEmbeddable(paragraph.createImage(logo));
		paragraph.addEmbeddable(paragraph.createText(" and this one:"));
		paragraph.addEmbeddable(paragraph.createImage(logo));
		document.addEmbeddable(paragraph);

		//Text styles.
		paragraph = document.createParagraph(title1Style, "Text styles");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph(emphaseStyle, "A color can be defined for an entire paragraph but also for ");
		paragraph.addEmbeddable(paragraph.createText(codeStyle, "a subset "));
		paragraph.addEmbeddable(paragraph.createText("of the same paragraph."));
		document.addEmbeddable(paragraph);

		TextStyle plainUnderlined = new TextStyle(FontsVariant.PLAIN);
		plainUnderlined.setFontColor(emphaseColor);
		plainUnderlined.setUnderlined(true);
		
		TextStyle italicUnderlined = new TextStyle(FontsVariant.ITALIC);
		italicUnderlined.setUnderlined(true);
		
		paragraph = document.createParagraph(simpleBoldStyle, "This paragraph demonstrate that text can simply be put in ");
		paragraph.addEmbeddable(paragraph.createText(new TextStyle(FontsVariant.BOLD), "bold,"));
		paragraph.addEmbeddable(paragraph.createText(italicUnderlined, " italic"));
		paragraph.addEmbeddable(paragraph.createText(plainUnderlined, " and underlined."));
		paragraph.addEmbeddable(paragraph.createText(new TextStyle(TIMES_ROMAN, 14), " Text size can also be changed within a paragraph."));
		paragraph.addEmbeddable(paragraph.createText(NEW_LINE, "Text styles needs to be used inside a \"Text\" object. A paragraph allow a simple String or Text object as parameter."));
		document.addEmbeddable(paragraph);

		//Paragraph styles.
		paragraph = document.createParagraph(title1Style, "Paragraph styles");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph(marginsStyle, "This paragraph has a margin of 20 on each side plus a first line margin of 20."
				, NEW_LINE, "It's also composed of few lines that are part of this same paragraph, so this paragraph configuration is applied to all lines of it."
				, NEW_LINE, "So, the first line is shifted by 40 pt from the left while other lines of the paragraph are shifted by 20."
				, NEW_LINE, "This paragraph also ends from 20 pt before page margins and have an top and bottom margin of 20 too."
				, NEW_LINE, "Text of this paragraph has a size of 14 instead of 12 in a default one.");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph(marginsRightStyle, "This paragraph is aligned to right. This shows that the first line margin is taken into account."
				+ " So, the text will not be displayed until start of paragraph but with a space of the size of the first line margin."
				+ " The others line will start with the default paragraph margin."
				, NEW_LINE, "This even works with new lines inside a paragraph. New lines will not be sensitive to first line of paragraph margin.");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph(marginsCenterStyle, "This paragraph is aligned to center with a first line margin."
				+ " It demonstrate the same principles than the previous paragraph.");
		document.addEmbeddable(paragraph);

		paragraph = document.createParagraph(lineSpacingStyle, "This paragraph shows that a vertical line spacing ratio can be applied between each line of a paragraph."
				+ " The standard line spacing is just the size of the font. The line spacing ratio is the multication of itself by the font size."
				+ " So if line spacing is set to 2, an empty space of the size of the font is left blank between two lines."
				+ " Both the lines wrapped automatically and"
				, NEW_LINE, "The ones created specifically"
				, NEW_LINE, "will be affected by vertical line spacing ratio.");
		document.addEmbeddable(paragraph);

		//Page styles.
		document.nextPage(new Section(PageStyle.A6_LANDSCAPE, pageHeader, pageFooter));
		
		paragraph = document.createParagraph(title1Style, "Page styles");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph("This page show a page with a different orientation and size.", NEW_LINE, "You can use default ones or create the ones you need.");
		document.addEmbeddable(paragraph);

		//Create header for next page.
		pageHeader = document.createPageHeader();
		headerParagraph = document.createParagraph(paragraphHeaderStyle, logo);
		headerParagraph.addEmbeddable(headerParagraph.createText("SimplyPDF-layout - web4enterprise - new page header example"));
		pageHeader.addEmbeddables(headerParagraph);
		
		//Set footer before rendering any other element.
		pageFooter = document.createPageFooter();
		footerParagraph = document.createParagraph(paragraphFooterStyle, new Date().toString() + " - new page footer example");
		pageFooter.addEmbeddables(footerParagraph);
		
		//Add a page with new header.
		document.nextPage(new Section(PageStyle.A4_PORTRAIT, pageHeader, pageFooter));
		
		//Headers and footer title.
		paragraph = document.createParagraph(title1Style, "Adding headers and footers");
		document.addEmbeddable(paragraph);
		
		//Headers and footers explanations.
		paragraph = document.createParagraph("Headers and footers can be set once for all for every page or when adding a new page.");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph(codeStyle, "PageHeader pageHeader = new PageHeader();", 
				NEW_LINE, 
				"pageHeader.addElement(document.createParagraph(\"SimplyPDF-layout - web4enterprise\"));", 
				NEW_LINE,
				"document.addPage(pageHeader, pageFooter);");
		document.addEmbeddable(paragraph);

		//Tables.
		paragraph = document.createParagraph(title1Style, "Adding tables");
		document.addEmbeddable(paragraph);
		
		TableCellStyle tableHeaderCellStyle = new TableCellStyle();
		tableHeaderCellStyle.setBordersStyle(new BorderStyle(4.0f, darkEmphaseColor), BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID);
		tableHeaderCellStyle.setBackgroundColor(lightEmphaseColor);
		
		TableCellStyle tableFooterCellStyle = new TableCellStyle(lightCodeColor);
		tableFooterCellStyle.setBordersStyle(BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, new BorderStyle(2.0f, darkCodeColor), BorderStyle.THIN_SOLID);
		
		Table table = document.createTable();
		table.addRow(table.createTableCell(tableHeaderCellStyle, document.createParagraph(tableHeaderStyle, "How to ...")), table.createTableCell(tableHeaderCellStyle, document.createParagraph(tableHeaderStyle, "Code")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Create a table")), table.createTableCell(document.createParagraph(codeStyle, "Table table = new Table()")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Add a row")), table.createTableCell(document.createParagraph(codeStyle, "table.addRow(TableCell...)")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Add a cell to a row")), table.createTableCell(document.createParagraph(codeStyle, "table.createTableCell(\"text of cell\")")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Control column width")), table.createTableCell(document.createParagraph(codeStyle, "table.setColumnWidth(columnIndex, width);")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Add table to page")), table.createTableCell(document.createParagraph(codeStyle, "document.addElement(table)")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Merge rows")).setMergedRows(1), table.createTableCell(document.createParagraph(codeStyle, "cell.setMergedRows(nbRows)")));
		table.addRow(table.createTableCell(), table.createTableCell());
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Merge columns"), document.createParagraph(codeStyle, "cell.setMergedColumns(nbColumns)")).setMergedColumns(1));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Repeat header on a new page")), table.createTableCell(document.createParagraph(codeStyle, "setRepeatHeaderOnNewPage(true)")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Create a cell style")), table.createTableCell(document.createParagraph(codeStyle, "TableCellStyle cellStyle = table.createTableCellStyle()")));
		table.addRow(table.createTableCell(document.createParagraph(emphaseStyle, "Affect style to cell")), table.createTableCell(document.createParagraph(codeStyle, "table.createTableCell(cellStyle, paragraph)")));
		table.addRow(table.createTableCell(tableFooterCellStyle, document.createParagraph("Add footer")), table.createTableCell(tableFooterCellStyle, document.createParagraph(codeStyle, "Just create a new row with a different style.")));
		
		table.setColumnWidth(0, 237.5f);
		table.setColumnWidth(1, 237.5f);
		document.addEmbeddable(table);
		
		paragraph = document.createParagraph(NEW_LINE, "More information on tables in table.pdf");
		document.addEmbeddable(paragraph);

		//Table of content.
		paragraph = document.createParagraph(title1Style, "Adding table of content");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph(emphaseStyle, "This still have to be coded.");
		document.addEmbeddable(paragraph);

		//Internal links.
		paragraph = document.createParagraph(title1Style, "Adding document internal links");
		document.addEmbeddable(paragraph);

		paragraph = document.createParagraph(NEW_LINE);
		Text linkedText = paragraph.createText(internalLinkStyle, "Document internal links");
		paragraph.addEmbeddable(linkedText);
		paragraph.addEmbeddable(paragraph.createText(" can simply be added to any text."));
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph("An internal link target is nothing special, it's just a simple element like this one but is generally a title or a figure.");
		document.addEmbeddable(paragraph);
		
		linkedText.setLink(paragraph);

		//Footnotes.
		paragraph = document.createParagraph(title1Style, "Adding footnotes");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph();
		Text footNotedText = paragraph.createText("A footnote can be added simply by adding a footnote object to a paragraph element.");
		paragraph.addEmbeddable(footNotedText);
		Paragraph footNote = document.createParagraph(footNoteStyle, "Any paragraph with texts and images can be added to a footnote.", NEW_LINE, 
				"Footnotes indices are added to text automatically as super-scripts.");
		Paragraph footNote2 = document.createParagraph(footNoteStyle, "Many foot-notes can be placed on the same text.");
		footNotedText.addFootNote(footNote);
		footNotedText.addFootNote(footNote2);
		document.addEmbeddable(paragraph);
		
		//Stops.
		paragraph = document.createParagraph(title1Style, "Using stops");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph("A stop can be added anywhere in a paragraph to:");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph();
		paragraph.addStop(new Stop(StopType.RIGHT, 80.0f));
		paragraph.nextStop("- place text right to it.");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph();
		paragraph.addStop(new Stop(StopType.LEFT, 177.14f));
		paragraph.nextStop("- place text left to it.");
		document.addEmbeddable(paragraph);
		
		paragraph = document.createParagraph();
		paragraph.addStop(new Stop(StopType.CENTER, 130.0f));
		paragraph.nextStop("- place text centered on it.");
		document.addEmbeddable(paragraph);
		
		document.write(out);
	}
}
