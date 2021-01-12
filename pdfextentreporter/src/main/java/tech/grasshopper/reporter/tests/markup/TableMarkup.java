package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.Arrays;

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
import tech.grasshopper.reporter.tablecell.TableWithinTableCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TableMarkup extends MarkupDisplay {

	private float width;

	@Override
	public AbstractCell displayDetails() {

		return TableWithinTableCell.builder().table(internalTable()).build();
	}

	private Table internalTable() {

		Elements rows = element.select("tr");

		int cols = rows.get(0).select("td").size();
		float colWidth = width / cols;

		float[] columnWidths = new float[cols];
		Arrays.fill(columnWidths, colWidth);

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(columnWidths).fontSize(LOG_FONT_SIZE)
				.font(LOG_FONT).borderWidth(BORDER_WIDTH).borderColor(Color.LIGHT_GRAY).wordBreak(true);

		rows.forEach(row -> {
			RowBuilder rowBuilder = Row.builder();
			row.select("td").forEach(v -> {
				rowBuilder.add(TextCell.builder().text(textSanitizer.sanitizeText(v.text())).textColor(textColor)
						.lineSpacing(MULTILINE_SPACING).build());
			});
			tableBuilder.addRow(rowBuilder.build());
		});
		return tableBuilder.build();
	}
}
