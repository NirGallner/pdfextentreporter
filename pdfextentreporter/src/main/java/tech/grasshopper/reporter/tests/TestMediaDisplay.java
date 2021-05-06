package tech.grasshopper.reporter.tests;

import java.awt.Color;
import java.util.List;

import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.structure.cell.TextLinkCell;
import tech.grasshopper.reporter.annotation.AnnotationStore;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestMediaDisplay extends Display implements TestIndent {

	private static final float MEDIA_WIDTH = 70f;
	private static final float MEDIA_HEIGHT = 70f;
	private static final float PADDING = 2f;
	private static final float GAP_HEIGHT = 10f;
	private static final float MEDIA_MAX_MSG_WIDTH = 55f;

	private static final float BORDER_WIDTH = 1f;

	protected Test test;

	private TableBuilder tableBuilder;
	
	private AnnotationStore annotations;

	@Override
	public void display() {
		if (test.hasScreenCapture()) {

			xlocation += calculateIndent(test.getLevel(), config.getTestMaxIndentLevel()) * TestDetails.LEVEL_X_INDENT;

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
		boolean maxMedia = false;
		RowBuilder rowBuilder = Row.builder();
		List<Media> medias = test.getMedia();
		int mediaCount = 6 - calculateIndent(test.getLevel(), config.getTestMaxIndentLevel());
		float plusWidth = 15f;

		if (medias.size() > mediaCount) {
			medias = medias.subList(0, mediaCount);
			maxMedia = true;
		}

		if (config.isDisplayExpandedMedia()) {
			tableBuilder.addColumnsOfWidth(plusWidth);
			Annotation annotation = Annotation.builder().id(test.getId()).build();
			annotations.addTestMediaAnnotation(annotation);
			
			rowBuilder.add(TextLinkCell.builder().text("+").annotation(annotation).font(ReportFont.REGULAR_FONT)
					.fontSize(15).textColor(Color.RED).showLine(false).padding(2f).borderWidth(0f)
					.verticalAlignment(VerticalAlignment.TOP).build());
		}

		for (Media media : medias) {
			tableBuilder.addColumnsOfWidth(MEDIA_WIDTH);
			rowBuilder.add(TestMedia.builder().media(media).document(document).width(MEDIA_WIDTH).height(MEDIA_HEIGHT)
					.padding(PADDING).build().createImageCell());
		}

		if (maxMedia) {
			tableBuilder.addColumnsOfWidth(MEDIA_MAX_MSG_WIDTH);
			rowBuilder.add(TextCell.builder().text("Only first " + mediaCount + " medias are shown.")
					.font(ReportFont.REGULAR_FONT).fontSize(10).textColor(Color.RED)
					.verticalAlignment(VerticalAlignment.TOP).wordBreak(true).build());
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
