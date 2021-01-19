package tech.grasshopper.reporter.font;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import tech.grasshopper.reporter.exception.PdfReportException;

public class ReportFont {

	private static final Logger logger = Logger.getLogger(ReportFont.class.getName());

	public static PDFont REGULAR_FONT;
	public static PDFont BOLD_FONT;
	public static PDFont ITALIC_FONT;
	public static PDFont BOLD_ITALIC_FONT;
	public static final String FONT_FOLDER = "/tech/grasshopper/ttf/";

	private PDDocument document;

	public ReportFont(PDDocument document) {
		this.document = document;
	}

	public void loadReportFontFamily() {

		try {
			REGULAR_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-Regular.ttf"));

			BOLD_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-Bold.ttf"));

			ITALIC_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-Italic.ttf"));

			BOLD_ITALIC_FONT = PDType0Font.load(document,
					ReportFont.class.getResourceAsStream(FONT_FOLDER + "LiberationSans-BoldItalic.ttf"));
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"Unable to load report font - LiberationSans. The 'ttf' files should be available in '/tech/grasshopper/ttf' folder.",
					e);
			throw new PdfReportException(e);
		}
	}
}
