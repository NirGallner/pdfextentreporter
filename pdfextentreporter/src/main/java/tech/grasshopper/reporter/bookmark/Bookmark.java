package tech.grasshopper.reporter.bookmark;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import lombok.Builder;
import tech.grasshopper.reporter.context.AttributeType;
import tech.grasshopper.reporter.destination.Destination;
import tech.grasshopper.reporter.destination.Destination.DestinationStore;

@Builder
public class Bookmark {

	// private ReportConfig reportConfig;

	public final static String SUMMARY_BOOKMARK_TEXT = "SUMMARY";
	public final static String DASHBOARD_BOOKMARK_TEXT = "DASHBOARD";
	public final static String ATTRIBUTES_BOOKMARK_TEXT = "ATTRIBUTES";
	public final static String TESTS_BOOKMARK_TEXT = "TESTS";

	public PDDocumentOutline createDocumentOutline(DestinationStore destinationStore/* , ReportData reportData */) {

		PDDocumentOutline outline = new PDDocumentOutline();

		PDOutlineItem summaryOutline = new PDOutlineItem();
		summaryOutline.setTitle(SUMMARY_BOOKMARK_TEXT);
		summaryOutline.setBold(true);
		outline.addLast(summaryOutline);

		PDOutlineItem dashboardOutline = createOutlineItem(destinationStore.getDashboardDestination(),
				destinationStore.getDashboardDestination().getName());
		summaryOutline.addLast(dashboardOutline);

		if (!destinationStore.getAttributeSummaryDestinations().isEmpty()) {
			PDOutlineItem attributeOutline = createOutlineItem(
					destinationStore.getAttributeSummaryDestinations().get(0), ATTRIBUTES_BOOKMARK_TEXT);
			summaryOutline.addLast(attributeOutline);

			for (Destination destination : destinationStore.getAttributeSummaryDestinations()) {
				PDOutlineItem attTypeOutline = createOutlineItem(destination,
						AttributeType.valueOf(destination.getName()).toString());
				attributeOutline.addLast(attTypeOutline);
			}
		}

		if (!destinationStore.getTestDestinations().isEmpty())
			outline.addLast(createChapterOutlineItems(destinationStore.getTestDestinations(), TESTS_BOOKMARK_TEXT));

		if (!destinationStore.getAttributeDetailDestinations().isEmpty()) {

			PDOutlineItem attributeOutline = createOutlineItem(destinationStore.getAttributeDetailDestinations().get(0),
					ATTRIBUTES_BOOKMARK_TEXT);
			attributeOutline.setBold(true);
			outline.addLast(attributeOutline);

			Map<AttributeType, List<Destination>> typeDestinationMap = destinationStore.getAttributeDetailDestinations()
					.stream().collect(Collectors.groupingBy(d -> {
						String name = d.getName();
						return AttributeType.valueOf(name.substring(0, name.indexOf("- ")).toUpperCase());
					}, LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));

			typeDestinationMap.forEach((type, destinations) -> {
				PDOutlineItem attTypeOutline = createOutlineItem(destinations.get(0), type.toString());
				attributeOutline.addLast(attTypeOutline);

				destinations.forEach(dest -> {
					String name = dest.getName();
					PDOutlineItem attOutline = createOutlineItem(dest, name.substring(name.indexOf("- ") + 2));
					attTypeOutline.addLast(attOutline);
				});
			});
		}
		return outline;
	}

	private PDOutlineItem createOutlineItem(Destination destination, String title) {
		return createOutlineItem(destination.createPDPageDestination(), title);
	}

	private PDOutlineItem createOutlineItem(Destination destination) {
		return createOutlineItem(destination, destination.getName());
	}

	private PDOutlineItem createOutlineItem(PDDestination destination, String title) {
		PDOutlineItem bookmark = new PDOutlineItem();
		bookmark.setDestination(destination);
		bookmark.setTitle(title);
		return bookmark;
	}

	private PDOutlineItem createChapterOutlineItems(List<Destination> destinations, String title) {
		PDOutlineItem chapterBookmark = createOutlineItem(destinations.get(0), title);
		chapterBookmark.setBold(true);
		destinations.forEach(d -> {
			PDOutlineItem pagesBookmark = createOutlineItem(d);
			chapterBookmark.addLast(pagesBookmark);
		});
		return chapterBookmark;
	}
}
