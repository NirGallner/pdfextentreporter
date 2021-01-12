package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;

@Data
@SuperBuilder
public abstract class MarkupDisplay {

	protected final float BORDER_WIDTH = 1f;
	protected final float MULTILINE_SPACING = 1f;

	protected final PDFont LOG_FONT = ReportFont.REGULAR_FONT;
	protected final int LOG_FONT_SIZE = 11;

	protected Element element;
	protected Elements elements;

	@Default
	protected Color textColor = Color.BLACK;
	
	protected final TextSanitizer textSanitizer = TextSanitizer.builder().font(LOG_FONT).build();

	public abstract AbstractCell displayDetails();
}
