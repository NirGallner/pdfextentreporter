package tech.grasshopper.reporter.expanded;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.structure.cell.ImageCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.medias.Medias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ExpandedMedia extends Medias {

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
		return ImageCell.builder().image(image).width(image.getWidth()).padding(padding).maxHeight(image.getHeight())
				.build();
	}
}
