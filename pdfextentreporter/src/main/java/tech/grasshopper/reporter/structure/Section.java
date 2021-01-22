package tech.grasshopper.reporter.structure;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.aventstack.extentreports.model.Report;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.ExtentPDFReporterConfig;
import tech.grasshopper.reporter.annotation.Annotation.AnnotationStore;
import tech.grasshopper.reporter.destination.Destination.DestinationStore;
import tech.grasshopper.reporter.header.PageHeader;

@Data
@SuperBuilder
public abstract class Section {

	@NonNull
	protected PDDocument document;

	protected DestinationStore destinations;

	protected AnnotationStore annotations;

	protected ExtentPDFReporterConfig config;

	@NonNull
	protected Report report;

	protected PageHeader pageHeader;

	public abstract void createSection();

	protected void createPage() {
		PageCreator pageCreator = PageCreator.builder().document(document).build();
		pageCreator.createPotraitPage();
	}

	public void pageHeaderDetails() {
		pageHeader.addSectionPageData(getSectionTitle(), document.getNumberOfPages());
	}

	public abstract String getSectionTitle();
}
