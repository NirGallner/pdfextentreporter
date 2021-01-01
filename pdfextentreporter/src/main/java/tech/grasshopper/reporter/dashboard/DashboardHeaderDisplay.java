package tech.grasshopper.reporter.dashboard;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.component.text.Text;
import tech.grasshopper.reporter.component.text.TextComponent;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextLengthOptimizer;
import tech.grasshopper.reporter.structure.Display;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DashboardHeaderDisplay extends Display {

	private static final int TITLE_FONT_SIZE = 22;
	private static final PDFont TITLE_FONT = ReportFont.BOLD_FONT;
	private static final int TITLE_X_LOCATION = 50;

	private static final int DATE_FONT_SIZE = 16;
	private static final int DATE_X_LOCATION = 600;

	private static final int Y_LOCATION = 520;

	private final TextLengthOptimizer titleLengthOptimizer = TextLengthOptimizer.builder().font(TITLE_FONT)
			.fontsize(TITLE_FONT_SIZE).spaceWidth(DATE_X_LOCATION - TITLE_X_LOCATION - 20).build();

	@Override
	public void display() {
		createReportTitleText();
		createReportDateText();
	}

	private void createReportTitleText() {
		Text text = Text.builder().textColor(Color.BLACK).font(TITLE_FONT).fontSize(TITLE_FONT_SIZE)
				.text(titleLengthOptimizer.optimizeText("Cucumber Report by Grasshopper")).xlocation(TITLE_X_LOCATION)
				.ylocation(Y_LOCATION).build();
		TextComponent.builder().content(content).text(text).build().display();
	}

	private void createReportDateText() {
		Text text = Text.builder().textColor(Color.BLUE).font(ReportFont.REGULAR_FONT).fontSize(DATE_FONT_SIZE)
				.xlocation(DATE_X_LOCATION).ylocation(Y_LOCATION)
				.text(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))).build();
		TextComponent.builder().content(content).text(text).build().display();
	}
}
