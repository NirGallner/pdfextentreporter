package tech.grasshopper.reporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TrialEmpty {

	public static void main(String[] args) throws IOException {

		ExtentReports extent = new ExtentReports();
		// extent.setAnalysisStrategy(AnalysisStrategy.BDD);
		ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark/SparkEmpty.html");
		extent.attachReporter(spark);
		ExtentPDFReporter pdf = new ExtentPDFReporter("target/Pdf/PdfEmpty.pdf");
		extent.attachReporter(pdf);
		// pdf.loadJSONConfig(new File("src/main/resources/spark-config.json"));
		// pdf.loadXMLConfig(new File("src/main/resources/spark-config.xml"));

		extent.flush();
	}

}
