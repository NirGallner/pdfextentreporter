package tech.grasshopper.reporter.tests;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Author;
import com.aventstack.extentreports.model.Category;
import com.aventstack.extentreports.model.Device;
import com.aventstack.extentreports.model.Test;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.destination.Destination;
import tech.grasshopper.reporter.destination.DestinationAware;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;
import tech.grasshopper.reporter.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestBasicDetailsDisplay extends Display implements DestinationAware {

	private static final int NAME_FONT_SIZE = 15;
	private static final int TIMES_FONT_SIZE = 12;
	private static final int ATTRIBUTE_FONT_SIZE = 12;

	private static final float PADDING = 5f;
	private static final float WIDTH = 500f;
	private static final float NAME_HEIGHT = 20f;
	private static final float TIMES_HEIGHT = 20f;
	private static final float ATTRIBUTE_HEIGHT = 20f;
	private static final float GAP_HEIGHT = 5f;

	private static final float BORDER_WIDTH = 0;

	private static final float MULTI_LINE_SPACING = 1f;

	protected Test test;

	@Default
	private int numberOfRowsToRepeat = 2;

	private TableBuilder tableBuilder;

	private int destinationY;

	@Override
	public void display() {

		xlocation += test.getLevel() * TestDetails.LEVEL_X_INDENT;

		createTableBuilder();
		createNameRow();
		createDurationRow();
		createAttributesRow();
		drawTable();
	}

	private void createTableBuilder() {
		tableBuilder = Table.builder().addColumnsOfWidth(WIDTH).padding(PADDING).borderWidth(BORDER_WIDTH)
				.font(ReportFont.BOLD_ITALIC_FONT).horizontalAlignment(HorizontalAlignment.LEFT)
				.verticalAlignment(VerticalAlignment.MIDDLE);
	}

	private void createNameRow() {
		tableBuilder.addRow(Row.builder().add(TextCell.builder().minHeight(NAME_HEIGHT).fontSize(NAME_FONT_SIZE)
				.text(test.getName()).wordBreak(true).lineSpacing(MULTI_LINE_SPACING).textColor(Color.RED).build())
				.build());
	}

	private void createDurationRow() {
		tableBuilder.addRow(Row.builder().add(TextCell.builder().minHeight(TIMES_HEIGHT).fontSize(TIMES_FONT_SIZE)
				.text(testDuration()).textColor(Color.BLUE).build()).build());
	}

	private String testDuration() {
		LocalDateTime start = DateUtil.convertToLocalDateTimeFromDate(test.getStartTime());
		LocalDateTime end = DateUtil.convertToLocalDateTimeFromDate(test.getEndTime());
		String duration = "/ " + DateUtil.formatDateTimeWithMillis(start) + " / "
				+ DateUtil.formatDateTimeWithMillis(end) + " / " + DateUtil.durationValue(start, end) + " /";

		if (test.getLevel() > 0)
			duration = " / " + DateUtil.durationValue(start, end) + " /";
		return duration;
	}

	private void createAttributesRow() {
		String atts = "";

		List<String> attributes = new ArrayList<>();
		attributes.addAll(test.getAuthorSet().stream().map(Author::getName).collect(Collectors.toList()));
		attributes.addAll(test.getCategorySet().stream().map(Category::getName).collect(Collectors.toList()));
		attributes.addAll(test.getDeviceSet().stream().map(Device::getName).collect(Collectors.toList()));

		if (!attributes.isEmpty()) {
			numberOfRowsToRepeat = 3;
			atts = attributes.stream().collect(Collectors.joining(" / ", "/ ", " /"));

			tableBuilder.addRow(Row.builder().fontSize(ATTRIBUTE_FONT_SIZE)
					.add(TextCell.builder().text(atts).minHeight(ATTRIBUTE_HEIGHT).wordBreak(true)
							.lineSpacing(MULTI_LINE_SPACING).textColor(Color.GRAY).build())
					.build());
		}
	}

	private void drawTable() {

		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);
		destinationY = (int) ylocation;

		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).repeatRows(numberOfRowsToRepeat).build();
		table.displayTable();

		ylocation = table.getFinalY() - GAP_HEIGHT;
		page = table.getTableStartPage();

		if (!initialPage.equals(page))
			destinationY = (int) Display.CONTENT_START_Y;
	}

	@Override
	public Destination createDestination() {
		return Destination.builder().name(test.getName()).yCoord(destinationY).page(page).build();
	}
}
