package com.web4enterprise.pdf.layout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.web4enterprise.pdf.layout.Document;
import com.web4enterprise.pdf.layout.Margins;
import com.web4enterprise.pdf.layout.PageFormat;
import com.web4enterprise.pdf.layout.PageStyle;
import com.web4enterprise.pdf.layout.Paragraph;
import com.web4enterprise.pdf.layout.ParagraphStyle;
import com.web4enterprise.pdf.core.PdfGenerationException;

public class DocumentTest {
	@Test
	public void writeTest() throws IOException, PdfGenerationException {
		OutputStream out = new FileOutputStream("documentation.pdf");
		
		PageStyle pageStyle = new PageStyle();
		Document document = new Document(pageStyle);

		Paragraph paragraph = new Paragraph("This first page is a standard page in A4 format. This first paragraph has nothing special...");
		document.addParagraph(paragraph);
		
		ParagraphStyle paragraphStyle = new ParagraphStyle(14);
		paragraphStyle.setMargins(new Margins(20, 20, 20, 20));
		paragraphStyle.setFirstLineMargin(20);
		paragraph = new Paragraph(paragraphStyle, "This paragraph has a margin of 20 on each side plus a first line margin of 20."
				, "It's also composed of few lines that are part of this same paragraph, so this paragraph configuration is applied to all lines of it."
				, "So, the first line is shifted by 40 pt from the left while other lines of the paragraph are shifted by 20."
				, "This paragraph also ends from 20 pt before page margins and have an top and bottom margin of 20 too."
				, "Text of this paragraph has a size of 14 instead of 12 in a default one.");
		document.addParagraph(paragraph);
		
		paragraphStyle = new ParagraphStyle(24);
		paragraphStyle.setMargins(new Margins(120, 120, 50, 0));
		paragraph = new Paragraph(paragraphStyle, "This next paragraph is just written in a big font size with anormaly large margins "
				+ "because it is just here to prove that pages are created automatically when end of page is reached."
				, "Therefore this paragraph is started on one page and the end of it is rendered automaticaly on another one"
				+ " without coding anything special.");
		document.addParagraph(paragraph);
		
		paragraphStyle = new ParagraphStyle();
		paragraph = new Paragraph(paragraphStyle, "The next page will show a page with a different orientation and size.");
		document.addParagraph(paragraph);
		
		PageStyle pageStyle1 = new PageStyle(PageFormat.A3_LANDSCAPE, new Margins(40, 40, 40, 40));
		document.addPage(pageStyle1);

		paragraphStyle = new ParagraphStyle(14);
		paragraphStyle.setLineSpacing(2.0f);
		paragraph = new Paragraph(paragraphStyle, "This paragraph shows that a vertical line spacing ratio can be applied between each line of a paragraph."
				+ " The standard line spacing is just the size of the font. The line spacing ratio is the multication of itself by the font size."
				+ " So if line spacing is set to 2, an empty space of the size of the font is left blanck between two lines."
				+ " Both the lines wrapped automatically and"
				, "The ones created specifically"
				, "will be affected by vertical line spacing ratio.");
		document.addParagraph(paragraph);
		
		document.write(out);
	}
}
