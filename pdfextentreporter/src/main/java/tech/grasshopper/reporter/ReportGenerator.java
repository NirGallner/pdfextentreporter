package tech.grasshopper.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;

import com.aventstack.extentreports.model.Report;

import tech.grasshopper.reporter.annotation.AnnotationProcessor;
import tech.grasshopper.reporter.annotation.AnnotationStore;
import tech.grasshopper.reporter.bookmark.Bookmark;
import tech.grasshopper.reporter.config.ExtentPDFReporterConfig;
import tech.grasshopper.reporter.context.AttributeSummary;
import tech.grasshopper.reporter.context.detail.AttributeDetails;
import tech.grasshopper.reporter.dashboard.Dashboard;
import tech.grasshopper.reporter.destination.Destination.DestinationStore;
import tech.grasshopper.reporter.expanded.BDDMediaSummary;
import tech.grasshopper.reporter.expanded.MediaSummary;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.header.PageHeader;
import tech.grasshopper.reporter.tests.TestBDDDetails;
import tech.grasshopper.reporter.tests.TestDetails;

public class ReportGenerator {

	private Report report;
	private ExtentPDFReporterConfig config;
	private File reportFile;
	private PDDocument document;
	private ReportFont reportFont;
	private DestinationStore destinations;
	private AnnotationStore annotations;
	private PageHeader pageHeader;

	public ReportGenerator(Report report, ExtentPDFReporterConfig config, File file) {
		this.report = report;
		this.config = config;
		this.reportFile = file;
		this.document = new PDDocument();
		this.reportFont = new ReportFont(this.document);
		this.destinations = new DestinationStore();
		this.annotations = new AnnotationStore();
		this.pageHeader = new PageHeader();

		createReportDirectory();
	}

	public void generate() throws IOException {

		Dashboard.builder().document(document).reportFont(reportFont).report(report).config(config)
				.destinations(destinations).build().createSection();

		if (config.isDisplayAttributeSummary())
			AttributeSummary.builder().document(document).report(report).reportFont(reportFont).config(config)
					.destinations(destinations).annotations(annotations).pageHeader(pageHeader).build().createSection();

		if (config.isDisplayTestDetails()) {
			if (report.isBDD())
				TestBDDDetails.builder().document(document).report(report).reportFont(reportFont).config(config)
						.destinations(destinations).annotations(annotations).pageHeader(pageHeader).build()
						.createSection();
			else
				TestDetails.builder().document(document).report(report).reportFont(reportFont).config(config)
						.destinations(destinations).annotations(annotations).pageHeader(pageHeader).build()
						.createSection();
		}

		if (config.isDisplayAttributeDetails())
			AttributeDetails.builder().document(document).report(report).reportFont(reportFont).config(config)
					.destinations(destinations).annotations(annotations).pageHeader(pageHeader).build().createSection();

		if (config.isDisplayExpandedMedia()) {
			if (report.isBDD())
				BDDMediaSummary.builder().document(document).report(report).reportFont(reportFont).config(config)
						.destinations(destinations).annotations(annotations).pageHeader(pageHeader).build()
						.createSection();
			else
				MediaSummary.builder().document(document).report(report).reportFont(reportFont).config(config)
						.destinations(destinations).annotations(annotations).pageHeader(pageHeader).build()
						.createSection();
		}

		AnnotationProcessor.builder().annotations(annotations).destinations(destinations).config(config).build()
				.processAnnotations();

		Bookmark bookmark = Bookmark.builder().destinationStore(destinations).config(config).report(report).build();
		PDDocumentOutline outline = bookmark.createDocumentOutline();

		document.getDocumentCatalog().setDocumentOutline(outline);
		document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);

		pageHeader.processHeader(document, reportFont);

		document.save(reportFile);
		document.close();
	}

	private void createReportDirectory() {
		File dir = new File(this.reportFile.getParent());
		if (!dir.exists())
			dir.mkdirs();
	}
}
