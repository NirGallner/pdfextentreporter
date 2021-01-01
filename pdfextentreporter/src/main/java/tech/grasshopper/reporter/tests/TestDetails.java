package tech.grasshopper.reporter.tests;

import java.util.ArrayList;
import java.util.List;

import com.aventstack.extentreports.model.Test;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.Section;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestDetails extends Section {

	public static final int LEVEL_X_INDENT = 20;

	@Default
	private float yLocation = Display.CONTENT_START_Y;

	@Override
	public void createSection() {

		createPage();

		List<Test> allTests = new ArrayList<>();
		report.getTestList().forEach(t -> {
			allTests.add(t);
			collectTestNodes(t, allTests);
		});

		for (Test test : allTests) {

			TestBasicDetailsDisplay testBasicDetailsDisplay = TestBasicDetailsDisplay.builder().document(document)
					.test(test).ylocation(yLocation).build();
			testBasicDetailsDisplay.display();
			createTestDestination(testBasicDetailsDisplay);
			yLocation = testBasicDetailsDisplay.getYlocation();

			if (test.hasScreenCapture()) {
				TestMediaDisplay testMediaDisplay = TestMediaDisplay.builder().document(document).test(test)
						.ylocation(yLocation).build();
				testMediaDisplay.display();

				yLocation = testMediaDisplay.getYlocation();
			}

			if (test.hasLog()) {
				TestLogsDisplay testLogsDisplay = TestLogsDisplay.builder().document(document).test(test)
						.ylocation(yLocation).build();
				testLogsDisplay.display();

				yLocation = testLogsDisplay.getYlocation();
			}
		}
	}

	private void collectTestNodes(Test test, List<Test> tests) {
		test.getChildren().forEach(t -> {
			tests.add(t);
			collectTestNodes(t, tests);
		});
	}

	private void createTestDestination(TestBasicDetailsDisplay testDisplay) {
		destinations.addTestDestination(testDisplay.createDestination());
	}
}
