package tech.grasshopper.reporter.context.detail;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDPage;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.NamedAttribute;
import com.aventstack.extentreports.model.context.NamedAttributeContext;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.reporter.context.AttributeType;
import tech.grasshopper.reporter.destination.Destination;
import tech.grasshopper.reporter.destination.DestinationAware;
import tech.grasshopper.reporter.font.ReportFont;
import tech.grasshopper.reporter.structure.Display;
import tech.grasshopper.reporter.structure.TableCreator;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class AttributeTestStatusBasicDisplay extends Display implements DestinationAware {

	private static final int NAME_FONT_SIZE = 15;
	private static final int STATUS_FONT_SIZE = 12;

	private static final float NAME_STATUS_WIDTH = 500f;
	private static final float NAME_HEIGHT = 25f;
	private static final float STATUS_HEIGHT = 20f;
	private static final float GAP_HEIGHT = 5f;
	private static final float PADDING = 5f;
	protected static final float MULTILINE_SPACING = 1f;

	protected NamedAttributeContext<? extends NamedAttribute> attribute;

	protected AttributeType type;

	protected TableBuilder tableBuilder;

	private int destinationY;

	@Override
	public void display() {

		createDetailsTableBuilder();
		createNameRow();
		createStatusRow();
		drawDetailsTable();
	}

	private void createDetailsTableBuilder() {
		tableBuilder = Table.builder().addColumnsOfWidth(NAME_STATUS_WIDTH).padding(PADDING).borderWidth(0)
				.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.MIDDLE);
	}

	private void createNameRow() {
		tableBuilder.addRow(Row.builder()
				.add(TextCell.builder().minHeight(NAME_HEIGHT).fontSize(NAME_FONT_SIZE)
						.font(ReportFont.BOLD_ITALIC_FONT)
						.text(type.toString().toLowerCase() + "- " + attribute.getAttr().getName()).wordBreak(true)
						.lineSpacing(MULTILINE_SPACING).textColor(nameColor()).build())
				.build());
	}

	private void createStatusRow() {
		String statusText = "";
		for (Status status : Status.values()) {
			if (attribute.getStatusDist().getOrDefault(status, 0) > 0)
				statusText = statusText + "/ " + attribute.getStatusDist().get(status) + " " + status + " ";
		}
		if (!statusText.isEmpty())
			statusText = statusText + "/";
		;

		tableBuilder
				.addRow(Row.builder().height(STATUS_HEIGHT).fontSize(STATUS_FONT_SIZE).font(ReportFont.BOLD_ITALIC_FONT)
						.add(TextCell.builder().text(statusText).textColor(config.getAttributeTestStatusColor()).build()).build());
	}

	protected void drawDetailsTable() {
		PDPage initialPage = document.getPage(document.getNumberOfPages() - 1);
		destinationY = (int) ylocation;

		TableCreator table = TableCreator.builder().tableBuilder(tableBuilder).document(document).startX(xlocation)
				.startY(ylocation).repeatRows(2).build();
		table.displayTable();

		ylocation = table.getFinalY() - GAP_HEIGHT;
		page = table.getTableStartPage();

		if (!initialPage.equals(page))
			destinationY = (int) Display.CONTENT_START_Y;
	}

	@Override
	public Destination createDestination() {
		return Destination.builder().name(type.toString().toLowerCase() + "- " + attribute.getAttr().getName())
				.yCoord(destinationY).page(page).build();
	}

	private Color nameColor() {
		if (type == AttributeType.CATEGORY)
			return config.getCategoryTitleColor();
		if (type == AttributeType.AUTHOR)
			return config.getAuthorTitleColor();
		if (type == AttributeType.DEVICE)
			return config.getDeviceTitleColor();
		if (type == AttributeType.EXCEPTION)
			return config.getExceptionTitleColor();
		return Color.RED;
	}
}
