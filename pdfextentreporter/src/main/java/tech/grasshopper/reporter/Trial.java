package tech.grasshopper.reporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import tech.grasshopper.reporter.structure.TableCreator;

public class Trial {

	private static final String CODE1 = "{\n    \"theme\": \"standard\",\n    \"encoding\": \"utf-8\n}";
	private static final String CODE2 = "{\n    \"protocol\": \"HTTPS\",\n    \"timelineEnabled\": false\n}";

	public static void main(String[] args) throws IOException {

		ExtentReports extent = new ExtentReports();
		// extent.setAnalysisStrategy(AnalysisStrategy.TEST);
		ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark/Spark.html");
		extent.attachReporter(spark);
		ExtentPDFReporter pdf = new ExtentPDFReporter("target/Pdf/Pdf.pdf");
		extent.attachReporter(pdf);

		extent.setSystemInfo("SYS1", "system one");
		extent.setSystemInfo("SYS2", "system two");
		extent.setSystemInfo("SYS3", "system three");
		extent.setSystemInfo("SYS4", "system four");
		extent.setSystemInfo("SYS5", "system five");
		extent.setSystemInfo(
				"SYSLONGLONGLONGLONGLONGLONGLONGSYSSYSLONG LONGLONGLONGLONGLONGLONGSYS SYSLONGLONG SYSLONGLONGLONGLONGLONGLONGLONGSYSSYSLONG"
						+ System.getProperty("line.separator")
						+ "LONGLONGLONGLONGLONGSYS SYSLONGLONGLONGLONGLONGLONGLONGSYSSYSLONG",
				"system  LONG LONG" + System.getProperty("line.separator")
						+ "LONG LONG LONG LONGSYS LONG LONG LONG LONG LONG LONG LONG");
		extent.setSystemInfo("SYS6", "system six");
		extent.setSystemInfo("SYS7", "system seven");
		extent.setSystemInfo("SYS8", "system eight");
		extent.setSystemInfo("SYS9", "system nine");
		extent.setSystemInfo("SYS10", "system ten");

		/*
		 * extent.setSystemInfo("SYS11", "system ten"); extent.setSystemInfo("SYS12",
		 * "system ten"); extent.setSystemInfo("SYS13", "system ten");
		 * extent.setSystemInfo("SYS14", "system ten"); extent.setSystemInfo("SYS16",
		 * "system ten"); extent.setSystemInfo("SYS17", "system ten");
		 * extent.setSystemInfo("SYS18", "system ten"); extent.setSystemInfo("SYS19",
		 * "system ten"); extent.setSystemInfo("SYS20", "system ten");
		 * extent.setSystemInfo("SYS21", "system ten"); extent.setSystemInfo("SYS22",
		 * "system ten"); extent.setSystemInfo("SYS23", "system ten");
		 * extent.setSystemInfo("SYS24", "system ten"); extent.setSystemInfo("SYS25",
		 * "system ten"); extent.setSystemInfo("SYS26", "system ten");
		 * extent.setSystemInfo("SYS27", "system ten"); extent.setSystemInfo("SYS28",
		 * "system ten"); extent.setSystemInfo("SYS29", "system ten");
		 * extent.setSystemInfo("SYS30", "system ten"); extent.setSystemInfo("SYS31",
		 * "system ten"); extent.setSystemInfo("SYS32", "system ten");
		 * extent.setSystemInfo("SYS33", "system ten"); extent.setSystemInfo("SYS34",
		 * "system ten"); extent.setSystemInfo("SYS35", "system ten");
		 * extent.setSystemInfo("SYS36", "system ten"); extent.setSystemInfo("SYS37",
		 * "system ten");
		 */

		byte[] array = Files.readAllBytes(Paths.get("src/main/resources/image.png"));
		String base64 = Base64.getEncoder().encodeToString(array);

		extent.createTest("ScreenCapture").addScreenCaptureFromPath("src/main/resources/image.png")
				.addScreenCaptureFromPath("src/main/resources/logo.png").assignAuthor("Screen").assignAuthor("Veena")
				.assignAuthor("Neeta").addScreenCaptureFromBase64String(base64)
				.pass(MediaEntityBuilder.createScreenCaptureFromPath("src/main/resources/amur.png").build());

		extent.createTest("CodeBlock").pass("not").generateLog(Status.PASS,
				MarkupHelper.createCodeBlock(CODE1, CODE2, CODE1));

		String[][] data = { { "h", "d" }, { "r", "t" } };

		extent.createTest("OtherBlock").pass("not").generateLog(Status.PASS, MarkupHelper.createTable(data));

		extent.createTest("Nothing else").assignAuthor("Psuedo").assignCategory("Psuedo").assignAuthor("TheAuthor")
				.assignCategory("Baby");
		extent.createTest("LogLevels").info("info").pass("pass").warning("warn").skip("skip").fail("fail")
				.assignDevice("TheDevice").assignDevice("Mac");

		extent.createTest("ParentWithChild").info("hello").assignCategory("My  Tag").assignAuthor("TheAuthor")
				.assignAuthor("Mounish").assignDevice("Windows").assignDevice("TheDevice").info("hello").info("morning")
				.pass("night").pass("evening").warning("hello").info("bye").info("hello").info("morning")
				.createNode("Child").addScreenCaptureFromPath("src/main/resources/logo.png").pass("evening")
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
