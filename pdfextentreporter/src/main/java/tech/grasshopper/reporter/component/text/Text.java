package tech.grasshopper.reporter.component.text;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.reporter.font.ReportFont;

@Data
@Builder
public class Text {

	@Default
	private Color textColor = Color.BLACK;
	@Default
	private PDFont font = ReportFont.REGULAR_FONT;
	@Default
	private float fontSize = 12;
	private float xlocation;
	private float ylocation;
	private String text;
}
