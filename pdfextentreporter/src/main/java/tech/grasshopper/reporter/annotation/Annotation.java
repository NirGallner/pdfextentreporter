package tech.grasshopper.reporter.annotation;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Annotation {

	private String title;
	private int id;
	private float xBottom;
	private float yBottom;
	private float width;
	private float height;
	private PDPage page;

	public PDAnnotationLink createPDAnnotationLink() {

		PDRectangle position = new PDRectangle(xBottom, yBottom, width, height);
		PDAnnotationLink link = new PDAnnotationLink();

		PDBorderStyleDictionary borderULine = new PDBorderStyleDictionary();
		link.setBorderStyle(borderULine);

		link.setRectangle(position);
		link.setHighlightMode(PDAnnotationLink.HIGHLIGHT_MODE_PUSH);
		return link;
	}

	@Data
	public static class AnnotationStore {

		private List<Annotation> testNameAnnotation = new ArrayList<>();

		private List<Annotation> attributeNameAnnotation = new ArrayList<>();

		public void addTestNameAnnotation(Annotation annotation) {
			testNameAnnotation.add(annotation);
		}

		public void addAttributeNameAnnotation(Annotation annotation) {
			attributeNameAnnotation.add(annotation);
		}
	}
}
