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
		Color color = Color.BLACK;
		String colorName = "";
		String[] spanClasses = element.className().split("\\s+");

		if (spanClasses.length > 0) {
			colorName = spanClasses[spanClasses.length - 1].trim();
		} else {
			// Add to logger
			return color;
		}

		try {
			color = (Color) Color.class.getField(colorName.toLowerCase()).get(null);
			if (color == Color.WHITE)
				color = Color.BLACK;
		} catch (Exception e) {
			// Add to logger
			color = Color.BLACK;
		}
		return color;
	}
}
