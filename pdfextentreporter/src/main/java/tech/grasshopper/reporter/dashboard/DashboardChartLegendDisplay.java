package tech.grasshopper.reporter.dashboard;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.INFO;
import static com.aventstack.extentreports.Status.PASS;
import static com.aventstack.extentreports.Status.SKIP;
import static com.aventstack.extentreports.Status.WARNING;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.aventstack.extentreports.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.component.Component;
import tech.grasshopper.reporter.component.decorator.BackgroundDecorator;
import tech.grasshopper.reporter.component.text.Text;
import tech.grasshopper.reporter.component.text.TextComponent;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.structure.Display;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DashboardChartLegendDisplay extends Display {

	private static final int TOP_LEGEND_Y_LOCATION = 180;
	private static final int LEGEND_Y_GAP = 25;
	private static final int CHART_TITLE_X_PADDING = 25;
	private static final int LEGEND_KEY_WIDTH = 50;
	private static final int LEGEND_KEY_HEIGHT = 20;
	private static final int LEGEND_KEY_FONT_SIZE = 11;
	private static final int LEGEND_VALUE_FONT_SIZE = 14;

	private static final Map<Status, Color> statusColor = new LinkedHashMap<>();
	private AnalysisStrategyDisplay strategyDisplay;

	@Override
	public void display() {

		statusColor.put(PASS, Color.GREEN);
		statusColor.put(FAIL, Color.RED);
		statusColor.put(SKIP, Color.ORANGE);
		statusColor.put(WARNING, Color.YELLOW);
		statusColor.put(INFO, Color.BLUE);

		strategyDisplay = AnalysisStrategyDisplay.displaySettings((report.getStats().getAnalysisStrategy()));

		createFirstChartDataBox();
		createSecondChartDataBox();
		createThirdChartDataBox();
		createLogsChartDataBox();
	}

	private void createFirstChartDataBox() {
		createLegendData(report.getStats().getParent(),
				strategyDisplay.firstLevelChartXLocation() + CHART_TITLE_X_PADDING);
	}

	private void createSecondChartDataBox() {
		createLegendData(report.getStats().getChild(),
				strategyDisplay.secondLevelChartXLocation() + CHART_TITLE_X_PADDING);
	}

	private void createThirdChartDataBox() {
		createLegendData(report.getStats().getGrandchild(),
				strategyDisplay.thirdLevelChartXLocation() + CHART_TITLE_X_PADDING);
	}

	private void createLogsChartDataBox() {
		if (strategyDisplay.displayLogsChart())
			createLegendData(report.getStats().getLog(), strategyDisplay.logsChartXLocation() + CHART_TITLE_X_PADDING);
	}

	private void createLegendData(Map<Status, Long> statusData, float xlocation) {
		float yTopLocation = TOP_LEGEND_Y_LOCATION;
		for (Entry<Status, Color> entry : statusColor.entrySet()) {
			long value = statusData.getOrDefault(entry.getKey(), 0L);
			if (value > 0) {
				createLegendKey(entry.getKey().toString(), xlocation, yTopLocation, entry.getValue());
				createLegendValue(xlocation, yTopLocation, value);
				yTopLocation = yTopLocation - LEGEND_Y_GAP;
			}
		}
	}

	private void createLegendKey(String legendText, float xlocation, float ylocation, Color legendColor) {
		Text text = Text.builder().fontSize(LEGEND_KEY_FONT_SIZE).xlocation(xlocation).ylocation(ylocation)
				.text(legendText).font(ReportFont.ITALIC_FONT).build();
		Component component = TextComponent.builder().content(content).text(text).build();
		component = BackgroundDecorator.builder().component(component).content(content).containerColor(legendColor)
				.xContainerBottomLeft(xlocation - 5).yContainerBottomLeft(ylocation - 6)
				.containerWidth(LEGEND_KEY_WIDTH).containerHeight(LEGEND_KEY_HEIGHT).build();
		component.display();
	}

	private void createLegendValue(float xlocation, float ylocation, Long legendValue) {
		Text text = Text.builder().fontSize(LEGEND_VALUE_FONT_SIZE).xlocation(xlocation + 65).ylocation(ylocation)
				.text(String.valueOf(legendValue)).font(ReportFont.BOLD_FONT).build();
		Component component = TextComponent.builder().content(content).text(text).build();
		component.display();
	}
}
