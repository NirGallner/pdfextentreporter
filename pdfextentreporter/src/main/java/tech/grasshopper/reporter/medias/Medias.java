package tech.grasshopper.reporter.medias;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.structure.cell.ImageCell;

import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.service.MediaService;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class Medias {

	protected Media media;
	protected PDDocument document;
	protected PDImageXObject image;

	protected float padding;

	protected static PDImageXObject imageNotFound = null;

	private void initializeNotFoundImage() throws IOException {
		if (imageNotFound == null) {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("not-found-image.png");
			byte[] targetArray = new byte[is.available()];
			is.read(targetArray);

			imageNotFound = PDImageXObject.createFromByteArray(document, targetArray, "not-found-image");
		}
	}

	private void generatePDImage() throws IOException {
		// create base64 image file
		if (MediaService.isBase64(media))
			image = imageNotFound;
		else
			image = PDImageXObject.createFromFile(media.getPath(), document);
	}

	protected PDImageXObject processPDImage() {
		try {
			initializeNotFoundImage();
			generatePDImage();
		} catch (Exception e) {
			// Todo write logger
			image = imageNotFound;
		}
		return image;
	}

	public abstract ImageCell createImageCell();
}
