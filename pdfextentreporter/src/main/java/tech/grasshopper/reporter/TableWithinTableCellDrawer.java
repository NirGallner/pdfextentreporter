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
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.cell.AbstractCellDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.structure.PageCreator;

public class TableWithinTableCellDrawer {
	@Getter
	@SuperBuilder
	private static class TableWithinTableCell extends AbstractCell {

		private Table table;

		@Override
		public float getMinHeight() {
			return table.getHeight() + getVerticalPadding();
		}

		@Override
		protected Drawer createDefaultDrawer() {
			return new TableWithinTableDrawer(this);
		}

		private class TableWithinTableDrawer extends AbstractCellDrawer<TableWithinTableCell> {

			public TableWithinTableDrawer(TableWithinTableCell tableWithinTableCell) {
				this.cell = tableWithinTableCell;
			}

			@Override
			public void drawContent(DrawingContext drawingContext) {
				TableDrawer.builder().startX(drawingContext.getStartingPoint().x + getPaddingLeft())
						.startY(drawingContext.getStartingPoint().y + cell.getHeight() - getPaddingTop())
						.table(this.cell.getTable()).contentStream(drawingContext.getContentStream()).build().draw();
			}

			@Override
			protected float calculateInnerHeight() {
				return cell.table.getHeight();
			}
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
}
