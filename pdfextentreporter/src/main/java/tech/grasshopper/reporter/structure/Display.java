package tech.grasshopper.reporter.structure;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.aventstack.extentreports.model.Report;

import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.ExtentPDFReporterConfig;

@Data
@SuperBuilder
public abstract class Display {

	public static final float CONTENT_START_Y = 775f;
	public static final float CONTENT_END_Y = 50f;
	public static final float CONTENT_MARGIN_TOP_Y = 67f;

	protected PDPageContentStream content;

	protected ExtentPDFReporterConfig config;

	protected PDDocument document;

	protected PDPage page;

	protected Report report;

	protected float ylocation;
	@Default
	protected float xlocation = 50f;

	public abstract void display();
}
