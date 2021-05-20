package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.Arrays;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.structure.cell.TableWithinTableCell;
import tech.grasshopper.reporter.font.ReportFont;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TableMarkup extends MarkupDisplay {

	private float width;

	private int maxTableColumnCount;

	@Override
	public AbstractCell displayDetails() {

		return TableWithinTableCell.builder().table(internalTable()).build();
	}

	private Table internalTable() {

		Elements rows = element.select("tr");

		int cols = rows.get(0).select("td").size();
		boolean maxCols = cols > maxTableColumnCount ? true : false;
		cols = cols > maxTableColumnCount ? maxTableColumnCount : cols;

		float colWidth = width / cols;
		float[] columnWidths = new float[cols];
		Arrays.fill(columnWidths, colWidth);

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(columnWidths).fontSize(LOG_FONT_SIZE)
				.font(LOG_FONT).borderWidth(BORDER_WIDTH).borderColor(Color.LIGHT_GRAY).wordBreak(true);

		for (Element row : rows) {
			if (row.children().isEmpty())
				continue;
			RowBuilder rowBuilder = Row.builder();

			int j = 1;
			for (Element cell : row.select("td")) {
				if (j > cols)
					break;
				rowBuilder.add(TextCell.builder().text(textSanitizer.sanitizeText(cell.text())).textColor(textColor)
						.lineSpacing(MULTILINE_SPACING).build());
				j++;
			}
			tableBuilder.addRow(rowBuilder.build());
		}

		String tableCounts = "";
		String tableSettings = "";

		if (maxCols) {
			tableCounts = maxTableColumnCount + " columns";
			tableSettings = "'maxTableColumnCount'";

			tableBuilder.addRow(Row.builder()
					.add(TextCell.builder().colSpan(cols)
							.text("Only first " + tableCounts + " are shown. Change this from the " + tableSettings
									+ " settings.")
							.minHeight(15f).font(ReportFont.REGULAR_FONT).fontSize(10).textColor(Color.RED)
							.wordBreak(true).lineSpacing(MULTILINE_SPACING).build())
					.build());
		}

		return tableBuilder.build();
	}
}
