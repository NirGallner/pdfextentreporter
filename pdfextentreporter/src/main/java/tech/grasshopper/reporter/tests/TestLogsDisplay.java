package tech.grasshopper.reporter.tests;

import java.awt.Color;
import java.util.List;

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
import tech.grasshopper.pdf.structure.cell.TableWithinTableCell;
import tech.grasshopper.pdf.structure.cell.TextLabelCell;
import tech.grasshopper.reporter.annotation.AnnotationStore;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;
import tech.grasshopper.reporter.util.DateUtil;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestLogsDisplay extends Display implements TestIndent {

	private static final float LOGS_HEADER_HEIGHT = 20f;

	private static final int LOGS_HEADER_FONT_SIZE = 11;
	private static final float PADDING = 5f;
	private static final float LOGS_STATUS_WIDTH = 50f;
	private static final float LOGS_TIMESTAMP_WIDTH = 70f;
	private static final float LOGS_DETAILS_WIDTH = 380f;

	private static final float BORDER_WIDTH = 1f;
	private static final int LOGS_TABLE_CONTENT_FONT_SIZE = 10;
	private static final PDFont LOGS_TABLE_CONTENT_FONT = ReportFont.REGULAR_FONT;

	private static final float GAP_HEIGHT = 15f;

	protected Test test;

	protected TableBuilder tableBuilder;

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
			AbstractCell detailsCell = createLogDisplayCell(l, test);

			Row row = Row.builder().padding(PADDING).font(LOGS_TABLE_CONTENT_FONT)
					.fontSize(LOGS_TABLE_CONTENT_FONT_SIZE)
					.add(TextLabelCell.builder().text(l.getStatus().toString())
							.labelColor(config.statusColor(l.getStatus())).build())
					.add(TextCell.builder()
							.text(DateUtil.formatTimeAMPM(DateUtil.convertToLocalDateTimeFromDate(l.getTimestamp())))
							.textColor(config.getTestTimeStampColor()).build())
					.add(detailsCell).build();

			tableBuilder.addRow(row);
		});
	}

	private AbstractCell createLogDisplayCell(Log log, Test test) {
		LogDetailsCollector logDetailsCollector = LogDetailsCollector.builder().annotations(annotations).config(config)
				.document(document).test(test)
				.width(LOGS_DETAILS_WIDTH - (test.getLevel() * TestDetails.LEVEL_X_INDENT)).build();

		List<AbstractCell> allDetailCells = logDetailsCollector.createLogDetailCells(log);

		if (allDetailCells.isEmpty())
			return TextCell.builder().text("").padding(PADDING).build();
		else if (allDetailCells.size() == 1)
			return allDetailCells.get(0);
		else
			return createMultipleDetailsLogCell(allDetailCells);
	}

	private AbstractCell createMultipleDetailsLogCell(List<AbstractCell> allDetailCells) {
		TableBuilder multipleDetailsBuilder = Table.builder()
				.addColumnsOfWidth(LOGS_DETAILS_WIDTH - (test.getLevel() * TestDetails.LEVEL_X_INDENT)).borderWidth(0f);

		allDetailCells.forEach(c -> {
			if (c != null)
				multipleDetailsBuilder.addRow(Row.builder().add(c).padding(PADDING).build());
		});

		return TableWithinTableCell.builder().table(multipleDetailsBuilder.build()).padding(0f).build();
	}

	private void drawTable() {
		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).splitRow(true).build();
		table.displayTable();

		ylocation = table.getFinalY() - GAP_HEIGHT;
	}
}
