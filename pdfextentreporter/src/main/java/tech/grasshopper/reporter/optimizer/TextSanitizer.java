package tech.grasshopper.reporter.optimizer;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class TextSanitizer {

	private PDFont font;

	@Default
	private String replaceBy = "?";

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
					strBuf.append(replaceBy);
				}
			}
		}
		return strBuf.toString();
	}
}
