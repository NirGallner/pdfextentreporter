package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import tech.grasshopper.reporter.optimizer.TextSanitizer;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TableMarkup extends MarkupDisplay {
	// The code is not optimum with regards to exception handling. Needs to be
	// refactored in future.
	private static final Logger logger = Logger.getLogger(TableMarkup.class.getName());

	private float width;

	private int maxTableColumnCount;

	@Override
	public AbstractCell displayDetails() {
		Elements rows = null;
		try {
			rows = element.select("tr");
			if (rows.isEmpty()) {
				logger.log(Level.SEVERE, "No data rows available.");
				return errorDisplay("Table data is not available.");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to retrieve data rows.");
			return errorDisplay("Unable to access data.");
		}

		int cols = 0;
		try {
			for (Element row : rows) {
				cols = row.select("td").size();
				if (cols > 0)
					break;
			}
			if (cols == 0) {
				logger.log(Level.SEVERE, "No data columns available.");
				return errorDisplay("Table data is not available.");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to retrieve data columns.");
			return errorDisplay("Unable to access data.");
		}
		return TableWithinTableCell.builder().table(internalTable(rows, cols)).build();
	}

	private Table internalTable(Elements rows, int columnCount) {

		int displayColumnCount = columnCount > maxTableColumnCount ? maxTableColumnCount : columnCount;

		float colWidth = width / displayColumnCount;
		float[] columnWidths = new float[displayColumnCount];
		Arrays.fill(columnWidths, colWidth);

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(columnWidths).fontSize(LOG_FONT_SIZE)
				.font(logFont).borderWidth(BORDER_WIDTH).borderColor(Color.LIGHT_GRAY).wordBreak(true);

		for (Element row : rows) {
			if (row.children().isEmpty())
				continue;

			Elements cells = row.select("td");
			if (cells.isEmpty()) {
				logger.log(Level.WARNING, "Row does not contain any cell data.");
				continue;
			}

			RowBuilder rowBuilder = Row.builder();
			TextSanitizer textSanitizer = TextSanitizer.builder().font(logFont).build();
			int j = 1;
			
			for (Element cell : cells) {
				if (j > displayColumnCount)
					break;

				String text = "";
				try {
					text = cell.text();
				} catch (Exception e) {
					text = "Error!";
					logger.log(Level.SEVERE, "Unable to get text for cell, default to error message.");
				}

				rowBuilder.add(TextCell.builder().text(textSanitizer.sanitizeText(text)).textColor(textColor)
						.lineSpacing(MULTILINE_SPACING).build());
				j++;
			}
			tableBuilder.addRow(rowBuilder.build());
		}

		String tableCounts = "";
		String tableSettings = "";

		if (columnCount > maxTableColumnCount) {
			tableCounts = maxTableColumnCount + " columns";
			tableSettings = "'maxTableColumnCount'";

			logger.log(Level.WARNING,
					"Only first " + tableCounts + " are shown. Modify the " + tableSettings + " settings.");

			tableBuilder.addRow(Row.builder().add(TextCell.builder().colSpan(displayColumnCount)
					.text("Only first " + tableCounts + " are shown. Modify the " + tableSettings + " settings.")
					.minHeight(15f).font(logFont).fontSize(10).textColor(Color.RED).wordBreak(true)
					.lineSpacing(MULTILINE_SPACING).build()).build());
		}
		return tableBuilder.build();
	}
}
