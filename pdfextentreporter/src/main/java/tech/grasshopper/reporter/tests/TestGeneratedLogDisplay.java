package tech.grasshopper.reporter.tests;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;
import tech.grasshopper.reporter.tests.markup.TestMarkup;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestGeneratedLogDisplay extends Display {

	public static final float LOGS_HEADER_HEIGHT = 20f;
	public static final float LOGS_ROW_HEIGHT = 20f;

	private static final int LOGS_HEADER_FONT_SIZE = 13;
	private static final float PADDING = 5f;
	private static final float LOGS_STATUS_WIDTH = 55f;
	private static final float LOGS_DETAILS_WIDTH = 445f;

	private static final float BORDER_WIDTH = 1f;
	private static final float GAP_HEIGHT = 10f;
	private static final int LOGS_TABLE_CONTENT_FONT_SIZE = 11;
	private static final PDFont LOGS_TABLE_CONTENT_FONT = ReportFont.REGULAR_FONT;
	private static final float LOGS_TABLE_CONTENT_MULTILINE_SPACING = 1f;

	protected Test test;

	private TableBuilder tableBuilder;

	@Override
	public void display() {
		if (!test.getGeneratedLog().isEmpty()) {
			xlocation += test.getLevel() * TestDetails.LEVEL_X_INDENT;

			createTableBuilder();
			createHeaderRow();
			createLogRows();
			drawTable();
		}
	}

	private void createTableBuilder() {
		tableBuilder = Table.builder()
				.addColumnsOfWidth(LOGS_STATUS_WIDTH,
						LOGS_DETAILS_WIDTH - (test.getLevel() * TestDetails.LEVEL_X_INDENT))
				.padding(PADDING).borderColor(Color.LIGHT_GRAY).borderWidth(BORDER_WIDTH)
				.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.TOP);
	}

	private void createHeaderRow() {
		tableBuilder.addRow(Row.builder().height(LOGS_HEADER_HEIGHT).font(ReportFont.BOLD_ITALIC_FONT)
				.fontSize(LOGS_HEADER_FONT_SIZE).add(TextCell.builder().text("Status").build())
				.add(TextCell.builder().text("Details").build()).build());
	}

	private void createLogRows() {
		test.getGeneratedLog().forEach(l -> {
			AbstractCell detailCell = TestMarkup.builder().log(l).width(LOGS_DETAILS_WIDTH - (2 * PADDING)).build()
					.createMarkupCell();

			Row row = Row.builder().font(LOGS_TABLE_CONTENT_FONT).fontSize(LOGS_TABLE_CONTENT_FONT_SIZE).wordBreak(true)
					.padding(PADDING).add(TextCell.builder().text(l.getStatus().toString()).build()).add(detailCell)
					.build();
			tableBuilder.addRow(row);
		});
	}

	private void drawTable() {
		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).repeatRows(1).build();
		table.displayTable();

		ylocation = table.getFinalY() - GAP_HEIGHT;
	}
}
