package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.structure.cell.TextLabelCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class LabelMarkup extends MarkupDisplay {

	@Override
	public AbstractCell displayDetails() {

		return TextLabelCell.builder().text(textSanitizer.sanitizeText(element.text())).labelColor(textColor())
				.fontSize(LOG_FONT_SIZE).font(LOG_FONT).lineSpacing(MULTILINE_SPACING).build();
	}

	private Color textColor() {
		Color color = null;
		String colorName = element.className().substring(element.className().lastIndexOf(' ') + 1);

		try {
			color = (Color) Color.class.getField(colorName.toLowerCase()).get(null);
			if (color == Color.WHITE)
				color = Color.BLACK;
		} catch (Exception e) {
			color = Color.BLACK;
		}
		return color;
	}
}
