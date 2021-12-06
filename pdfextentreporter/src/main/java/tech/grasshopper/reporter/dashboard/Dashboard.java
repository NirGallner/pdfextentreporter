package tech.grasshopper.reporter.dashboard;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.bookmark.Bookmark;
import tech.grasshopper.reporter.destination.Destination;
import tech.grasshopper.reporter.destination.DestinationAware;
import tech.grasshopper.reporter.structure.PageCreator;
import tech.grasshopper.reporter.structure.Section;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Dashboard extends Section implements DestinationAware {

	private PDPage page;

	@Override
	@SneakyThrows
	public void createSection() {

		createPage();

		try (final PDPageContentStream content = new PDPageContentStream(document, page, AppendMode.APPEND, true)) {

			DashboardHeaderDisplay.builder().config(config).content(content).reportFont(reportFont).build().display();

			DashboardStatisticsDisplay.builder().config(config).content(content).report(report).reportFont(reportFont)
					.build().display();

			DashboardDonutChartDisplay.builder().document(document).config(config).content(content).report(report)
					.reportFont(reportFont).build().display();
			DashboardChartLegendDisplay.builder().config(config).content(content).report(report).reportFont(reportFont)
					.build().display();
		}

		createDestination();
	}

	@Override
	protected void createPage() {
		page = PageCreator.createLandscapePageAndAddToDocument(document);
	}

	@Override
	public Destination createDestination() {
		Destination destination = Destination.builder().name(Bookmark.DASHBOARD_BOOKMARK_TEXT).yCoord(500).page(page)
				.build();
		destinations.setDashboardDestination(destination);
		return destination;
	}

	@Override
	public String getSectionTitle() {
		return "";
	}
}
