package tech.grasshopper.reporter.dashboard;

import java.awt.Color;
import java.util.Map;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.PieStyler.ClockwiseDirectionType;
import org.knowm.xchart.style.Styler.ChartTheme;

import com.aventstack.extentreports.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.component.chart.ChartDisplayer;
import tech.grasshopper.reporter.component.text.Text;
import tech.grasshopper.reporter.component.text.TextComponent;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.structure.Display;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DashboardDonutChartDisplay extends Display {

	private PieChart chart;
	
	private static final int CHART_TITLE_X_PADDING = 10;
	private static final int CHART_TITLE_Y_LOCATION = 390;
	private static final int CHART_TITLE_FONT_SIZE = 16;

	private static final int CHART_HEIGHT = 170;
	private static final int CHART_Y_LOCATION = 210;

	private AnalysisStrategyDisplay strategyDisplay;

	@Override
	public void display() {

		strategyDisplay = AnalysisStrategyDisplay.displaySettings((report.getStats().getAnalysisStrategy()));

		createFirstChartTitle();
		createSecondChartTitle();
		createThirdChartTitle();
		createLogsChartTitle();

		createFirstDonutChart();
		createSecondDonutChart();
		createThirdDonutChart();
		createLogsDonutChart();
	}

	private void createFirstChartTitle() {
		Text text = Text.builder().fontSize(CHART_TITLE_FONT_SIZE)
				.xlocation(strategyDisplay.firstLevelChartXLocation() + CHART_TITLE_X_PADDING)
				.ylocation(CHART_TITLE_Y_LOCATION).text(strategyDisplay.firstLevelText()).font(ReportFont.ITALIC_FONT)
				.build();
		TextComponent.builder().content(content).text(text).build().display();
	}

	private void createSecondChartTitle() {
		Text text = Text.builder().fontSize(CHART_TITLE_FONT_SIZE)
				.xlocation(strategyDisplay.secondLevelChartXLocation() + CHART_TITLE_X_PADDING)
				.ylocation(CHART_TITLE_Y_LOCATION).text(strategyDisplay.secondLevelText()).font(ReportFont.ITALIC_FONT)
				.build();
		TextComponent.builder().content(content).text(text).build().display();
	}

	private void createThirdChartTitle() {
		Text text = Text.builder().fontSize(CHART_TITLE_FONT_SIZE)
				.xlocation(strategyDisplay.thirdLevelChartXLocation() + CHART_TITLE_X_PADDING)
				.ylocation(CHART_TITLE_Y_LOCATION).text(strategyDisplay.thirdLevelText()).font(ReportFont.ITALIC_FONT)
				.build();
		TextComponent.builder().content(content).text(text).build().display();
	}

	private void createLogsChartTitle() {
		if (strategyDisplay.displayLogsChart()) {
			Text text = Text.builder().fontSize(CHART_TITLE_FONT_SIZE)
					.xlocation(strategyDisplay.logsChartXLocation() + CHART_TITLE_X_PADDING)
					.ylocation(CHART_TITLE_Y_LOCATION).text(strategyDisplay.logsText()).font(ReportFont.ITALIC_FONT)
					.build();
			TextComponent.builder().content(content).text(text).build().display();
		}
	}

	private void createFirstDonutChart() {
		createDonutChart(report.getStats().getParent(), strategyDisplay.firstLevelChartXLocation());
	}

	private void createSecondDonutChart() {
		createDonutChart(report.getStats().getChild(), strategyDisplay.secondLevelChartXLocation());
	}

	private void createThirdDonutChart() {
		createDonutChart(report.getStats().getGrandchild(), strategyDisplay.thirdLevelChartXLocation());
	}

	private void createLogsDonutChart() {
		if (strategyDisplay.displayLogsChart())
			createDonutChart(report.getStats().getLog(), strategyDisplay.logsChartXLocation());
	}

	private void createDonutChart(Map<Status, Long> data, float xLocation) {
		chart = new PieChart(strategyDisplay.chartWidth(), CHART_HEIGHT, ChartTheme.XChart);
		updateChartStyler(chart.getStyler());
		updateChartData(data);

		ChartDisplayer.builder().document(document).content(content).chart(chart).xBottomLeft(xLocation)
				.yBottomLeft(CHART_Y_LOCATION).build().display();
	}

	private void updateChartStyler(PieStyler styler) {
		styler.setSeriesColors(new Color[] { Color.GREEN, Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE });
		styler.setLegendVisible(false);
		styler.setPlotContentSize(0.85);
		styler.setPlotBorderVisible(true);
		styler.setPlotBorderColor(Color.BLACK);
		styler.setChartPadding(1);
		styler.setClockwiseDirectionType(ClockwiseDirectionType.CLOCKWISE);
		styler.setHasAnnotations(false);
		styler.setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
		styler.setDonutThickness(0.4);
		styler.setSumVisible(true);
		styler.setSumFontSize(20);
		styler.setDecimalPattern("#");
		styler.setChartBackgroundColor(Color.WHITE);
	}

	private void updateChartData(Map<Status, Long> statusData) {
		chart.addSeries(Status.PASS.toString(), statusData.getOrDefault(Status.PASS, 0L));
		chart.addSeries(Status.FAIL.toString(), statusData.getOrDefault(Status.FAIL, 0L));
		chart.addSeries(Status.SKIP.toString(), statusData.getOrDefault(Status.WARNING, 0L));
		chart.addSeries(Status.WARNING.toString(), statusData.getOrDefault(Status.SKIP, 0L));
		chart.addSeries(Status.INFO.toString(), statusData.getOrDefault(Status.INFO, 0L));
	}
}
