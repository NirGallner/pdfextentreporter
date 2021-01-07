package tech.grasshopper.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;

import com.aventstack.extentreports.model.Report;

import tech.grasshopper.reporter.bookmark.Bookmark;
import tech.grasshopper.reporter.context.AttributeSummary;
import tech.grasshopper.reporter.context.detail.AttributeDetails;
import tech.grasshopper.reporter.dashboard.Dashboard;
import tech.grasshopper.reporter.destination.Destination.DestinationStore;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.tests.TestDetails;

public class ReportGenerator {

	private Report report;
	private File reportFile;
	private PDDocument document;
	private DestinationStore destinations;

	public ReportGenerator(Report report, File file) {
		this.report = report;
		this.reportFile = file;
		this.document = new PDDocument();
		this.destinations = new DestinationStore();

		loadFontFamily();
		createReportDirectory();
	}

	public void generate() throws IOException {

		Dashboard.builder().document(document).report(report).destinations(destinations).build().createSection();

		AttributeSummary.builder().document(document).report(report).destinations(destinations).build().createSection();

		TestDetails.builder().document(document).report(report).destinations(destinations).build().createSection();

		AttributeDetails.builder().document(document).report(report).destinations(destinations).build().createSection();

		Bookmark bookmark = Bookmark.builder()/* .reportConfig(reportConfig) */.build();
		PDDocumentOutline outline = bookmark.createDocumentOutline(destinations);

		// Annotation.updateDestination(reportData);

		document.getDocumentCatalog().setDocumentOutline(outline);
		document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);

		document.save(reportFile);
		document.close();
	}

	private void loadFontFamily() {
		ReportFont reportFont = new ReportFont(document);
		reportFont.loadReportFontFamily();
	}

	private void createReportDirectory() {
		File dir = new File(this.reportFile.getParent());
		if (!dir.exists())
			dir.mkdirs();
	}

}
