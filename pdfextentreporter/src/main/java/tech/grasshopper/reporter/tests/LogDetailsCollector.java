package tech.grasshopper.reporter.tests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.pdf.annotation.Annotation;
import tech.grasshopper.pdf.structure.cell.TableWithinTableCell;
import tech.grasshopper.pdf.structure.cell.TextLinkCell;
import tech.grasshopper.reporter.annotation.AnnotationStore;
import tech.grasshopper.reporter.config.ExtentPDFReporterConfig;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;
import tech.grasshopper.reporter.tests.markup.TestMarkup;

@Data
@Builder
public class LogDetailsCollector {

	private PDDocument document;

	private Test test;

	private ExtentPDFReporterConfig config;

	private AnnotationStore annotations;

	private float width;

	@Default
	private boolean bddReport = false;

	private static final float PADDING = 5f;
	private static final float LOGS_MEDIA_HEIGHT = 100f;
	private static final float LOGS_MEDIA_WIDTH = 100f;
	private static final int LOGS_TABLE_CONTENT_FONT_SIZE = 10;
	private static final PDFont LOGS_TABLE_CONTENT_FONT = ReportFont.REGULAR_FONT;
	private static final int LOGS_STACK_TRACE_TABLE_CONTENT_FONT_SIZE = 11;
	private static final PDFont LOGS_STACK_TRACE_TABLE_CONTENT_FONT = ReportFont.BOLD_FONT;

	private static final float LOGS_DETAILS_HEIGHT = 15f;
	private static final float LOGS_MEDIA_PLUS_WIDTH = 15f;

	public List<AbstractCell> createLogDetailCells(Log log) {
		List<AbstractCell> allDetailCells = new ArrayList<>();

		if (!log.getDetails().isEmpty())
			allDetailCells.add(createDetailsMarkupCell(log));

		if (log.hasException())
			allDetailCells.add(createExceptionCell(log));

		if (log.hasMedia())
			allDetailCells.add(createMediaCell(log));

		return allDetailCells;
	}

	private AbstractCell createDetailsMarkupCell(Log log) {
		TextSanitizer textSanitizer = TextSanitizer.builder().font(LOGS_TABLE_CONTENT_FONT).build();

		Status status = bddReport ? test.getStatus() : log.getStatus();
		AbstractCell detailMarkupCell = TextCell.builder().text(textSanitizer.sanitizeText(log.getDetails()))
				.font(LOGS_TABLE_CONTENT_FONT).fontSize(LOGS_TABLE_CONTENT_FONT_SIZE)
				.textColor(config.statusColor(status)).build();

		if (TestMarkup.isMarkup(log.getDetails()))
			detailMarkupCell = TestMarkup.builder().log(log).test(test).bddReport(bddReport)
					.width(width - (2 * PADDING)).config(config).build().createMarkupCell();
		return detailMarkupCell;
	}

	private AbstractCell createMediaCell(Log log) {
		if (config.isDisplayExpandedMedia()) {
			TableBuilder tableBuilder = Table.builder()
					.addColumnsOfWidth(LOGS_MEDIA_PLUS_WIDTH, width - LOGS_MEDIA_PLUS_WIDTH).padding(0f);

			Annotation annotation = Annotation.builder().id(test.getId()).build();
			annotations.addTestMediaAnnotation(annotation);

			tableBuilder.addRow(Row.builder()
					.add(TextLinkCell.builder().text("+").annotation(annotation).font(ReportFont.REGULAR_FONT)
							.fontSize(15).textColor(Color.RED).showLine(false).verticalAlignment(VerticalAlignment.TOP)
							.horizontalAlignment(HorizontalAlignment.CENTER).build())
					.add(TestMedia.builder().media(log.getMedia()).document(document)
							.width(width - LOGS_MEDIA_PLUS_WIDTH).height(LOGS_MEDIA_HEIGHT)
							.locations(config.getMediaFolders()).build().createImageCell())
					.build());

			return TableWithinTableCell.builder().table(tableBuilder.build()).width(width - LOGS_MEDIA_PLUS_WIDTH)
					.build();
		} else
			return TestMedia.builder().media(log.getMedia()).document(document).width(LOGS_MEDIA_WIDTH)
					.height(LOGS_MEDIA_HEIGHT).padding(PADDING).build().createImageCell();
	}

	private AbstractCell createExceptionCell(Log log) {
		return TestStackTrace.builder().log(log).font(LOGS_STACK_TRACE_TABLE_CONTENT_FONT)
				.color(config.getTestExceptionColor()).width(width - (2 * PADDING)).height(LOGS_DETAILS_HEIGHT)
				.fontSize(LOGS_STACK_TRACE_TABLE_CONTENT_FONT_SIZE).padding(PADDING).build().createStackTraceCell();
	}
}
