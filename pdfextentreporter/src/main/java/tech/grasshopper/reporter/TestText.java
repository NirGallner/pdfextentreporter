package tech.grasshopper.reporter;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.optimizer.TextSanitizer;

public class TestText {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		PDDocument document = new PDDocument();

		PDFont font = PDType0Font.load(document,
				ReportFont.class.getResourceAsStream("/tech/grasshopper/ttf/LiberationSans-Regular.ttf"));
		
		System.out.println(font.getName());
		
		String text = "abc n \r\n\t";
		
		String result = TextSanitizer.builder().font(font).build().sanitizeText(text);
		
		System.out.println(result);
		System.out.println(System.getProperty("line.separator"));
	}

}
