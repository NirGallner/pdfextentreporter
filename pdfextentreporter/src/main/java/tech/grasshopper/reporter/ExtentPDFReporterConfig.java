package tech.grasshopper.reporter;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.aventstack.extentreports.reporter.configuration.AbstractConfiguration;

import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@SuperBuilder
public class ExtentPDFReporterConfig extends AbstractConfiguration {

	private String name;
	private String nameColor;
	private String dateFormat;
	private String dateColor;

	private String startTimesColor;
	private String finishTimesColor;
	private String durationColor;
	private String passCountColor;
	private String failCountColor;

	private String passColor;
	private String failColor;
	private String skipColor;
	private String warnColor;
	private String infoColor;

	private String categoryTitleColor;
	private String categoryNameColor;

	private String authorTitleColor;
	private String authorNameColor;

	private String deviceTitleColor;
	private String deviceNameColor;

	private String systemTitleColor;
	private String systemNameColor;
	private String systemValueColor;

	private String exceptionTitleColor;

	private String attributeTestStatusColor;

	private String testNameColor;
	private String testTimesColor;
	private String testTimeStampColor;
	private String testExceptionColor;

	public String getReportName() {
		if (name == null || name.isEmpty())
			name = Defaults.name;
		return name;
	}

	public Color getReportNameColor() {
		return createColor(nameColor, Defaults.nameColor);
	}

	public DateTimeFormatter getReportDateFormat() {
		try {
			return DateTimeFormatter.ofPattern(dateFormat);
		} catch (Exception e) {
			// Log the exception
			return Defaults.dateFormatter;
		}
	}

	public Color getReportDateColor() {
		return createColor(dateColor, Defaults.dateColor);
	}

	public Color getStartTimesColor() {
		return createColor(startTimesColor, Defaults.startTimesColor);
	}

	public Color getFinishTimesColor() {
		return createColor(finishTimesColor, Defaults.finishTimesColor);
	}

	public Color getDurationColor() {
		return createColor(durationColor, Defaults.durationColor);
	}

	public Color getPassCountColor() {
		return createColor(passCountColor, Defaults.passCountColor);
	}

	public Color getFailCountColor() {
		return createColor(failCountColor, Defaults.failCountColor);
	}

	public Color getPassColor() {
		return createColor(passColor, Defaults.passColor);
	}

	public Color getFailColor() {
		return createColor(failColor, Defaults.failColor);
	}

	public Color getSkipColor() {
		return createColor(skipColor, Defaults.skipColor);
	}

	public Color getWarnColor() {
		return createColor(warnColor, Defaults.warnColor);
	}

	public Color getInfoColor() {
		return createColor(infoColor, Defaults.infoColor);
	}

	public Color getCategoryTitleColor() {
		return createColor(categoryTitleColor, Defaults.categoryTitleColor);
	}

	public Color getCategoryNameColor() {
		return createColor(categoryNameColor, Defaults.categoryNameColor);
	}

	public Color getAuthorTitleColor() {
		return createColor(authorTitleColor, Defaults.authorTitleColor);
	}

	public Color getAuthorNameColor() {
		return createColor(authorNameColor, Defaults.authorNameColor);
	}

	public Color getDeviceTitleColor() {
		return createColor(deviceTitleColor, Defaults.deviceTitleColor);
	}

	public Color getDeviceNameColor() {
		return createColor(deviceNameColor, Defaults.deviceNameColor);
	}

	public Color getSystemTitleColor() {
		return createColor(systemTitleColor, Defaults.systemTitleColor);
	}

	public Color getSystemNameColor() {
		return createColor(systemNameColor, Defaults.systemNameColor);
	}

	public Color getSystemValueColor() {
		return createColor(systemValueColor, Defaults.systemValueColor);
	}

	public Color getExceptionTitleColor() {
		return createColor(exceptionTitleColor, Defaults.exceptionTitleColor);
	}

	public Color getAttributeTestStatusColor() {
		return createColor(attributeTestStatusColor, Defaults.attributeTestStatusColor);
	}

	public Color getTestNameColor() {
		return createColor(testNameColor, Defaults.testNameColor);
	}

	public Color getTestTimesColor() {
		return createColor(testTimesColor, Defaults.testTimesColor);
	}

	public Color getTestTimeStampColor() {
		return createColor(testTimeStampColor, Defaults.testTimeStampColor);
	}

	public Color getTestExceptionColor() {
		return createColor(testExceptionColor, Defaults.testExceptionColor);
	}

	private Color createColor(String hexCode, Color defaultColor) {
		try {
			return Color.decode("#" + hexCode);
		} catch (Exception e) {
			// Log the exception
			return defaultColor;
		}
	}

	private static class Defaults {

		private static final String name = "PDF Extent Report";
		private static final Color nameColor = Color.BLACK;
		private static final DateTimeFormatter dateFormatter = DateTimeFormatter
				.ofLocalizedDateTime(FormatStyle.MEDIUM);
		private static final Color dateColor = Color.BLUE;
		private static final Color startTimesColor = Color.RED;
		private static final Color finishTimesColor = Color.RED;
		private static final Color durationColor = Color.RED;
		private static final Color passCountColor = Color.RED;
		private static final Color failCountColor = Color.RED;
		private static final Color passColor = Color.GREEN;
		private static final Color failColor = Color.RED;
		private static final Color skipColor = Color.ORANGE;
		private static final Color warnColor = Color.YELLOW;
		private static final Color infoColor = Color.BLUE;
		private static final Color categoryTitleColor = Color.RED;
		private static final Color categoryNameColor = Color.BLACK;
		private static final Color authorTitleColor = Color.RED;
		private static final Color authorNameColor = Color.BLACK;
		private static final Color deviceTitleColor = Color.RED;
		private static final Color deviceNameColor = Color.BLACK;
		private static final Color systemTitleColor = Color.RED;
		private static final Color systemNameColor = Color.BLACK;
		private static final Color systemValueColor = Color.BLACK;
		private static final Color exceptionTitleColor = Color.RED;
		private static final Color attributeTestStatusColor = Color.BLUE;
		private static final Color testNameColor = Color.RED;
		private static final Color testTimesColor = Color.BLUE;
		private static final Color testTimeStampColor = Color.BLACK;
		private static final Color testExceptionColor = Color.RED;
	}
}
