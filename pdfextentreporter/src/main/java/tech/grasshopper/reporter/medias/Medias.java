package tech.grasshopper.reporter.medias;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.structure.cell.ImageCell;

import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.service.MediaService;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Medias {

	protected Media media;
	protected PDDocument document;

	protected static PDImageXObject imageNotFound = null;

	@SneakyThrows
	protected void initializeNotFoundImage() {
		if (imageNotFound == null) {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("not-found-image.png");
			byte[] targetArray = new byte[is.available()];
			is.read(targetArray);

			imageNotFound = PDImageXObject.createFromByteArray(document, targetArray, "not-found-image");
		}
	}

	@SneakyThrows
	protected PDImageXObject generatePDImage() {
		PDImageXObject image = null;

		// create base64 image file
		if (MediaService.isBase64(media))
			image = imageNotFound;
		else
			image = PDImageXObject.createFromFile(media.getPath(), document);

		return image;
	}

	public abstract ImageCell createImageCell();
}
