package tech.grasshopper.reporter.tests.markup;

import org.jsoup.nodes.Element;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.structure.cell.TableWithinTableCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class OrderedListMarkup extends MarkupDisplay {

	private float width;

	private static final float SNO_COLUMN_WIDTH = 25f;

	@Override
	public AbstractCell displayDetails() {

		return TableWithinTableCell.builder().table(listTable()).build();
	}

	private Table listTable() {

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(SNO_COLUMN_WIDTH, width - SNO_COLUMN_WIDTH)
				.fontSize(LOG_FONT_SIZE).font(LOG_FONT).borderWidth(0).wordBreak(true);

		int sno = 1;
		for (Element e : elements) {
			tableBuilder.addRow(Row.builder().add(TextCell.builder().text(String.valueOf(sno)).build())
					.add(TextCell.builder().text(textSanitizer.sanitizeText(e.text())).textColor(textColor)
							.lineSpacing(MULTILINE_SPACING).build())
					.build());
			sno++;
		}

		return tableBuilder.build();
	}
}
