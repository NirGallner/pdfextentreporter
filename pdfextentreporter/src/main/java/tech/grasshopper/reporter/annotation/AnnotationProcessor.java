package tech.grasshopper.reporter.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.reporter.destination.Destination;

@Data
@Builder
public class AnnotationProcessor {

	private static final Logger logger = Logger.getLogger(AnnotationProcessor.class.getName());

	@Default
	private List<Annotation> annotations = new ArrayList<>();

	@Default
	private List<Destination> destinations = new ArrayList<>();

	public void processTestNameAnnotation() {
		destinations.forEach(d -> {
			List<Annotation> matchedAnnotations = annotations.stream().filter(a -> a.getId() == d.getId())
					.collect(Collectors.toList());
			processMatchedAnnotations(matchedAnnotations, d);
		});
	}

	public void processAttributeNameAnnotation() {
		destinations.forEach(d -> {
			List<Annotation> matchedAnnotations = annotations.stream().filter(a -> a.getTitle().equals(d.getName()))
					.collect(Collectors.toList());
			processMatchedAnnotations(matchedAnnotations, d);
		});
	}

	private void processMatchedAnnotations(List<Annotation> matchedAnnotation, Destination destination) {

		matchedAnnotation.forEach(a -> {
			PDActionGoTo action = new PDActionGoTo();
			action.setDestination(destination.createPDPageDestination());

			PDAnnotationLink annotationLink = a.createPDAnnotationLink();
			annotationLink.setAction(action);

			try {
				a.getPage().getAnnotations().add(annotationLink);
			} catch (IOException e) {
				logger.log(Level.WARNING, "Unable to create annotation link", e);
			}
		});
	}

}
