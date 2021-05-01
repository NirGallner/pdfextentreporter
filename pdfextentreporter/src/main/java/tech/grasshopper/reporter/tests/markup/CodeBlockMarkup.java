package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;

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
import tech.grasshopper.reporter.font.ReportFont;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CodeBlockMarkup extends MarkupDisplay {

	private float width;

	private int maxCodeBlockCount;

	@Override
	public AbstractCell displayDetails() {
		return TableWithinTableCell.builder().table(codeTable()).build();
	}

	private Table codeTable() {
		boolean maxCodes = elements.size() > maxCodeBlockCount ? true : false;
		int count = elements.size() > maxCodeBlockCount ? maxCodeBlockCount : elements.size();

		TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(width).fontSize(LOG_FONT_SIZE).font(LOG_FONT)
				.borderWidth(BORDER_WIDTH).borderColor(Color.LIGHT_GRAY).wordBreak(true).padding(5f);

		int i = 1;
		for (Element e : elements) {
			if (i > count)
				break;
			tableBuilder.addRow(Row.builder().add(TextCell.builder().text(textSanitizer.sanitizeText(e.text()))
					.textColor(textColor).lineSpacing(MULTILINE_SPACING).build()).build());
			i++;
		}

		if (maxCodes) {
			tableBuilder
					.addRow(Row.builder()
							.add(TextCell.builder().text("Only first " + maxCodeBlockCount
									+ " code blocks are shown. Change this from the 'maxCodeBlockCount' setting.")
									.minHeight(15f).font(ReportFont.REGULAR_FONT).fontSize(10).textColor(Color.RED)
									.lineSpacing(MULTILINE_SPACING).build())
							.build());
		}

		return tableBuilder.build();
	}
}
