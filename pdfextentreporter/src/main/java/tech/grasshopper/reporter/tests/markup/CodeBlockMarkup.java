package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Element;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;
import org.vandeseer.easytable.util.PdfUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CodeBlockMarkup extends MarkupDisplay {

	private float width;

	private static final int MAX_DASH_COUNT = 100;
	private static final int DEFAULT_DASH_COUNT = 10;

	@Override
	public AbstractCell displayDetails() {
		return ParagraphCell.builder().paragraph(codeDetails()).width(width).padding(5f).lineSpacing(1.1f).build();
	}

	private Paragraph codeDetails() {
		ParagraphBuilder paragraphBuilder = Paragraph.builder();

		int count = 1;
		for (Element e : elements) {
			StyledText text = StyledText.builder().fontSize((float) LOG_FONT_SIZE).font(LOG_FONT).color(textColor)
					.text(textSanitizer.sanitizeText(e.text())).build();
			paragraphBuilder.append(text);

			if (count < elements.size()) {
				paragraphBuilder.appendNewLine(10f);
				paragraphBuilder.append(StyledText.builder().fontSize((float) LOG_FONT_SIZE).font(LOG_FONT)
						.color(Color.GRAY).text(createDashedLine()).build());
				paragraphBuilder.appendNewLine(10f);
			}
			count++;
		}
		return paragraphBuilder.build();
	}

	private String createDashedLine() {
		// Subtract 5f just for kicks
		List<String> dashedLines = PdfUtil.getOptimalTextBreakLines(
				String.join("", Collections.nCopies(MAX_DASH_COUNT, "-")), LOG_FONT, LOG_FONT_SIZE, width - 5f);

		return dashedLines.isEmpty() ? String.join("", Collections.nCopies(DEFAULT_DASH_COUNT, "-"))
				: dashedLines.get(0);
	}
}
