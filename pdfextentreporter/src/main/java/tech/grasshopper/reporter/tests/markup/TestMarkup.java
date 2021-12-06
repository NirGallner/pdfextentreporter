package tech.grasshopper.reporter.tests.markup;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import tech.grasshopper.reporter.config.ExtentPDFReporterConfig;
import tech.grasshopper.reporter.font.ReportFont;

@Data
@Builder
public class TestMarkup {

	private static final Logger logger = Logger.getLogger(TestMarkup.class.getName());

	private Test test;

	private Log log;

	private float width;

	private ExtentPDFReporterConfig config;

	@NonNull
	private ReportFont reportFont;

	@Default
	private boolean bddReport = false;

	public AbstractCell createMarkupCell() {
		String html = log.getDetails();
		Status status = bddReport ? test.getStatus() : log.getStatus();

		Document document = null;
		try {
			document = Jsoup.parseBodyFragment(html);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error is parsing markup html, diplaying raw log details");
			return displayDefault(status);
		}

		Element element = document.selectFirst("body > span[class*=\"badge\"]");
		if (element != null) {
			return LabelMarkup.builder().element(element).logFont(reportFont.getRegularFont()).build().displayDetails();
		}

		element = document.selectFirst("body > table[class*=\"markup-table table\"]");
		if (element != null) {
			return TableMarkup.builder().element(element).logFont(reportFont.getRegularFont())
					.textColor(config.statusColor(status)).maxTableColumnCount(config.getMaxTableColumnCount())
					.width(width).build().displayDetails();
		}

		Elements elements = document.select("body > ol > li");
		if (elements.size() > 0) {
			return OrderedListMarkup.builder().elements(elements).logFont(reportFont.getRegularFont())
					.textColor(config.statusColor(status)).width(width).build().displayDetails();
		}

		elements = document.select("body > ul > li");
		if (elements.size() > 0) {
			return UnorderedListMarkup.builder().elements(elements).logFont(reportFont.getRegularFont())
					.textColor(config.statusColor(status)).width(width).build().displayDetails();
		}

		elements = document.select("body textarea[class*=\"code-block\"]");
		if (elements.size() > 0) {
			return CodeBlockMarkup.builder().elements(elements).logFont(reportFont.getRegularFont())
					.textColor(config.statusColor(status)).width(width).build().displayDetails();
		}

		if (html.contains("JSONTree")) {
			return JsonMarkup.builder().html(html).logFont(reportFont.getRegularFont())
					.textColor(config.statusColor(status)).build().displayDetails();
		}

		return displayDefault(status);
	}

	private AbstractCell displayDefault(Status status) {
		return DefaultMarkup.builder().log(log).logFont(reportFont.getRegularFont())
				.textColor(config.statusColor(status)).build().displayDetails();
	}

	public static boolean isMarkup(String markup) {
		if (markup.trim().startsWith("<") && markup.trim().endsWith(">"))
			return true;
		return false;
	}

}
