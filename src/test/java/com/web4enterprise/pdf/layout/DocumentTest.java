package com.web4enterprise.pdf.layout;

import static com.web4enterprise.pdf.core.font.Font.TIMES_ROMAN;
import static com.web4enterprise.pdf.layout.Text.NEW_LINE;
import static com.web4enterprise.pdf.layout.Text.NEW_TEXT_LINE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.web4enterprise.pdf.core.Color;
import com.web4enterprise.pdf.core.PdfGenerationException;
import com.web4enterprise.pdf.core.font.FontStyle;

public class DocumentTest {
	@Test
	public void writeTest() throws IOException, PdfGenerationException {
		OutputStream out = new FileOutputStream("documentation.pdf");
		
		PageStyle pageStyle = new PageStyle();
		Document document = new Document(pageStyle);

		Paragraph paragraph = new Paragraph("This first page is a standard page in A4 format. This first paragraph has nothing special...");
		document.addParagraph(paragraph);
		
		ParagraphStyle paragraphStyle = new ParagraphStyle(TIMES_ROMAN, 14);
		paragraphStyle.setMargins(new Margins(20, 20, 20, 20));
		paragraphStyle.setFirstLineMargin(20);
		paragraph = new Paragraph(paragraphStyle, "This paragraph has a margin of 20 on each side plus a first line margin of 20."
				, NEW_LINE, "It's also composed of few lines that are part of this same paragraph, so this paragraph configuration is applied to all lines of it."
				, NEW_LINE, "So, the first line is shifted by 40 pt from the left while other lines of the paragraph are shifted by 20."
				, NEW_LINE, "This paragraph also ends from 20 pt before page margins and have an top and bottom margin of 20 too."
				, NEW_LINE, "Text of this paragraph has a size of 14 instead of 12 in a default one.");
		document.addParagraph(paragraph);
		
		TextStyle colorPurple = new TextStyle();
		colorPurple.setFontColor(new Color(200,  0, 200));
		
		paragraphStyle = new ParagraphStyle();
		paragraphStyle.setFontColor(new Color(0, 100, 150));
		paragraph = new Paragraph(paragraphStyle, new Text("A color can be defined for an entire paragraph but also for ")
				, new Text(colorPurple, "a subset")
				, new Text("of the same paragraph."));
		document.addParagraph(paragraph);

		TextStyle plainUnderlined = new TextStyle(FontStyle.PLAIN);
		plainUnderlined.setFontColor(new Color(200,  0, 200));
		plainUnderlined.setUnderlined(true);
		
		TextStyle italicUnderlined = new TextStyle(FontStyle.PLAIN);
		italicUnderlined.setUnderlined(true);
		
		paragraph = new Paragraph(new ParagraphStyle(TIMES_ROMAN, FontStyle.BOLD_ITALIC, 12), new Text("This paragraph demonstrate that text can simply be put in ")
				, new Text(new TextStyle(FontStyle.BOLD), "bold,")
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
		
		PageStyle pageStyle1 = new PageStyle(PageFormat.A3_LANDSCAPE, new Margins(40, 40, 40, 40));
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
