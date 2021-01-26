package tech.grasshopper.reporter;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;

import com.aventstack.extentreports.model.Report;

import tech.grasshopper.reporter.annotation.Annotation.AnnotationStore;
import tech.grasshopper.reporter.annotation.AnnotationProcessor;
import tech.grasshopper.reporter.bookmark.Bookmark;
import tech.grasshopper.reporter.context.AttributeSummary;
import tech.grasshopper.reporter.context.detail.AttributeDetails;
import tech.grasshopper.reporter.dashboard.Dashboard;
import tech.grasshopper.reporter.destination.Destination.DestinationStore;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.header.PageHeader;
import tech.grasshopper.reporter.tests.TestDetails;

public class ReportGenerator {

	private Report report;
	private ExtentPDFReporterConfig config;
	private File reportFile;
	private PDDocument document;
	private DestinationStore destinations;
	private AnnotationStore annotations;
	private PageHeader pageHeader;

	public ReportGenerator(Report report, ExtentPDFReporterConfig config, File file) {
		this.report = report;
		this.config = config;
		this.reportFile = file;
		this.document = new PDDocument();
		this.destinations = new DestinationStore();
		this.annotations = new AnnotationStore();
		this.pageHeader = new PageHeader();

		loadFontFamily();
		createReportDirectory();
	}

	public void generate() throws IOException {

		Dashboard.builder().document(document).report(report).config(config).destinations(destinations).build()
				.createSection();

		AttributeSummary.builder().document(document).report(report).config(config).destinations(destinations)
				.annotations(annotations).pageHeader(pageHeader).build().createSection();

		TestDetails.builder().document(document).report(report).config(config).destinations(destinations)
				.pageHeader(pageHeader).build().createSection();

		AttributeDetails.builder().document(document).report(report).config(config).destinations(destinations)
				.annotations(annotations).pageHeader(pageHeader).build().createSection();

		AnnotationProcessor.builder().annotations(annotations.getTestNameAnnotation())
				.destinations(destinations.getTestDestinations()).build().processTestNameAnnotation();
		AnnotationProcessor.builder().annotations(annotations.getAttributeNameAnnotation())
				.destinations(destinations.getAttributeDetailDestinations()).build().processAttributeNameAnnotation();

		Bookmark bookmark = Bookmark.builder().destinationStore(destinations).build();
		PDDocumentOutline outline = bookmark.createDocumentOutline();

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
