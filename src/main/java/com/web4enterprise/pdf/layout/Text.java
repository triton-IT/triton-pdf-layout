package com.web4enterprise.pdf.layout;

import java.util.ArrayList;
import java.util.List;

import com.web4enterprise.pdf.core.font.FontVariant;

public class Text {
	public static final String NEW_LINE = "\n";	
	public static final Text NEW_TEXT_LINE = new Text(NEW_LINE);

	protected TextStyle style = new TextStyle();
	protected String string;
	
	public Text(String string) {
		this.string = string;
	}
	
	public Text(TextStyle style, String string) {
		this.style = style;
		this.string = string;
	}

	public TextStyle getStyle() {
		return style;
	}

	public void setStyle(TextStyle style) {
		this.style = style;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
	public List<Text> getLines() {
		ArrayList<Text> lines = new ArrayList<>();

		//If string starts with a new line, we have to create an empty one.
		if(string.startsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		//Split will return only one entry if regex if at start or end of string.
		String[] stringLines = string.split(NEW_LINE);
		for(String stringLine : stringLines) {
			lines.add(new Text(style, stringLine));
		}
		
		//If string ends with a new line, we have to create an empty one.
		if(string.endsWith(NEW_LINE)) {
			lines.add(new Text(style, ""));
		}
		
		return lines;
	}

	public void split(FontVariant font, int textSize, int currentPosition, int firstLineMaxWidth, int maxWidth, SplitInformation splitInformation) {
		split(string, font, textSize, currentPosition, firstLineMaxWidth, maxWidth, splitInformation, true);
	}
	
	public void split(String stringToSplit, FontVariant font, int textSize, int currentPosition, int firstLineMaxWidth, 
			int maxWidth, SplitInformation splitInformation, boolean isFirstLine) {
		int textWidth = font.getWidth(textSize, stringToSplit);
		String keptString = stringToSplit;
		
		int currentMaxWidth = maxWidth;
		if(isFirstLine) {
			currentMaxWidth = firstLineMaxWidth;
		}
		//If we have to split.
		while(textWidth > currentMaxWidth - currentPosition) {
			//Get index of split
			int splitIndex = keptString.lastIndexOf(' ');
			//If we can split, try to split again. We test against 0 in addition to -1 because first character can be a space and then string cannot be split anymore.
			if(splitIndex != 0 && splitIndex != -1) {
				keptString = stringToSplit.substring(0, splitIndex);
				textWidth = font.getWidth(textSize, keptString);
			}
		}
		
		//Add information on current line to split information.
		splitInformation.splitTexts.add(new Text(style, keptString));
		splitInformation.newPosition = textWidth;
		
		//Try to split the text left.
		if(stringToSplit.length() != keptString.length()) {
			String textLeft = stringToSplit.substring(keptString.length());
			split(textLeft, font, textSize, 0, firstLineMaxWidth, maxWidth, splitInformation, false);
		}
	}
	
	public class SplitInformation {
		public int newPosition;
		List<Text> splitTexts = new ArrayList<>();
	}
}
