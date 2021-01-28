package tech.grasshopper.reporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Trial {

	private static final String CODE1 = "{\n    \"theme\": \"standard\",\n    \"encoding\": \"utf-8\n}";
	private static final String CODE2 = "{\n    \"protocol\": \"HTTPS\",\n    \"timelineEnabled\": false\n}";
	private static final String CODE5 = "{\n    \"protocol\": \"HTTPS\"\n}";
	private static final String CODE3 = "{ name: { first: \"John\", last: \"Jane\" }, age: 31, city: \"New York\" }";
	// private static final String CODE3 = "{ name: \"John\", age: 31, city: \"New
	// York\" }";
	private static final String CODE4 = "<food>\n" + "    <name>Belgian Waffles</name>\n"
			+ "    <price>$5.95</price>\n    <calories>650</calories>\n" + "</food>";

	public static void main(String[] args) throws IOException {

		ExtentReports extent = new ExtentReports();
		// extent.setAnalysisStrategy(AnalysisStrategy.TEST);
		ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark/Spark.html");
		extent.attachReporter(spark);
		ExtentPDFReporter pdf = new ExtentPDFReporter("target/Pdf/Pdf.pdf");
		extent.attachReporter(pdf);
		pdf.loadJSONConfig(new File("src/test/resources/spark-config.json"));
		// pdf.loadXMLConfig(new File("src/test/resources/spark-config.xml"));

		extent.setSystemInfo("SYS1", "system one");
		extent.setSystemInfo("SYS2", "system two");
		extent.setSystemInfo("SYS3", "system three");
		extent.setSystemInfo("SYS4", "system four");
		extent.setSystemInfo("SYS5", "system five");
		extent.setSystemInfo(
				"\tSYSLONGLONGLONGLONGLONGLONGLONGSYSSYSLONG LONGLONGLONGLONGLONGLONGSYS SYSLONGLONG SYSLONGLONGLONGLONGLONGLONGLONGSYSSYSLONG"
						+ System.getProperty("line.separator")
						+ "LONGLONG LONGLONG LONGSYS SYS LONG LONG LONG LONGLONGLONGLONGSYSSYSLONG",
				"system  LONG LONG" + System.getProperty("line.separator")
						+ "LONG LONG LONG LONGSYS LONG LONG LONG LONG LONG LONG LONG");
		extent.setSystemInfo("SYS6", "system six");
		extent.setSystemInfo("SYS7", "system seven");
		extent.setSystemInfo("SYS8", "system eight");
		extent.setSystemInfo("SYS9", "system nine");
		extent.setSystemInfo("SYS10", "system ten");

		byte[] array = Files.readAllBytes(Paths.get("src/test/resources/image.png"));
		String base64 = Base64.getEncoder().encodeToString(array);

		extent.createTest("ScreenCapture").generateLog(Status.FAIL, "Hello There")
				.addScreenCaptureFromPath("src/test/resources/image.png")
				// .addScreenCaptureFromPath("src/test/resources/image.png")
				.addScreenCaptureFromPath("src/test/resources/amur.png")
				.addScreenCaptureFromPath("src/test/resources/image.png")
				.addScreenCaptureFromPath("src/test/resources/logo.png").assignAuthor("Screen").assignAuthor("Veena")
				.assignAuthor("Neeta").addScreenCaptureFromBase64String(base64)
				// .addScreenCaptureFromPath("src/test/resources/logo.png")
				.warning(MediaEntityBuilder.createScreenCaptureFromPath("src/test/resources/amur.png").build())
				.pass(MarkupHelper.createLabel("Label Text", ExtentColor.BLUE));

		extent.createTest("Label").generateLog(Status.FAIL, MarkupHelper.createLabel("Label Text", ExtentColor.GREEN));

		extent.createTest("CodeBlock").pass("not")
				.generateLog(Status.PASS, MarkupHelper.createCodeBlock(CODE1, CODE2, CODE5, CODE2))
				.generateLog(Status.PASS, MarkupHelper.createCodeBlock(CODE4, CodeLanguage.XML))
				.generateLog(Status.PASS, MarkupHelper.createCodeBlock(CODE3, CodeLanguage.JSON));

		String[][] data1 = { { "1", "2", "coco cola coco cola coco cola coco cola coco cola", "4" },
				{ "roast roast roat", "ten", "pepsi", "4" }, { "roast roast roat", "ten", "pepsi", "4" },
				{ "roast roast roat", "ten", "pepsi", "4" }, { "roast roast roat", "ten", "pepsi", "4" } };

		extent.createTest("Table").pass("not").generateLog(Status.PASS, MarkupHelper.createTable(data1))
				.generateLog(Status.WARNING, MarkupHelper.createLabel("Label Text", ExtentColor.RED));

		String[][] data2 = { { "1", "2", "coco cola coco cola coco cola coco cola coco cola", "4" },
				{ "roast roast roat", "ten", "pepsi", "4" } };

		extent.createTest("ToTable").generateLog(Status.PASS, MarkupHelper.toTable(data2));

		Map<String, String> ullidata = new HashMap<>();
		ullidata.put("1", "One");
		ullidata.put("2", "Two");
		ullidata.put("3", "Three");
		ullidata.put("4",
				"Four  multiline stuff  multiline stuff  multiline stuff multiline stuff multiline stuff multiline stuff");
		ullidata.put("5", "Five");

		extent.createTest("Ordered List").generateLog(Status.INFO, MarkupHelper.createOrderedList(ullidata));

		extent.createTest("Unordered List").generateLog(Status.SKIP, MarkupHelper.createUnorderedList(ullidata));

		extent.createTest("Nothing else").assignAuthor("Psuedo").assignCategory("Psuedo").assignAuthor("TheAuthor")
				.assignCategory("Baby").assignCategory("baby").assignCategory("Baby");

		extent.createTest(
				"Nothing else Long long long name Long long long name Long long long name Long long long name Long long long name")
				.assignCategory("long name");

		extent.createTest("LogLevels").info("info").pass("pass").warning("warn").skip("skip").fail("fail")
				.warning(MarkupHelper.createTable(data1)).assignDevice("TheDevice").assignDevice("Mac");

		extent.createTest("ParentWithChild").info("hello").assignCategory("My  Tag").assignAuthor("TheAuthor")
				.assignAuthor("Mounish").assignDevice("Windows").assignDevice("TheDevice").info("hello").info("morning")
				.pass("night").pass("evening").warning("hello").info("bye").info("hello").info("morning")
				.createNode("Child").addScreenCaptureFromPath("src/test/resources/logo.png").pass("evening")
				.warning("hello").info("bye").assignAuthor("Barbie").createNode("Grand Child")
				.pass("This test is created as a toggle as part of a child test of 'ParentWithChild'")
				.createNode("Great Grand Child").pass("This test is created as a toggle as part of a child");

		Exception ex = null;

		try {
			rexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrex();
		} catch (Exception e) {
			ex = e;
		}

		extent.createTest("Exception! <i class='fa fa-frown-o'></i>").assignAuthor("Mounish").assignAuthor("Saha")
				.assignAuthor("TheAuthor").assignCategory("Error").createNode("Checked").fail(ex);

		extent.createTest("Tags").assignCategory("MyTag").assignDevice("TheDevice").assignAuthor("TheAuthor")
				.assignDevice("ram").assignDevice("Duck")
				.skip("The test 'Tags' was assigned by the tag <span class='badge badge-primary'>MyTag</span>");

		extent.createTest("Authors").assignAuthor("TheAuthor").assignDevice("TheDevice").assignDevice("Unix")
				.assignDevice("usb").info("This test 'Authors' was assigned by a special kind of author tag.");

		extent.createTest("Devices").assignDevice("TheDevice").assignAuthor("TheAuthor").assignDevice("Polo")
				.warning("This test 'Devices' was assigned by a special kind of devices tag.");

		extent.createTest("Exception! <i class='fa fa-frown-o'></i>").assignDevice("vox")
				.assignAuthor("NotPsuedo LONG LONG NotPsuedo LONG LONG NotPsuedo LONG LONG")
				.assignCategory("NotPsuedo LONG LONG NotPsuedo LONG LONG NotPsuedo LONG LONG")
				.assignDevice("NotPsuedo LONG LONG NotPsuedo LONG LONG NotPsuedo LONG LONG")
				.fail(new RuntimeException("A runtime exception occurred!"));

		// SIMPLIFIED
		/*
		 * extent.createTest("LogLevels").info("info").pass("pass").warning("warn").skip
		 * ("skip").fail("fail")
		 * .assignDevice("TheDevice").assignAuthor("TheAuthor").assignCategory(
		 * "TheCategory");
		 * 
		 * extent.createTest("Trial").pass("pass").assignDevice("TheDevice").
		 * assignAuthor("TheAuthor") .assignCategory("TheCategory");
		 */

		extent.flush();
	}

	private static void rexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrexrex() {
		rexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilld();
	}

	private static void rexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilldrexchilld() {
		int x = 1 / 0;
	}
}
