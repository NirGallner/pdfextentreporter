package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class LabelMarkup extends MarkupDisplay {

	@Override
	public AbstractCell displayDetails() {

		return TextCell.builder().text(textSanitizer.sanitizeText(element.text())).textColor(textColor())
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
