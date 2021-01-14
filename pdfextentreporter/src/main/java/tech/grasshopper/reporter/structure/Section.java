package tech.grasshopper.reporter.structure;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.aventstack.extentreports.model.Report;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.ExtentPDFReporterConfig;
import tech.grasshopper.reporter.destination.Destination.DestinationStore;
import tech.grasshopper.reporter.header.PageHeader;

@Data
@SuperBuilder
public abstract class Section {

	protected PDDocument document;

	protected DestinationStore destinations;

	protected ExtentPDFReporterConfig config;

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
