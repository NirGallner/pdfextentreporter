package tech.grasshopper.reporter.context.detail;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.NamedAttribute;
import com.aventstack.extentreports.model.context.NamedAttributeContext;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.annotation.Annotation;
import tech.grasshopper.reporter.annotation.Annotation.AnnotationStore;
import tech.grasshopper.reporter.annotation.cell.TextLinkCell;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;
import tech.grasshopper.reporter.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class AttributeTestStatusDetailsDisplay extends Display {

	private static final float HEADER_HEIGHT = 25f;
	private static final float TABLE_GAP_HEIGHT = 15f;

	private static final int HEADER_FONT_SIZE = 13;
	private static final float HEADER_PADDING_WIDTH = 5f;
	private static final float HEADER_STATUS_WIDTH = 55f;
	private static final float HEADER_TIMESTAMP_WIDTH = 85f;
	private static final float HEADER_TESTNAME_WIDTH = 360f;

	private static final float TABLE_BORDER_WIDTH = 1f;
	private static final int TABLE_CONTENT_FONT_SIZE = 11;
	private static final PDFont TABLE_CONTENT_FONT = ReportFont.REGULAR_FONT;
	private static final float TABLE_CONTENT_COLUMN_PADDING = 7f;
	private static final float MULTILINE_SPACING = 1f;

	protected NamedAttributeContext<? extends NamedAttribute> attribute;

	protected TableBuilder tableBuilder;

	protected AnnotationStore annotations;

	protected final TextSanitizer textSanitizer = TextSanitizer.builder().font(TABLE_CONTENT_FONT).build();

	@Override
	public void display() {

		createTestTableBuilder();
		createTestHeaderRow();
		createTestDataRows();
		drawTestDetailsTable();
	}

	private void createTestTableBuilder() {
		tableBuilder = Table.builder()
				.addColumnsOfWidth(HEADER_STATUS_WIDTH, HEADER_TIMESTAMP_WIDTH, HEADER_TESTNAME_WIDTH)
				.padding(HEADER_PADDING_WIDTH).borderColor(Color.LIGHT_GRAY).borderWidth(TABLE_BORDER_WIDTH)
				.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.TOP);
	}

	private void createTestHeaderRow() {
		tableBuilder.addRow(Row.builder().height(HEADER_HEIGHT).font(ReportFont.BOLD_ITALIC_FONT)
				.fontSize(HEADER_FONT_SIZE).add(TextCell.builder().text("Status").build())
				.add(TextCell.builder().text("Timestamp").build()).add(TextCell.builder().text("Test Name").build())
				.build());
	}

	private void createTestDataRows() {
		attribute.getTestList().forEach(t -> {
			Annotation annotation = Annotation.builder().id(t.getId()).build();
			annotations.addTestNameAnnotation(annotation);

			Row row = Row.builder().font(TABLE_CONTENT_FONT).fontSize(TABLE_CONTENT_FONT_SIZE).wordBreak(true)
					.padding(TABLE_CONTENT_COLUMN_PADDING)
					.add(TextCell.builder().text(t.getStatus().toString()).textColor(config.statusColor(t.getStatus()))
							.build())
					.add(TextCell.builder()
							.text(DateUtil.formatTimeAMPM(DateUtil.convertToLocalDateTimeFromDate(t.getStartTime())))
							.textColor(config.getTestTimeStampColor()).build())
					.add(TextLinkCell.builder().annotation(annotation).text(textSanitizer.sanitizeText(t.getName()))
							.textColor(config.statusColor(t.getStatus())).lineSpacing(MULTILINE_SPACING).build())
					.build();

			tableBuilder.addRow(row);
		});
	}

	private void drawTestDetailsTable() {
		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).repeatRows(1).build();
		table.displayTable();

		ylocation = table.getFinalY() - TABLE_GAP_HEIGHT;
	}
}
