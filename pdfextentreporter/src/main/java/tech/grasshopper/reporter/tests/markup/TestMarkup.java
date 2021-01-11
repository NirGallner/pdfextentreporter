package tech.grasshopper.reporter.tests.markup;

import java.awt.Color;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.model.Log;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class TestMarkup {

	private Log log;

	private float width;
	@Default
	private float lineSpacing = 1f;
	@Default
	private Color textColor = Color.BLACK;

	public AbstractCell createMarkupCell() {
		String html = log.getDetails();
		Document doc = Jsoup.parseBodyFragment(html);

		Element element = doc.selectFirst("body > span[class*=\"badge\"]");
		if (element != null)
			return LabelMarkup.builder().element(element).build().displayDetails();

		element = doc.selectFirst("body > table[class*=\"markup-table table\"]");
		if (element != null)
			return TableMarkup.builder().element(element).textColor(textColor).width(width).build().displayDetails();

		Elements elements = doc.select("body > ol > li");
		if (elements.size() > 0)
			return OrderedListMarkup.builder().elements(elements).textColor(textColor).build().displayDetails();

		elements = doc.select("body > ul > li");
		if (elements.size() > 0)
			return UnorderedListMarkup.builder().elements(elements).textColor(textColor).build().displayDetails();

		elements = doc.select("body textarea[class*=\"code-block\"]");
		if (elements.size() > 0)
			return CodeBlockMarkup.builder().elements(elements).textColor(textColor).width(width).build()
					.displayDetails();

		if (html.contains("JSONTree"))
			return JsonMarkup.builder().html(html).textColor(textColor).build().displayDetails();

		// TODO Fix this stuff
		return TextCell.builder().text(html).lineSpacing(lineSpacing).textColor(textColor).build();
	}

	public static boolean isMarkup(String markup) {
		if (markup.trim().startsWith("<") && markup.trim().endsWith(">"))
			return true;
		return false;
	}

}
