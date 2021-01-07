package tech.grasshopper.reporter;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.TIMES_ROMAN;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import tech.grasshopper.reporter.structure.PageCreator;
import tech.grasshopper.reporter.tablecell.TableWithinTableCell;

public class InternalTable {

	public static void main(String[] args) throws IOException {

		try (final PDDocument document = new PDDocument()) {

			final PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

				TableDrawer.builder().contentStream(contentStream).table(createSimpleTable()).startX(50)
						.startY(page.getMediaBox().getHeight() - 50).build().draw();

				TableDrawer tableDrawer = RepeatedHeaderTableDrawer.builder().table(createSimpleTable()).startX(50)
						.startY(page.getMediaBox().getHeight() - 200).endY(50).build();

				tableDrawer.draw(() -> document, PageCreator.potraitPageSupplier(), 67);
			}

			document.save("table-within-table.pdf");
		}
	}

	private static Table createSimpleTable() {
		return Table.builder().addColumnsOfWidth(40, 40, 100, 40).fontSize(8).font(HELVETICA)
				.addRow(Row.builder().add(TextCell.builder().borderWidth(1).text("23").build())
						.add(TextCell.builder().borderWidth(1).text("65").build())
						.add(TextCell.builder().borderWidth(1).text("12").build())
						.add(TextCell.builder().borderWidth(1).text("99").build()).build())
				.addRow(Row.builder().add(TextCell.builder().borderWidth(1).text("A").build())
						.add(TextCell.builder().borderWidth(1).text("C").build())
						.add(TableWithinTableCell.builder().borderWidth(1).table(createInnerTable()).build())
						.add(TextCell.builder().borderWidth(1).text("B").build()).build())
				.build();
	}

	private static Table createInnerTable() {
		return Table.builder().addColumnsOfWidth(20, 20, 20, 20).fontSize(6).font(TIMES_ROMAN)
				.addRow(Row.builder().add(TextCell.builder().borderWidth(1).text("1").build())
						.add(TextCell.builder().borderWidth(1).text("3").build())
						.add(TextCell.builder().borderWidth(1).text("1").build())
						.add(TextCell.builder().borderWidth(1).text("2").build()).build())
				.addRow(Row.builder().add(TextCell.builder().borderWidth(1).text("4").build())
						.add(TextCell.builder().borderWidth(1).text("7").build())
						.add(TextCell.builder().borderWidth(1).text("1").build())
						.add(TextCell.builder().borderWidth(1).text("1").build()).build())
				.build();
	}
}
