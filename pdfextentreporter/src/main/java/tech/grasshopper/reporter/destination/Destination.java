package tech.grasshopper.reporter.destination;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Destination {

	private String name;
	private PDPage page;
	@Default
	private int xCoord = 0;
	@Default
	private int yCoord = 0;

	public PDPageXYZDestination createPDPageDestination() {
		PDPageXYZDestination destination = new PDPageXYZDestination();
		destination.setPage(page);
		destination.setLeft(xCoord);
		destination.setTop(yCoord);
		return destination;
	}

	@Data
	public static class DestinationStore {

		private Destination dashboardDestination;
		private List<Destination> attributeSummaryDestinations = new ArrayList<>();
		private List<Destination> testDestinations = new ArrayList<>();
		private List<Destination> attributeDetailDestinations = new ArrayList<>();

		public void addAttributeSummaryDestination(Destination destination) {
			attributeSummaryDestinations.add(destination);
		}
		
		public void addTestDestination(Destination destination) {
			testDestinations.add(destination);
		}
		
		public void addAttributeDetailDestination(Destination destination) {
			attributeDetailDestinations.add(destination);
		}
	}
}
