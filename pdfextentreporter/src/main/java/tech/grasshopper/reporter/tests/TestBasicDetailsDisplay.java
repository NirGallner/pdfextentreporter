package tech.grasshopper.reporter.tests;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import com.aventstack.extentreports.model.NamedAttribute;
import com.aventstack.extentreports.model.Test;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.context.AttributeType;
import tech.grasshopper.reporter.destination.Destination;
import tech.grasshopper.reporter.destination.DestinationAware;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;
import tech.grasshopper.reporter.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestBasicDetailsDisplay extends Display implements DestinationAware {

	private static final PDFont CONTENT_FONT = ReportFont.BOLD_ITALIC_FONT;

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

	protected final TextSanitizer textSanitizer = TextSanitizer.builder().font(CONTENT_FONT).build();

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
		tableBuilder.addRow(Row.builder()
				.add(TextCell.builder().minHeight(NAME_HEIGHT).fontSize(NAME_FONT_SIZE)
						.text(textSanitizer.sanitizeText(test.getName())).wordBreak(true)
						.lineSpacing(MULTI_LINE_SPACING).textColor(config.getTestNameColor()).build())
				.build());
	}

	private void createDurationRow() {
		tableBuilder.addRow(Row.builder().add(TextCell.builder().minHeight(TIMES_HEIGHT).fontSize(TIMES_FONT_SIZE)
				.text(testDuration()).textColor(config.getTestTimesColor()).build()).build());
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
		if (test.getCategorySet().isEmpty() && test.getAuthorSet().isEmpty() && test.getDeviceSet().isEmpty())
			return;

		ParagraphBuilder paraBuilder = Paragraph.builder();
		creaateAttributeText(test.getCategorySet(), AttributeType.CATEGORY, paraBuilder);
		creaateAttributeText(test.getAuthorSet(), AttributeType.AUTHOR, paraBuilder);
		creaateAttributeText(test.getDeviceSet(), AttributeType.DEVICE, paraBuilder);

		tableBuilder.addRow(
				Row.builder().fontSize(ATTRIBUTE_FONT_SIZE).add(ParagraphCell.builder().paragraph(paraBuilder.build())
						.minHeight(ATTRIBUTE_HEIGHT).lineSpacing(MULTI_LINE_SPACING).build()).build());
	}

	private void creaateAttributeText(Set<? extends NamedAttribute> attributes, AttributeType type,
			ParagraphBuilder paraBuilder) {
		if (!attributes.isEmpty()) {
			numberOfRowsToRepeat = 3;
			String atts = attributes.stream().map(a -> textSanitizer.sanitizeText(a.getName()))
					.collect(Collectors.joining(" / ", "/ ", " /"));
			paraBuilder.append(StyledText.builder().text(atts).color(config.attributeHeaderColor(type)).build());
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
