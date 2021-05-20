package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;
import java.util.Collections;

import org.jsoup.nodes.Element;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph.ParagraphBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CodeBlockMarkup extends MarkupDisplay {

	private float width;

	private int maxCodeBlockCount;

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
				paragraphBuilder.append(StyledText.builder().fontSize(12f).font(LOG_FONT).color(Color.GRAY)
						.text(String.join("", Collections.nCopies(92, "-"))).build());
				paragraphBuilder.appendNewLine(10f);
			}
			count++;
		}

		return paragraphBuilder.build();
	}
}
