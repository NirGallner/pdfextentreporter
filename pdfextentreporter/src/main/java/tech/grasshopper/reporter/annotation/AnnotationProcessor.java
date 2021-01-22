package tech.grasshopper.reporter.annotation;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;

import tech.grasshopper.reporter.destination.Destination;

public class AnnotationProcessor {

	private static final Logger logger = Logger.getLogger(AnnotationProcessor.class.getName());

	public static void processTestNameAnnotation(List<Annotation> testNameAnnotation,
			List<Destination> testNameDestinations) {

		testNameDestinations.forEach(d -> {
			List<Annotation> matchedAnnotation = testNameAnnotation.stream().filter(a -> a.getId() == d.getId())
					.collect(Collectors.toList());

			matchedAnnotation.forEach(a -> {
				PDActionGoTo action = new PDActionGoTo();
				action.setDestination(d.createPDPageDestination());
				
				PDAnnotationLink annotationLink = a.createPDAnnotationLink();
				annotationLink.setAction(action);

				try {
					a.getPage().getAnnotations().add(annotationLink);
				} catch (IOException e) {
					logger.log(Level.WARNING, "Unable to create annotation link", e);
				}
			});
		});
	}
}
