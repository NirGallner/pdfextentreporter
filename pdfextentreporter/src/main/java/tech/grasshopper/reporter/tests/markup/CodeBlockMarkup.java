package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.Arrays;

import org.jsoup.nodes.Element;
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
public class CodeBlockMarkup extends MarkupDisplay {

	private float width;

	@Override
	public AbstractCell displayDetails() {

		return TableWithinTableCell.builder().table(codeTable()).build();
	}

	private Table codeTable() {

		int cols = elements.size();
		float colWidth = width / cols;

		float[] columnWidths = new float[cols];
		Arrays.fill(columnWidths, colWidth);

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(columnWidths).fontSize(LOG_FONT_SIZE)
				.font(LOG_FONT).borderWidth(BORDER_WIDTH).borderColor(Color.LIGHT_GRAY).wordBreak(true);

		RowBuilder rowBuilder = Row.builder();
		for (Element e : elements)
			rowBuilder.add(TextCell.builder().text(e.text()).lineSpacing(MULTILINE_SPACING).build());

		tableBuilder.addRow(rowBuilder.build());

		return tableBuilder.build();
	}
}
