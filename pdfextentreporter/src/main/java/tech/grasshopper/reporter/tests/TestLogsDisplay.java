package tech.grasshopper.reporter.tests;

import java.awt.Color;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.structure.cell.TableWithinTableCell;
import tech.grasshopper.pdf.structure.cell.TextLabelCell;
import tech.grasshopper.pdf.structure.cell.TextLinkCell;
import tech.grasshopper.reporter.annotation.AnnotationStore;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;
import tech.grasshopper.reporter.tests.markup.TestMarkup;
import tech.grasshopper.reporter.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestLogsDisplay extends Display implements TestIndent {

	public static final float LOGS_HEADER_HEIGHT = 20f;
	public static final float LOGS_ROW_HEIGHT = 20f;
	public static final float LOGS_MEDIA_HEIGHT = 100f;
	public static final float LOGS_MEDIA_WIDTH = 100f;

	private static final int LOGS_HEADER_FONT_SIZE = 11;
	private static final float PADDING = 5f;
	private static final float LOGS_STATUS_WIDTH = 50f;
	private static final float LOGS_TIMESTAMP_WIDTH = 70f;
	private static final float LOGS_DETAILS_WIDTH = 380f;
	private static final float LOGS_DETAILS_HEIGHT = 15f;
	private static final float LOGS_MEDIA_PLUS_WIDTH = 15f;

	private static final float BORDER_WIDTH = 1f;
	private static final int LOGS_TABLE_CONTENT_FONT_SIZE = 10;
	private static final PDFont LOGS_TABLE_CONTENT_FONT = ReportFont.REGULAR_FONT;
	private static final int LOGS_STACK_TRACE_TABLE_CONTENT_FONT_SIZE = 11;
	private static final PDFont LOGS_STACK_TRACE_TABLE_CONTENT_FONT = ReportFont.BOLD_FONT;
	private static final float LOGS_TABLE_CONTENT_MULTILINE_SPACING = 1f;

	private static final float GAP_HEIGHT = 15f;

	protected Test test;

	private TableBuilder tableBuilder;

	protected final TextSanitizer textSanitizer = TextSanitizer.builder().font(LOGS_TABLE_CONTENT_FONT).build();

	private AnnotationStore annotations;

	@Override
	public void display() {

		if (test.hasLog()) {

			xlocation += calculateIndent(test.getLevel(), config.getTestMaxIndentLevel()) * TestDetails.LEVEL_X_INDENT;

			createTableBuilder();
			createHeaderRow();
			createLogRows();
			drawTable();
		}
	}

	private void createTableBuilder() {
		tableBuilder = Table.builder()
				.addColumnsOfWidth(LOGS_STATUS_WIDTH, LOGS_TIMESTAMP_WIDTH,
						LOGS_DETAILS_WIDTH - (test.getLevel() * TestDetails.LEVEL_X_INDENT))
				.padding(PADDING).borderColor(Color.LIGHT_GRAY).borderWidth(BORDER_WIDTH)
				.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.TOP);
	}

	private void createHeaderRow() {
		tableBuilder.addRow(Row.builder().height(LOGS_HEADER_HEIGHT).font(ReportFont.ITALIC_FONT)
				.fontSize(LOGS_HEADER_FONT_SIZE).add(TextCell.builder().text("Status").build())
				.add(TextCell.builder().text("Timestamp").build()).add(TextCell.builder().text("Log Details").build())
				.build());
	}

	private void createLogRows() {
		test.getLogs().forEach(l -> {

			boolean multipleLogDetails = logWithMultipleDetails(l);
			AbstractCell detailsCell = null, exceptionCell = null, mediaCell = null;

			detailsCell = createDetailsMarkupCell(l);

			if (l.hasException()) {
				exceptionCell = createExceptionCell(l);
				if (!multipleLogDetails)
					detailsCell = exceptionCell;
			}

			if (l.hasMedia()) {
				mediaCell = createMediaCell(l);
				if (!multipleLogDetails)
					detailsCell = mediaCell;
			}

			if (multipleLogDetails)
				detailsCell = createMultipleDetailsLogCell(l, detailsCell, exceptionCell, mediaCell);

			Row row = Row.builder().font(LOGS_TABLE_CONTENT_FONT).fontSize(LOGS_TABLE_CONTENT_FONT_SIZE).wordBreak(true)
					.padding(PADDING)
					.add(TextLabelCell.builder().text(l.getStatus().toString())
							.labelColor(config.statusColor(l.getStatus())).build())
					.add(TextCell.builder()
							.text(DateUtil.formatTimeAMPM(DateUtil.convertToLocalDateTimeFromDate(l.getTimestamp())))
							.textColor(config.getTestTimeStampColor()).build())
					.add(detailsCell).build();

			tableBuilder.addRow(row);
		});
	}

	private AbstractCell createMultipleDetailsLogCell(Log log, AbstractCell detailsCell, AbstractCell exceptionCell,
			AbstractCell mediaCell) {
		TableBuilder multipleDetailsBuilder = Table.builder()
				.addColumnsOfWidth(LOGS_DETAILS_WIDTH - (test.getLevel() * TestDetails.LEVEL_X_INDENT)).borderWidth(0f);

		if (detailsCell != null)
			multipleDetailsBuilder.addRow(Row.builder().add(detailsCell).build());

		if (exceptionCell != null)
			multipleDetailsBuilder.addRow(Row.builder().add(exceptionCell).build());

		if (mediaCell != null)
			multipleDetailsBuilder.addRow(Row.builder().add(mediaCell).build());

		return TableWithinTableCell.builder().table(multipleDetailsBuilder.build()).paddingTop(2f).paddingBottom(2f)
				.paddingLeft(0f).paddingRight(0f).build();
	}

	private AbstractCell createDetailsMarkupCell(Log log) {
		AbstractCell detailMarkupCell = TextCell.builder().text(textSanitizer.sanitizeText(log.getDetails()))
				.textColor(config.statusColor(log.getStatus())).lineSpacing(LOGS_TABLE_CONTENT_MULTILINE_SPACING)
				.build();

		if (TestMarkup.isMarkup(log.getDetails()))
			detailMarkupCell = TestMarkup.builder().log(log).width(LOGS_DETAILS_WIDTH - (2 * PADDING)).config(config)
					.build().createMarkupCell();
		return detailMarkupCell;
	}

	private AbstractCell createMediaCell(Log log) {
		if (config.isDisplayExpandedMedia()) {
			TableBuilder tableBuilder = Table.builder()
					.addColumnsOfWidth(LOGS_MEDIA_PLUS_WIDTH, LOGS_DETAILS_WIDTH - LOGS_MEDIA_PLUS_WIDTH).padding(0f);

			Annotation annotation = Annotation.builder().id(test.getId()).build();
			annotations.addTestMediaAnnotation(annotation);

			tableBuilder.addRow(Row.builder()
					.add(TextLinkCell.builder().text("+").annotation(annotation).font(ReportFont.REGULAR_FONT)
							.fontSize(15).textColor(Color.RED).showLine(false).verticalAlignment(VerticalAlignment.TOP)
							.horizontalAlignment(HorizontalAlignment.CENTER).build())
					.add(TestMedia.builder().media(log.getMedia()).document(document)
							.width(LOGS_DETAILS_WIDTH - LOGS_MEDIA_PLUS_WIDTH).height(LOGS_MEDIA_HEIGHT).build()
							.createImageCell())
					.build());

			return TableWithinTableCell.builder().table(tableBuilder.build())
					.width(LOGS_DETAILS_WIDTH - LOGS_MEDIA_PLUS_WIDTH).build();
		} else
			return TestMedia.builder().media(log.getMedia()).document(document).width(LOGS_MEDIA_WIDTH)
					.height(LOGS_MEDIA_HEIGHT).padding(PADDING).build().createImageCell();
	}

	private AbstractCell createExceptionCell(Log log) {
		return TestStackTrace.builder().log(log).font(LOGS_STACK_TRACE_TABLE_CONTENT_FONT)
				.color(config.getTestExceptionColor())
				.width(LOGS_DETAILS_WIDTH - (test.getLevel() * TestDetails.LEVEL_X_INDENT) - (2 * PADDING))
				.height(LOGS_DETAILS_HEIGHT).fontSize(LOGS_STACK_TRACE_TABLE_CONTENT_FONT_SIZE).padding(PADDING).build()
				.createStackTraceCell();
	}

	private void drawTable() {
		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).splitRow(true).build();
		table.displayTable();

		ylocation = table.getFinalY() - GAP_HEIGHT;
	}

	private boolean logWithMultipleDetails(Log log) {
		Boolean[] detailPresent = { !log.getDetails().isEmpty(), log.hasException(), log.hasMedia() };

		if (Arrays.stream(detailPresent).filter(d -> d == true).count() > 1)
			return true;
		return false;
	}
}
