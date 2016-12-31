package com.web4enterprise.pdf.layout.document;

import static com.web4enterprise.pdf.core.font.Font.TIMES_ROMAN;
import static com.web4enterprise.pdf.layout.text.Text.NEW_LINE;
import static com.web4enterprise.pdf.layout.text.Text.NEW_TEXT_LINE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.junit.Test;

import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.image.Image;
import com.web4enterprise.pdf.layout.page.PageFooter;
import com.web4enterprise.pdf.layout.page.PageHeader;
import com.web4enterprise.pdf.layout.page.PageStyle;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.Paragraph;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;
import com.web4enterprise.pdf.layout.placement.Alignment;
import com.web4enterprise.pdf.layout.placement.BorderStyle;
import com.web4enterprise.pdf.layout.placement.Margins;
import com.web4enterprise.pdf.layout.placement.Stop;
import com.web4enterprise.pdf.layout.placement.StopType;
import com.web4enterprise.pdf.layout.table.Table;
import com.web4enterprise.pdf.layout.table.TableCell;
import com.web4enterprise.pdf.layout.table.TableCellStyle;
import com.web4enterprise.pdf.layout.text.Text;
import com.web4enterprise.pdf.layout.text.TextStyle;

public class DocumentTest {
	@Test
	public void writeTest() throws IOException, PdfGenerationException {
		OutputStream out = new FileOutputStream("documentation.pdf");
		
		Color codeColor = new Color(128, 80, 128);
		Color lightCodeColor = new Color(255, 250, 255);
		Color darkCodeColor = new Color(100, 0, 100);
		ParagraphStyle codeStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.ITALIC, 10, codeColor);
		Color titleColor = new Color(204, 85, 89);
		ParagraphStyle titleStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 14, titleColor);
		titleStyle.setMargins(new Margins(20, 0, 10, 0));
		ParagraphStyle title2Style = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 12, titleColor);
		title2Style.setMargins(new Margins(10, 0, 5, 0));
		Color emphaseColor = new Color(128, 128, 60);
		Color lightEmphaseColor = new Color(250, 250, 240);
		Color darkEmphaseColor = new Color(100, 100, 0);
		ParagraphStyle emphaseStyle = new ParagraphStyle();
		emphaseStyle.setFontColor(emphaseColor);
		ParagraphStyle footNoteStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.ITALIC, 10, codeColor);
		footNoteStyle.setFontColor(darkEmphaseColor);
		ParagraphStyle internalLinkStyle = new ParagraphStyle();
		internalLinkStyle.setUnderlined(true);
		
		Document document = new Document();
		
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
		
		PageHeader pageHeader = new PageHeader();
		ParagraphStyle paragraphHeaderStyle = new ParagraphStyle();
		paragraphHeaderStyle.setMargins(new Margins(0.0f, 0.0f, 15.0f, 0.0f));
		Paragraph paragraphHeader = new Paragraph(paragraphHeaderStyle, logo, new Text("SimplyPDF-layout - web4enterprise"));
		pageHeader.addElement(paragraphHeader);
		PageFooter pageFooter = new PageFooter();
		ParagraphStyle paragraphFooterStyle = new ParagraphStyle();
		paragraphFooterStyle.setMargins(new Margins(15.0f, 0.0f, 0.0f, 0.0f));
		pageFooter.addElement(new Paragraph(paragraphFooterStyle, new Date().toString()));
		
		document.addPage(pageHeader, pageFooter);

		//Creating a document.
		document.addElement(new Paragraph(titleStyle, "Creating a document"));
		
		Paragraph paragraph = new Paragraph("A PDF is created with:", NEW_LINE);
		paragraph.addElement(new Text(codeStyle, "Document document = new Document();"), NEW_TEXT_LINE,
				new Text("and rendered with;"), NEW_TEXT_LINE,
				new Text(codeStyle, "document.write(out);"));
		document.addElement(paragraph);

		//Adding texts, paragraphs and pages.
		document.addElement(new Paragraph(titleStyle, "Adding texts, paragraphs and pages"));

		paragraph = new Paragraph("A document created with no page style uses the default one: A4 portrait.", NEW_LINE);
		paragraph.addElement(new Text("No need to add new pages, when an element does not fit into the page, a new page with same style than the previous one is created."), 
				NEW_TEXT_LINE);
		paragraph.addElement(new Text("The same is applicable to lines. When a line does not fit into the page width, the text is automatically splitted on the last space "
				+ "that fits the page and the text that left is wrapped to a new line. This same principle is applicable indefinitively to all new lines created."), 
				NEW_TEXT_LINE);
		paragraph.addElement(new Text("Of course, new lines and new pages can be created at any time."), 
				NEW_TEXT_LINE);
		document.addElement(paragraph);
		
		TextStyle superScripted = new TextStyle();
		superScripted.setScript(TextScript.SUPER);
		TextStyle subScripted = new TextStyle();
		subScripted.setScript(TextScript.SUB);
		paragraph = new Paragraph(new Text("Text can be "), new Text(superScripted, "super-scripted"), new Text(" or "), new Text(subScripted, "sub-scripted."), NEW_TEXT_LINE);
		document.addElement(paragraph);
		
		paragraph = new Paragraph("A paragraph is created with:", NEW_LINE);
		paragraph.addElement(new Text(codeStyle, "Paragraph paragraph = new Paragraph(\"Text of paragraph\");"), NEW_TEXT_LINE,
				new Text("A new page is created with;"), NEW_TEXT_LINE,
				new Text(codeStyle, "document.addPage();"), NEW_TEXT_LINE,
				new Text("Paragraph style (margins, first line margin, text color, size, font, etc.) can be defined using the constructor for most of parameters. For others, "
						+ "getters and setters can be used."), NEW_TEXT_LINE,
				new Text("The same applies to most of the classes (Page, Text, etc.)."));
		document.addElement(paragraph);

		//Adding images and graphics
		document.addElement(new Paragraph(titleStyle, "Adding images and graphics"));
		
		paragraph = new Paragraph(new Text("A paragraph can contain text but also images like this one:"),  logo.clone(), new Text(" and this one:"),  logo.clone());
		document.addElement(paragraph);

		//Text styles.
		document.addElement(new Paragraph(titleStyle, "Text styles"));
		
		paragraph = new Paragraph(emphaseStyle, new Text("A color can be defined for an entire paragraph but also for ")
				, new Text(codeStyle, "a subset ")
				, new Text("of the same paragraph."));
		document.addElement(paragraph);

		TextStyle plainUnderlined = new TextStyle(FontsVariant.PLAIN);
		plainUnderlined.setFontColor(emphaseColor);
		plainUnderlined.setUnderlined(true);
		
		TextStyle italicUnderlined = new TextStyle(FontsVariant.ITALIC);
		italicUnderlined.setUnderlined(true);
		
		paragraph = new Paragraph(new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD_ITALIC, 12), new Text("This paragraph demonstrate that text can simply be put in ")
				, new Text(new TextStyle(FontsVariant.BOLD), "bold,")
				, new Text(italicUnderlined, " italic")
				, new Text(plainUnderlined, " and underlined.")
				, new Text(new TextStyle(TIMES_ROMAN, 14), " Text size can also be changed within a paragraph.")
				, NEW_TEXT_LINE, new Text("Text styles needs to be used inside a \"Text\" object. A paragraph allow a simple String or Text object as parameter."));
		document.addElement(paragraph);

		//Paragraph styles.
		document.addElement(new Paragraph(titleStyle, "Paragraph styles"));
		
		ParagraphStyle paragraphStyle = new ParagraphStyle(TIMES_ROMAN, 14);
		paragraphStyle.setMargins(new Margins(20));
		paragraphStyle.setFirstLineMargin(20);
		paragraph = new Paragraph(paragraphStyle, "This paragraph has a margin of 20 on each side plus a first line margin of 20."
				, NEW_LINE, "It's also composed of few lines that are part of this same paragraph, so this paragraph configuration is applied to all lines of it."
				, NEW_LINE, "So, the first line is shifted by 40 pt from the left while other lines of the paragraph are shifted by 20."
				, NEW_LINE, "This paragraph also ends from 20 pt before page margins and have an top and bottom margin of 20 too."
				, NEW_LINE, "Text of this paragraph has a size of 14 instead of 12 in a default one.");
		document.addElement(paragraph);
		
		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setAlignment(Alignment.RIGHT);
		paragraphStyle.setMargins(new Margins(20, 20, 0, 0));
		paragraphStyle.setFirstLineMargin(50);
		paragraph = new Paragraph(paragraphStyle, "This paragraph is aligned to right. This shows that the first line margin is taken into account."
				+ " So, the text will not be displayed until start of paragraph but with a space of the size of the first line margin."
				+ " The others line will start with the default paragraph margin."
				, NEW_LINE, "This even works with new lines inside a paragraph. New lines will not be sensitive to first line of paragraph margin.");
		document.addElement(paragraph);
		
		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setAlignment(Alignment.CENTER);
		paragraphStyle.setMargins(new Margins(50, 50, 0, 0));
		paragraphStyle.setFirstLineMargin(50);
		paragraph = new Paragraph(paragraphStyle, "This paragraph is aligned to center with a first line margin."
				+ " It demonstrate the same principles than the previous paragraph.");
		document.addElement(paragraph);

		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setLineSpacing(1.5f);
		paragraph = new Paragraph(paragraphStyle, "This paragraph shows that a vertical line spacing ratio can be applied between each line of a paragraph."
				+ " The standard line spacing is just the size of the font. The line spacing ratio is the multication of itself by the font size."
				+ " So if line spacing is set to 2, an empty space of the size of the font is left blank between two lines."
				+ " Both the lines wrapped automatically and"
				, NEW_LINE, "The ones created specifically"
				, NEW_LINE, "will be affected by vertical line spacing ratio.");
		document.addElement(paragraph);

		//Page styles.
		document.addPage(PageStyle.A8_LANDSCAPE);
		
		document.addElement(new Paragraph(titleStyle, "Page styles"));
		
		paragraph = new Paragraph("This page show a page with a different orientation and size.", NEW_LINE, "You can use default ones or create the ones you need.");
		document.addElement(paragraph);

		//Create header for next page.
		pageHeader = new PageHeader();
		paragraphHeader = new Paragraph(paragraphHeaderStyle, logo, new Text("SimplyPDF-layout - web4enterprise - new page header example"));
		pageHeader.addElement(paragraphHeader);
		
		//Set footer before rendering any other element.
		pageFooter = new PageFooter();
		pageFooter.addElement(new Paragraph(new Date().toString() + " - new page footer example"));
		
		//Add a page with new header.
		document.addPage(PageStyle.A4_PORTRAIT, pageHeader, pageFooter);
		
		//Headers and footer title.
		document.addElement(new Paragraph(titleStyle, "Adding headers and footers"));
		
		//Headers and footers explanations.
		paragraph = new Paragraph("Headers and footers can be set once for all for every page or when adding a new page.");
		document.addElement(paragraph);
		
		paragraph = new Paragraph(codeStyle, "PageHeader pageHeader = new PageHeader();", 
				NEW_LINE, 
				"pageHeader.addElement(new Paragraph(\"SimplyPDF-layout - web4enterprise\"));", 
				NEW_LINE,
				"document.addPage(pageHeader, pageFooter);");
		document.addElement(paragraph);

		//Tables.
		document.addElement(new Paragraph(titleStyle, "Adding tables"));
		
		TableCellStyle tableHeaderCellStyle = new TableCellStyle();
		tableHeaderCellStyle.setBordersStyle(new BorderStyle(4.0f, darkEmphaseColor), BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID);
		tableHeaderCellStyle.setBackgroundColor(lightEmphaseColor);
		
		TableCellStyle tableFooterCellStyle = new TableCellStyle(lightCodeColor);
		tableFooterCellStyle.setBordersStyle(BorderStyle.THIN_SOLID, BorderStyle.THIN_SOLID, new BorderStyle(2.0f, darkCodeColor), BorderStyle.THIN_SOLID);
		
		ParagraphStyle tableHeaderStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.PLAIN, 12, titleColor);
		Table table = new Table()
			.addRow(new TableCell(tableHeaderCellStyle, new Paragraph(tableHeaderStyle, "How to ...")), new TableCell(tableHeaderCellStyle, new Paragraph(tableHeaderStyle, "Code")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Create a table")), new TableCell(new Paragraph(codeStyle, "Table table = new Table()")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Add a row")), new TableCell(new Paragraph(codeStyle, "table.addRow(TableCell...)")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Add a cell to a row")), new TableCell(new Paragraph(codeStyle, "new TableCell(\"text of cell\")")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Control column width")), new TableCell(new Paragraph(codeStyle, "table.setColumnWidth(columnIndex, width);")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Add table to page")), new TableCell(new Paragraph(codeStyle, "document.addElement(table)")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Merge rows")).setMergedRows(1), new TableCell(new Paragraph(codeStyle, "cell.setMergedRows(nbRows)")))
			.addRow(new TableCell(), new TableCell())
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Merge columns"), new Paragraph(codeStyle, "cell.setMergedColumns(nbColumns)")).setMergedColumns(1))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Repeat header on a new page")), new TableCell(new Paragraph(codeStyle, "setRepeatHeaderOnNewPage(true)")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Create a cell style")), new TableCell(new Paragraph(codeStyle, "TableCellStyle cellStyle = new TableCellStyle()")))
			.addRow(new TableCell(new Paragraph(emphaseStyle, "Affect style to cell")), new TableCell(new Paragraph(codeStyle, "new TableCell(cellStyle, paragraph)")))
			.addRow(new TableCell(tableFooterCellStyle, new Paragraph("Add footer")), new TableCell(tableFooterCellStyle, new Paragraph(codeStyle, "Just create a new row with a different style.")));
		
		table.setColumnWidth(0, 237.5f);
		table.setColumnWidth(1, 237.5f);
		document.addElement(table);
		
		document.addElement(new Paragraph(NEW_LINE, "More information on tables in table.pdf"));

		//Table of content.
		document.addElement(new Paragraph(titleStyle, "Adding table of content"));
		
		paragraph = new Paragraph(emphaseStyle, "This still have to be coded.");
		document.addElement(paragraph);

		//Internal links.
		document.addElement(new Paragraph(titleStyle, "Adding document internal links"));
		Text linkedText = new Text(internalLinkStyle, "Document internal links");
		document.addElement(new Paragraph(NEW_TEXT_LINE, linkedText, new Text(" can simply be added to any text.")));
		
		paragraph = new Paragraph("An internal link target is nothing special, it's just a simple element like this one but is generally a title or a figure.");
		document.addElement(paragraph);
		
		linkedText.setLink(paragraph);

		//Footnotes.
		document.addElement(new Paragraph(titleStyle, "Adding footnotes"));
		
		Text footNotedText = new Text("A footnote can be added simply by adding a footnote object to a paragraph element.");
		footNotedText.addFootNote(new FootNote(new Paragraph(footNoteStyle, "Any paragraph with texts and images can be added to a footnote.", NEW_LINE, 
				"Footnotes indices are added to text automatically as super-scripts.")));
		paragraph = new Paragraph(footNotedText);
		document.addElement(paragraph);
		
		//Stops.
		document.addElement(new Paragraph(titleStyle, "Using stops"));
		
		paragraph = new Paragraph("A stop can be added anywhere in a paragraph to");
		paragraph.addStop(new Stop(StopType.LEFT, 400.0f));
		paragraph.nextStop("place text near to it.");
		document.addElement(paragraph);
		
		document.finish();
		
		document.write(out);
	}
}
