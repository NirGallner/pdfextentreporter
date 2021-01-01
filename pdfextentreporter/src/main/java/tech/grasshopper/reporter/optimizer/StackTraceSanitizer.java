package tech.grasshopper.reporter.optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StackTraceSanitizer {

	private PDFont font;

	public String sanitizeText(String text) {
		StringBuffer strBuf = new StringBuffer();
		try {
			font.encode(text);
			return text;
		} catch (Exception e) {
			char[] chars = text.toCharArray();
			for (Character character : chars) {
				try {
					font.encode(character.toString());
					strBuf.append(character);
				} catch (Exception ex) {
					strBuf.append("");
				}
			}
		}
		return strBuf.toString();
	}

	public String sanitizeTextMultiLine(String text) {
		StringBuffer strBuf = new StringBuffer();
		String[] lines = text.split("\\r?\\n");

		for (String line : lines) {
			strBuf.append(sanitizeText(line));
		}
		return strBuf.toString();
	}

	public String stm(String text) {
		String[] lines = text.split("\\r?\\n");
		List<String> splitLines = new ArrayList<>();

		for (String line : lines)
			splitLines.add(sanitizeText(line));

		return splitLines.stream().collect(Collectors.joining(System.getProperty("line.separator")));
	}
}
