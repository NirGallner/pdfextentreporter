package tech.grasshopper.reporter.expanded;

import java.util.ArrayList;
import java.util.List;

import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.header.PageHeader;
import tech.grasshopper.reporter.header.PageHeaderAware;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class MediaSummary extends Section implements PageHeaderAware {

	@Default
	private float yLocation = Display.CONTENT_START_Y;

	@Override
	public void createSection() {

		List<Test> allTests = new ArrayList<>();
		report.getTestList().forEach(t -> {
			allTests.add(t);
			collectTestNodes(t, allTests);
		});

		if (!checkDataValidity(allTests))
			return;

		pageHeaderDetails();
		createPage();

		for (Test test : allTests) {
			boolean containsMedia = false;

			if (!test.getMedia().isEmpty())
				containsMedia = true;

			if (!containsMedia) {
				for (Log log : test.getLogs()) {
					if (log.hasMedia())
						containsMedia = true;
				}
			}

			if (containsMedia) {
				ExpandedMediaDisplay expandedMediaDisplay = ExpandedMediaDisplay.builder().document(document)
						.config(config).test(test).ylocation(yLocation).build();
				expandedMediaDisplay.display();
				createMediaDestination(expandedMediaDisplay);
				yLocation = expandedMediaDisplay.getYlocation();
			}
		}
	}

	private void createMediaDestination(ExpandedMediaDisplay expandedMediaDisplay) {
		destinations.addTestMediaDestination(expandedMediaDisplay.createDestination());
	}

	@Override
	public String getSectionTitle() {
		return PageHeader.EXPANDED_MEDIA_SECTION;
	}

	private boolean checkDataValidity(List<Test> allTests) {

		for (Test test : allTests) {
			if (!test.getMedia().isEmpty())
				return true;

			for (Log log : test.getLogs()) {
				if (log.hasMedia())
					return true;
			}
		}
		return false;
	}

	private void collectTestNodes(Test test, List<Test> tests) {
		test.getChildren().forEach(t -> {
			tests.add(t);
			collectTestNodes(t, tests);
		});
	}
}
