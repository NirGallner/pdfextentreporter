package tech.grasshopper.reporter.tests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Log;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.StackTraceSanitizer;

@Data
@Builder
public class TestStackTrace {

	private Log log;

	private PDFont font;
	private int fontSize;
	private float padding;
	private float width;
	@Default
	private float lineSpacing = 1f;
	private float height;
	@Default
	private Color color = Color.RED;

	public TextCell createStackTraceCell() {

		StackTraceSanitizer sanitizer = StackTraceSanitizer.builder().font(font).build();
		String stackTrace = log.getException().getStackTrace();
		String stackText = "";

		if (stackTrace != null && !stackTrace.isEmpty()) {
			String[] lines = stackTrace.trim().split("\\r?\\n");
			List<String> splitLines = new ArrayList<>();

			for (String line : lines)
				splitLines.add(sanitizer.sanitizeText(line));

			stackText = splitLines.stream().collect(Collectors.joining(System.getProperty("line.separator")));
		}
		return TextCell.builder().text(stackText).lineSpacing(lineSpacing).minHeight(height)
				.font(ReportFont.REGULAR_FONT).fontSize(fontSize).wordBreak(true).textColor(color).build();
	}
}
