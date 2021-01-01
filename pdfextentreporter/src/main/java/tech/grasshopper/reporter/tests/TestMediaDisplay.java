package tech.grasshopper.reporter.tests;

import java.awt.Color;

import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestMediaDisplay extends Display {

	public static final float MEDIA_WIDTH = 70f;
	public static final float MEDIA_HEIGHT = 70f;
	public static final float PADDING = 5f;
	private static final float GAP_HEIGHT = 10f;

	private static final float BORDER_WIDTH = 1f;

	protected Test test;

	private TableBuilder tableBuilder;

	@Override
	public void display() {
		if (test.hasScreenCapture()) {

			xlocation += test.getLevel() * TestDetails.LEVEL_X_INDENT;

			createTableBuilder();
			createMediaRow();
			drawTable();
		}
	}

	private void createTableBuilder() {
		tableBuilder = Table.builder().borderColor(Color.LIGHT_GRAY).borderWidth(BORDER_WIDTH)
				.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.MIDDLE);
	}

	private void createMediaRow() {
		RowBuilder rowBuilder = Row.builder();

		for (Media media : test.getMedia()) {
			tableBuilder.addColumnsOfWidth(MEDIA_WIDTH);
			rowBuilder.add(TestMedia.builder().media(media).document(document).width(MEDIA_WIDTH).height(MEDIA_HEIGHT)
					.padding(PADDING).build().createImageCell());
		}
		tableBuilder.addRow(rowBuilder.build());
	}

	private void drawTable() {
		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).repeatRows(1).build();
		table.displayTable();

		ylocation = table.getFinalY() - GAP_HEIGHT;
	}
}
