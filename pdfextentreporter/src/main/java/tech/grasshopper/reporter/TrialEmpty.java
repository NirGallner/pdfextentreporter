package tech.grasshopper.reporter;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
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

		extent.createTest("Hello").pass("Single test").assignCategory("Cate");

		extent.flush();
	}

}
