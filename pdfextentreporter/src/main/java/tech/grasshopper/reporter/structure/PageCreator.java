package tech.grasshopper.reporter.structure;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

import lombok.Builder;
import lombok.Data;
import tech.grasshopper.reporter.exception.PdfReportException;

@Data
@Builder
public class PageCreator {

	private static final Logger logger = Logger.getLogger(PageCreator.class.getName());

	private PDDocument document;

	private PDPageContentStream content;

	private PDPage page;

	public PDPage createPotraitPageAndContentStream() {
		page = new PDPage(PDRectangle.A4);
		document.addPage(page);
		try {
			content = new PDPageContentStream(document, page, AppendMode.APPEND, true);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occured in creating the ContentStream - " + e.getMessage());
			throw new PdfReportException(e);
		}
		return page;
	}

	public PDPage createLandscapePageAndContentStream() {
		page = createPotraitPageAndContentStream();
		page.setRotation(90);
		try {
			content.transform(new Matrix(0, 1, -1, 0, page.getMediaBox().getWidth(), 0));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occured in transforming the ContentStream - " + e.getMessage());
			throw new PdfReportException(e);
		}
		return page;
	}

	public void closeContentStream() {
		try {
			content.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occured in closing the ContentStream - " + e.getMessage());
			throw new PdfReportException(e);
		}
	}

	public PDPage createPotraitPage() {
		page = new PDPage(PDRectangle.A4);
		document.addPage(page);
		return page;
	}

	public static Supplier<PDPage> potraitPageSupplier() {
		return () -> new PDPage(PDRectangle.A4);
	}
}
