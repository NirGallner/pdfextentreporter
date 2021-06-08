package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.structure.cell.TextLabelCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class LabelMarkup extends MarkupDisplay {

	private static final Logger logger = Logger.getLogger(LabelMarkup.class.getName());

	@Override
	public AbstractCell displayDetails() {
		String text = "";
		try {
			text = element.text();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to retrieve text for label text.");
			return errorDisplay("Label text not available.");
		}

		Color textColor = null;
		try {
			textColor = textColor();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unable to retrieve text color for label text, using default black.");
			return TextLabelCell.builder().text(textSanitizer.sanitizeText(element.text())).labelColor(Color.BLACK)
					.fontSize(LOG_FONT_SIZE).font(LOG_FONT).lineSpacing(MULTILINE_SPACING).build();
		}

		return TextLabelCell.builder().text(textSanitizer.sanitizeText(text)).labelColor(textColor)
				.fontSize(LOG_FONT_SIZE).font(LOG_FONT).lineSpacing(MULTILINE_SPACING).build();
	}

	private Color textColor() {
		Color color = Color.BLACK;
		String colorName = "";
		String[] spanClasses = element.className().split("\\s+");

		if (spanClasses.length > 0) {
			colorName = spanClasses[spanClasses.length - 1].trim();
		} else {
			logger.log(Level.WARNING, "No label color available, using default black.");
			return color;
		}

		try {
			color = (Color) Color.class.getField(colorName.toLowerCase()).get(null);
			if (color == Color.WHITE) {
				color = Color.BLACK;
				logger.log(Level.INFO, "Label color white cannot be displayed properly, using default black.");
			}
		} catch (Exception e) {
			color = Color.BLACK;
			logger.log(Level.WARNING, "No matching color name found for label in class java.awt.Color for "
					+ colorName.toUpperCase() + ", using default black.");
		}
		return color;
	}
}
