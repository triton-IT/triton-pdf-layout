package com.web4enterprise.pdf.layout;

import static com.web4enterprise.pdf.core.font.Font.TIMES_ROMAN;
import static com.web4enterprise.pdf.layout.Text.NEW_LINE;
import static com.web4enterprise.pdf.layout.Text.NEW_TEXT_LINE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.web4enterprise.pdf.core.exceptions.PdfGenerationException;
import com.web4enterprise.pdf.core.font.FontsVariant;
import com.web4enterprise.pdf.core.styling.Color;

public class DocumentTest {
	@Test
	public void writeTest() throws IOException, PdfGenerationException {
		OutputStream out = new FileOutputStream("documentation.pdf");
		
		ParagraphStyle codeStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.ITALIC, 10, new Color(80, 128, 128));
		ParagraphStyle titleStyle = new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD, 14);
		titleStyle.setMargins(new Margins(0, 0, 10, 10));
		
		Document document = new Document();

		document.addParagraph(new Paragraph(titleStyle, "Creating a document"));
		
		Paragraph paragraph = new Paragraph("A PDF is created with:", NEW_LINE);
		paragraph.addElement(new Text(codeStyle, "Document document = new Document();"), NEW_TEXT_LINE,
				new Text("and rendered with;"), NEW_TEXT_LINE,
				new Text(codeStyle, "document.write(out);"));
		document.addParagraph(paragraph);

		document.addParagraph(new Paragraph(titleStyle, "Adding texts, paragraphs and pages"));

		paragraph = new Paragraph("A document created with no page style uses the default one: A4 portrait.", NEW_LINE);
		paragraph.addElement(new Text("No need to add new pages, when an element does not fit into the page, a new page with same style than the previous one is created."), 
				NEW_TEXT_LINE);
		paragraph.addElement(new Text("The same is applicable to lines. When a line does not into the page width, the text is automatically splitted on the last space "
				+ "that fits the page and the text that left is wrapped to a new line. This same principle is applicable indefinitively to all new lines created."), 
				NEW_TEXT_LINE);
		paragraph.addElement(new Text("Of course, new lines and new pages can be created at any time."), 
				NEW_TEXT_LINE);
		document.addParagraph(paragraph);
		
		paragraph = new Paragraph("A paragraph is created with:", NEW_LINE);
		paragraph.addElement(new Text(codeStyle, "Paragraph paragraph = new Paragraph(\"Text of paragraph\");"), NEW_TEXT_LINE,
				new Text("A new page is created with;"), NEW_TEXT_LINE,
				new Text(codeStyle, "document.addPage();"), NEW_TEXT_LINE,
				new Text("Paragraph style (margins, first line margin, text color, size, font, etc.) can be defined using the constructor for most of parameters. For others, "
						+ "getters and setters can be used."), NEW_TEXT_LINE,
				new Text("The same applies to most of the classes (Page, Text, etc.)."));
		document.addParagraph(paragraph);

		document.addParagraph(new Paragraph(titleStyle, "Adding images and graphics"));

		document.addParagraph(new Paragraph(titleStyle, "Text styles"));

		document.addParagraph(new Paragraph(titleStyle, "Paragraph styles"));

		document.addParagraph(new Paragraph(titleStyle, "Page styles"));

		document.addParagraph(new Paragraph(titleStyle, "Adding headers and footers"));

		document.addParagraph(new Paragraph(titleStyle, "Adding tables"));

		document.addParagraph(new Paragraph(titleStyle, "Adding table of content"));

		document.addParagraph(new Paragraph(titleStyle, "Adding footnotes"));
		
		ParagraphStyle paragraphStyle = new ParagraphStyle(TIMES_ROMAN, 14);
		paragraphStyle.setMargins(new Margins(20));
		paragraphStyle.setFirstLineMargin(20);
		paragraph = new Paragraph(paragraphStyle, "This paragraph has a margin of 20 on each side plus a first line margin of 20."
				, NEW_LINE, "It's also composed of few lines that are part of this same paragraph, so this paragraph configuration is applied to all lines of it."
				, NEW_LINE, "So, the first line is shifted by 40 pt from the left while other lines of the paragraph are shifted by 20."
				, NEW_LINE, "This paragraph also ends from 20 pt before page margins and have an top and bottom margin of 20 too."
				, NEW_LINE, "Text of this paragraph has a size of 14 instead of 12 in a default one.");
		document.addParagraph(paragraph);
		
		Image image = document.createImage(this.getClass().getResourceAsStream("/test.png"));
		paragraphStyle = new ParagraphStyle();
		paragraph = new Paragraph(paragraphStyle, new Text("A paragraph can contain text but also images like this one:"), image);
		document.addParagraph(paragraph);
		
		
		TextStyle colorPurple = new TextStyle();
		colorPurple.setFontColor(new Color(128, 80, 128));
		
		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setFontColor(new Color(128, 128, 80));
		paragraph = new Paragraph(paragraphStyle, new Text("A color can be defined for an entire paragraph but also for ")
				, new Text(colorPurple, "a subset ")
				, new Text("of the same paragraph."));
		document.addParagraph(paragraph);

		TextStyle plainUnderlined = new TextStyle(FontsVariant.PLAIN);
		plainUnderlined.setFontColor(new Color(128, 80, 128));
		plainUnderlined.setUnderlined(true);
		
		TextStyle italicUnderlined = new TextStyle(FontsVariant.PLAIN);
		italicUnderlined.setUnderlined(true);
		
		paragraph = new Paragraph(new ParagraphStyle(TIMES_ROMAN, FontsVariant.BOLD_ITALIC, 12), new Text("This paragraph demonstrate that text can simply be put in ")
				, new Text(new TextStyle(FontsVariant.BOLD), "bold,")
				, new Text(italicUnderlined, " italic")
				, new Text(plainUnderlined, " and underlined.")
				, new Text(new TextStyle(TIMES_ROMAN, 14), " Text size can also be changed within a paragraph.")
				, NEW_TEXT_LINE, new Text("Text styles needs to be used inside a \"Text\" object. A paragraph allow a simple String or Text object as parameter."));
		document.addParagraph(paragraph);
		
		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setAlignment(Alignment.RIGHT);
		paragraphStyle.setMargins(new Margins(20, 20, 0, 0));
		paragraphStyle.setFirstLineMargin(50);
		paragraph = new Paragraph(paragraphStyle, "This paragraph is aligned to right. This shows that the first line margin is taken into account."
				+ " So, the text will not be displayed until start of paragraph but with a space of the size of the first line margin."
				+ " The others line will start with the default paragraph margin."
				, NEW_LINE, "This even works with new lines inside a paragraph. New lines will not be sensitive to first line of paragraph margin.");
		document.addParagraph(paragraph);
		
		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setAlignment(Alignment.CENTER);
		paragraphStyle.setMargins(new Margins(50, 50, 0, 0));
		paragraphStyle.setFirstLineMargin(50);
		paragraph = new Paragraph(paragraphStyle, "This paragraph is aligned to center with a first line margin."
				+ " It demonstrate the same principles than the previous paragraph.");
		document.addParagraph(paragraph);
		
		paragraphStyle = new ParagraphStyle(TIMES_ROMAN, 24);
		paragraphStyle.setMargins(new Margins(120, 120, 50, 0));
		paragraph = new Paragraph(paragraphStyle, "This next paragraph is just written in a big font size with anormaly large margins "
				+ "because it is just here to prove that pages are created automatically when end of page is reached."
				, NEW_LINE, "Therefore this paragraph is started on one page and the end of it is rendered automaticaly on another one"
				+ " without coding anything special.");
		document.addParagraph(paragraph);
		
		paragraphStyle = new ParagraphStyle();
		paragraph = new Paragraph(paragraphStyle, "The next page will show a page with a different orientation and size.");
		document.addParagraph(paragraph);
		
		PageStyle pageStyle1 = new PageStyle(PageFormat.A3_LANDSCAPE, new Margins(40));
		document.addPage(pageStyle1);

		paragraphStyle = new ParagraphStyle(TIMES_ROMAN, 14);
		paragraphStyle.setLineSpacing(2.0f);
		paragraph = new Paragraph(paragraphStyle, "This paragraph shows that a vertical line spacing ratio can be applied between each line of a paragraph."
				+ " The standard line spacing is just the size of the font. The line spacing ratio is the multication of itself by the font size."
				+ " So if line spacing is set to 2, an empty space of the size of the font is left blanck between two lines."
				+ " Both the lines wrapped automatically and"
				, NEW_LINE, "The ones created specifically"
				, NEW_LINE, "will be affected by vertical line spacing ratio.");
		document.addParagraph(paragraph);
		
		document.write(out);
	}
}
