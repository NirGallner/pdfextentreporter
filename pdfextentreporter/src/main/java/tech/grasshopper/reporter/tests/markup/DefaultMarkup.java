package tech.grasshopper.reporter.tests.markup;

import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Log;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DefaultMarkup extends MarkupDisplay {

	private Log log;

	@Override
	public AbstractCell displayDetails() {

		return TextCell.builder().text(textSanitizer.sanitizeText(log.getDetails())).textColor(textColor)
				.fontSize(LOG_FONT_SIZE).font(LOG_FONT).lineSpacing(MULTILINE_SPACING).build();
	}
}
