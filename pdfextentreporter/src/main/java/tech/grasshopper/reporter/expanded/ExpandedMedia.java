package tech.grasshopper.reporter.expanded;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.structure.cell.ImageCell;

import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.service.MediaService;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpandedMedia {

	private Media media;
	private PDDocument document;

	private float padding;

	private static PDImageXObject imageNotFound = null;

	public ImageCell createImageCell() {

		PDImageXObject image = null;

		try {
			if (imageNotFound == null)
				imageNotFound = PDImageXObject.createFromFile("src/main/resources/not-found-image.png", document);

			// create base64 image file
			if (MediaService.isBase64(media))
				image = imageNotFound;
			else
				image = PDImageXObject.createFromFile(media.getPath(), document);
		} catch (IOException e) {
			// Todo write logger
			image = imageNotFound;
		}
		return ImageCell.builder().image(image).width(image.getWidth()).padding(padding).maxHeight(image.getHeight())
				.build();
	}
}
