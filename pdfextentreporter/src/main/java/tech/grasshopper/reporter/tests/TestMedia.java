package tech.grasshopper.reporter.tests;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.structure.cell.ImageCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.medias.Medias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class TestMedia extends Medias {

	private float width;
	private float height;
	private float padding;

	public ImageCell createImageCell() {

		PDImageXObject image = null;

		try {
			initializeNotFoundImage();
			image = generatePDImage();
		} catch (Exception e) {
			// Todo write logger
			image = imageNotFound;
		}
		return ImageCell.builder().image(image).width(width).padding(padding).maxHeight(height).build();
	}
}
