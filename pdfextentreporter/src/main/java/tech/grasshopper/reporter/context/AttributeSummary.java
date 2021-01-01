package tech.grasshopper.reporter.context;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class AttributeSummary extends Section {

	@Default
	private float destinationYLocation = Display.CONTENT_START_Y;

	@Override
	public void createSection() {

		createPage();

		ContextAttributeSummary categories = ContextAttributeSummary.builder().document(document)
				.type(AttributeType.CATEGORY).report(report).ylocation(destinationYLocation).build();
		categories.display();
		createAttributeDestination(categories);

		ContextAttributeSummary authors = ContextAttributeSummary.builder().document(document)
				.type(AttributeType.AUTHOR).report(report).ylocation(categories.getYlocation()).build();
		authors.display();
		createAttributeDestination(authors);

		ContextAttributeSummary devices = ContextAttributeSummary.builder().document(document)
				.type(AttributeType.DEVICE).report(report).ylocation(authors.getYlocation()).build();
		devices.display();
		createAttributeDestination(devices);

		SystemAttributeSummary systems = SystemAttributeSummary.builder().document(document).type(AttributeType.SYSTEM)
				.report(report).ylocation(devices.getYlocation()).build();
		systems.display();
		createAttributeDestination(systems);
	}

	private void createAttributeDestination(AttributeSummaryDisplay summaryDisplay) {
		destinations.addAttributeSummaryDestination(summaryDisplay.createDestination());
	}
}
