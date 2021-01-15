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
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.tablecell.TableWithinTableCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TableMarkup extends MarkupDisplay {

	private float width;

	private int maxTableColumnCount;

	private int maxTableRowCount;

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

		boolean maxRows = rows.size() > maxTableRowCount ? true : false;
		int rowCnt = rows.size() > maxTableRowCount ? maxTableRowCount : rows.size();

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(columnWidths).fontSize(LOG_FONT_SIZE)
				.font(LOG_FONT).borderWidth(BORDER_WIDTH).borderColor(Color.LIGHT_GRAY).wordBreak(true);

		int i = 1;
		for (Element row : rows) {
			if (i > rowCnt)
				break;
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
			i++;
		}

		if (maxCols || maxRows) {
			tableBuilder.addRow(Row.builder().add(TextCell.builder().colSpan(cols).text("Only first "
					+ maxTableColumnCount + " columns and " + maxTableRowCount
					+ " rows are shown. Change this from the 'maxTableColumnCount' and maxTableRowCount'' settings.")
					.minHeight(15f).font(ReportFont.REGULAR_FONT).fontSize(10).textColor(Color.RED).wordBreak(true)
					.lineSpacing(MULTILINE_SPACING).build()).build());
		}

		return tableBuilder.build();
	}
}
