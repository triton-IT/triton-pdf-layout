package com.web4enterprise.pdf.layout.text;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.web4enterprise.pdf.core.font.Font;
import com.web4enterprise.pdf.core.font.FontVariant;
import com.web4enterprise.pdf.core.geometry.Point;
import com.web4enterprise.pdf.core.styling.Color;
import com.web4enterprise.pdf.core.text.TextScript;
import com.web4enterprise.pdf.layout.document.Element;
import com.web4enterprise.pdf.layout.document.impl.Layouter;
import com.web4enterprise.pdf.layout.page.Page;
import com.web4enterprise.pdf.layout.paragraph.FootNote;
import com.web4enterprise.pdf.layout.paragraph.ParagraphElement;
import com.web4enterprise.pdf.layout.paragraph.ParagraphStyle;

public class Text implements ParagraphElement {
	private static final Logger LOGGER = Logger.getLogger(ParagraphElement.class.getName());
	
	public static final String NEW_LINE = "\n";	
	public static final Text NEW_TEXT_LINE = new Text(NEW_LINE);

	protected TextStyle style = new TextStyle();	

	protected com.web4enterprise.pdf.core.text.Text coreText;
	protected com.web4enterprise.pdf.core.text.Text coreFootNoteTexts;
	
	protected List<FootNote> footNotes = new ArrayList<>();
	
	protected Element linkedElement;
	
	protected List<Text> spawns = new ArrayList<Text>();
	
	public Text(String string) {
		coreText = new com.web4enterprise.pdf.core.text.Text(0.0f, 0.0f, 0.0f, string);
	}
	
	public Text(TextStyle style, String string) {
		coreText = new com.web4enterprise.pdf.core.text.Text(0.0f, 0.0f, 0.0f, string);
		this.style = style;
	}

	public TextStyle getStyle() {
		return style;
	}

	public void setStyle(TextStyle style) {
		this.style = style;
	}

	public String getString() {
		return coreText.getValue();
	}

	public void setString(String string) {
		coreText.setValue(string);
	}
	
