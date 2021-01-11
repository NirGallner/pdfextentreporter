package tech.grasshopper.reporter.dashboard;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.bookmark.Bookmark;
import tech.grasshopper.reporter.destination.Destination;
import tech.grasshopper.reporter.destination.DestinationAware;
import tech.grasshopper.reporter.structure.PageCreator;
import tech.grasshopper.reporter.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Dashboard extends Section implements DestinationAware {

	private PageCreator pageCreator;

	@Override
	public void createSection() {
		createPage();
		displayReportHeader();
		displayTestStatistics();
		displayTestChart();

		createDestination();

		closeContentStream();
	}

	protected void createPage() {
		pageCreator = PageCreator.builder().document(document).build();
		pageCreator.createLandscapePageAndContentStream();
	}

	private void displayReportHeader() {
		DashboardHeaderDisplay.builder().config(config).content(pageCreator.getContent()).build().display();
	}

	private void displayTestStatistics() {
		DashboardStatisticsDisplay.builder().config(config).content(pageCreator.getContent()).report(report).build()
				.display();
	}

	private void displayTestChart() {
		DashboardDonutChartDisplay.builder().document(document).config(config).content(pageCreator.getContent())
				.report(report).build().display();
		DashboardChartLegendDisplay.builder().config(config).content(pageCreator.getContent()).report(report).build()
				.display();
	}

	private void closeContentStream() {
		pageCreator.closeContentStream();
	}

	@Override
	public Destination createDestination() {
		Destination destination = Destination.builder().name(Bookmark.DASHBOARD_BOOKMARK_TEXT).yCoord(500)
				.page(pageCreator.getPage()).build();
		destinations.setDashboardDestination(destination);
		return destination;
	}
}
