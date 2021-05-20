package tech.grasshopper.reporter.tests.markup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import com.aventstack.extentreports.model.Log;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.reporter.config.ExtentPDFReporterConfig;

@Data
@Builder
public class TestMarkup {

	private Log log;

	private float width;

	private ExtentPDFReporterConfig config;

	public AbstractCell createMarkupCell() {
		String html = log.getDetails();
		Document doc = Jsoup.parseBodyFragment(html);

		Element element = doc.selectFirst("body > span[class*=\"badge\"]");
		if (element != null) {
			return LabelMarkup.builder().element(element).build().displayDetails();
		}

		element = doc.selectFirst("body > table[class*=\"markup-table table\"]");
		if (element != null) {
			return TableMarkup.builder().element(element).textColor(config.statusColor(log.getStatus()))
					.maxTableColumnCount(config.getMaxTableColumnCount()).width(width).build().displayDetails();
		}

		Elements elements = doc.select("body > ol > li");
		if (elements.size() > 0) {

			return OrderedListMarkup.builder().elements(elements).textColor(config.statusColor(log.getStatus())).build()
					.displayDetails();
		}

		elements = doc.select("body > ul > li");
		if (elements.size() > 0) {
			return UnorderedListMarkup.builder().elements(elements).textColor(config.statusColor(log.getStatus()))
					.build().displayDetails();
		}

		elements = doc.select("body textarea[class*=\"code-block\"]");
		if (elements.size() > 0) {
			return CodeBlockMarkup.builder().elements(elements).textColor(config.statusColor(log.getStatus()))
					.width(width).build().displayDetails();
		}

		if (html.contains("JSONTree")) {
			return JsonMarkup.builder().html(html).textColor(config.statusColor(log.getStatus())).build()
					.displayDetails();
		}

		return DefaultMarkup.builder().log(log).textColor(config.statusColor(log.getStatus())).build().displayDetails();
	}

	public static boolean isMarkup(String markup) {
		if (markup.trim().startsWith("<") && markup.trim().endsWith(">"))
			return true;
		return false;
	}

}