	@Override
	public List<ParagraphElement> getLines() {		
		List<ParagraphElement> lines = new ArrayList<>();
		
		//If string starts with a new line, we have to create an empty one.
		if(getString().startsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		//Split will return only one entry if regex if at start or end of string.
		String[] stringLines = getString().split(NEW_LINE);
		for(String stringLine : stringLines) {
			Text singleTextLine = spawn(stringLine);
			singleTextLine.setFootNotes(footNotes);
			lines.add(singleTextLine);
		}
		
		//If string ends with a new line, we have to create an empty one.
		if(getString().endsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		return lines;
	}

	@Override
	public void setLink(Element element) {
		linkedElement = element;
		coreText.setLink(element);
		for(Text spawn : spawns) {
			spawn.setLink(element);
		}
	}

	@Override
	public void addFootNote(FootNote footNote) {
		footNotes.add(footNote);
	}

	public void setFootNotes(List<FootNote> footNotes) {
		this.footNotes = footNotes;
	}	

	@Override
	public List<FootNote> getFootNotes() {
		return footNotes;
	}
	
	@Override
	public SplitInformation split(Layouter layouter, ParagraphStyle defaultStyle, float fontSize, float positionX, float firstLineMaxWidth, Float maxWidth) {
		TextStyle textStyle = getStyle();
		//Get font name between paragraph and text ones.
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		//Get font size between paragraph and text ones.
		float currentTextSize = (textStyle.getFontSize() != null)?textStyle.getFontSize():fontSize;
		
		//Split the text on max width.
		SplitInformation splitInformation = new SplitInformation();
		split(layouter, getString(), currentFontVariant, currentTextSize, positionX, firstLineMaxWidth, maxWidth, splitInformation, true);
		
		return splitInformation;
	}
	
	protected void split(Layouter layouter, String stringToSplit, FontVariant font, float fontSize, float positionX, float firstLineMaxWidth, 
			float maxWidth, SplitInformation splitInformation, boolean isFirstLine) {
		//Calculate maximum size for final line.
		float maximumLineWidth = maxWidth;
		if(isFirstLine) {
			maximumLineWidth = firstLineMaxWidth;
		}
		maximumLineWidth -= positionX;
		
		//Get text size.
		float textWidth = font.getWidth(fontSize, stringToSplit);
		
		//Check if we need to split text.
		if(textWidth > maximumLineWidth) {
			//Initialize the kept String to initial String to split.
			String keptString = stringToSplit;
			//Split text to match maximum line width.
			boolean keepSplitting = textWidth > maximumLineWidth;
			while(keepSplitting) {
				int splitIndex = keptString.lastIndexOf(' ');
				if(splitIndex < 1) {
					LOGGER.warning("'" + stringToSplit + "' cannot be split to fit expected size. Text can be rendered outside its expected bounding box.");
					Text newTextLine = spawn(keptString);
					splitInformation.splitElements.add(newTextLine);
					float keptStringWidth = font.getWidth(fontSize, keptString);
					splitInformation.positionX = keptStringWidth;
					keepSplitting = false;
				} else {
					keptString = stringToSplit.substring(0, splitIndex);
					float keptStringSize = font.getWidth(fontSize, keptString);
					if(keptStringSize <= maximumLineWidth) {
						//This line is splitted, so do not continue to split this specific line.
						keepSplitting = false;
						//Add information on current line to split information.
						Text newTextLine = spawn(keptString);
						splitInformation.splitElements.add(newTextLine);
						float keptStringWidth = font.getWidth(fontSize, stringToSplit);
						splitInformation.positionX = keptStringWidth;
						
						//Try to split the next lines of text.
						String textLeft = stringToSplit.substring(keptString.length());
						//We split on space earlier, we don't want to display it on new line, so remove it.
						split(layouter, textLeft.substring(1), font, fontSize, 0, firstLineMaxWidth, maxWidth, splitInformation, false);
					}
				}
			}
		} else {		
			//Now, calculate with foot notes on last word. We want foot notes to be all together with word and never been separated from it.
			if(footNotes.size() > 0) {
				float currentFootNoteSize = fontSize / 2.0f;
				StringBuilder footNotesBuilder = new StringBuilder(stringToSplit);
				for(FootNote footNote : footNotes) {
					footNotesBuilder.append(footNote.generateId(layouter)).append(" ");
				}
				footNotesBuilder.deleteCharAt(footNotesBuilder.length() - 1);
				String footNotesString = footNotesBuilder.toString();
				
				float footNotesWidth = font.getWidth(currentFootNoteSize, footNotesString);
				//If last line + footNotes does not fit in page, then cut again on last word of the last line to put last word with its foot notes indices.
				if(textWidth + footNotesWidth > maximumLineWidth) {
					int splitIndex = stringToSplit.lastIndexOf(' ');
					if(splitIndex < 1) {
						LOGGER.warning("'" + stringToSplit + "' cannot be split to fit expected size. Text can be rendered outside its expected bounding box.");
					} else {
						String keptString = stringToSplit.substring(0, splitIndex);
						//Add information on current line to split information.
						Text newTextLine = spawn(keptString);
						splitInformation.splitElements.add(newTextLine);
						
						//Remove space on splitting.
						stringToSplit = stringToSplit.substring(splitIndex + 1);
					}
				}
			}
			
			//Finally, add last part of text to split information.
			Text newTextLine = spawn(stringToSplit);
			newTextLine.setFootNotes(footNotes);
			splitInformation.splitElements.add(newTextLine);
			splitInformation.positionX = textWidth;
		}
	}
	
	@Override
	public float getWidth(ParagraphStyle defaultStyle, float defaultTextSize) {
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		float currentTextSize = (style.fontSize != null)?style.fontSize:defaultTextSize;
		
		coreText.setFontVariant(currentFontVariant);
		coreText.setSize(currentTextSize);
		return coreText.getWidth();
	}
	
	@Override
	public Point layout(Page page, ParagraphStyle defaultStyle, float defaultFontSize, float positionX, float positionY) {
		Font currentFont = (style.getFont() != null)?style.getFont():defaultStyle.getFont();
		FontVariant currentFontVariant = currentFont.getVariant((style.getFontStyle() != null)?
				style.getFontStyle():defaultStyle.getFontStyle());
		float currentFontSize = (style.fontSize != null)?style.fontSize:defaultFontSize;
		
		Color color = (style.getFontColor() != null)?style.getFontColor():defaultStyle.getFontColor();
		
		Boolean isUnderlined = (style.isUnderlined() != null)?style.isUnderlined():defaultStyle.isUnderlined();
		
		coreText.setX(positionX);
		coreText.setY(positionY);
		coreText.setSize(currentFontSize);
		coreText.setFontVariant(currentFontVariant);
		coreText.setColor(color);
		coreText.setScript(style.script);
		
		if(Boolean.TRUE.equals(isUnderlined)) {
			coreText.setUnderlined(isUnderlined);
			Color underlineColor = (style.getUnderlineColor() != null)?style.getUnderlineColor():defaultStyle.getUnderlineColor();
			coreText.setUnderlineColor(underlineColor);
		}
		
		page.getCorePage().add(coreText);
		
		float footNotesWidth = 0.0f;
		if(footNotes.size() > 0) {
			StringBuilder footNotesBuilder = new StringBuilder();
			
			for(FootNote footNote : footNotes) {
				footNotesBuilder.append(footNote.getId()).append(" ");
			}
			footNotesBuilder.deleteCharAt(footNotesBuilder.length() - 1);
			float footNotePositionX = positionX + coreText.getWidth();
			coreFootNoteTexts = new com.web4enterprise.pdf.core.text.Text(footNotePositionX, positionY, currentFontSize, currentFontVariant, color, footNotesBuilder.toString());
			coreFootNoteTexts.setScript(TextScript.SUPER);
			
			page.getCorePage().add(coreFootNoteTexts);
		}
		
		return new Point(coreText.getWidth() + footNotesWidth, currentFontSize);
	}

	@Override
	public float getLineSpacing(ParagraphStyle defaultStyle) {
		float textHeight = defaultStyle.getFontVariant().getHeight(defaultStyle.getFontSize());
		
		return textHeight * defaultStyle.getLineSpacing();
	}
	
	@Override
	public Text clone() {
		//TODO: Clone style.
		Text newTextLine = new Text(style, getString());
		if(linkedElement != null) {
			newTextLine.setLink(linkedElement);
		}
		newTextLine.setFootNotes(footNotes);
		return newTextLine;
	}
	
	protected Text spawn(String value) {
		Text spawn = new Text(style, value);
		if(linkedElement != null) {
			spawn.setLink(linkedElement);
		}
		
		spawns.add(spawn);
		
		return spawn;
	}
}
