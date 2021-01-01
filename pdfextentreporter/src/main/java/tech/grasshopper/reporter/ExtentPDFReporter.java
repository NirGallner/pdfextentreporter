package tech.grasshopper.reporter;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.observer.ReportObserver;
import com.aventstack.extentreports.observer.entity.ReportEntity;
import com.aventstack.extentreports.reporter.AbstractFileReporter;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ExtentPDFReporter extends AbstractFileReporter implements ReportObserver<ReportEntity> {
	
	private static final Logger logger = Logger.getLogger(ExtentPDFReporter.class.getName());
	private static final String REPORTER_NAME = "pdf";
	private static final String FILE_NAME = "Index.pdf";

	private Disposable disposable;
	private Report report;

	public ExtentPDFReporter(String path) {
		super(new File(path));
	}

	public ExtentPDFReporter(File f) {
		super(f);
	}

	public Observer<ReportEntity> getReportObserver() {
		return new Observer<ReportEntity>() {

			public void onSubscribe(Disposable d) {
				start(d);
			}

			public void onNext(ReportEntity value) {
				flush(value);
			}

			public void onError(Throwable e) {
			}

			public void onComplete() {
			}
		};
	}

	private void start(Disposable d) {
		disposable = d;
	}

	private void flush(ReportEntity value) {
		try {
			report = value.getReport();
			final String filePath = getFileNameAsExt(FILE_NAME, new String[] { ".pdf" });

			/*
			 * ExtentPDFReportDataGenerator generator = new ExtentPDFReportDataGenerator();
			 * ReportData reportData = generator.generateReportData(report);
			 * 
			 * PDFCucumberReport pdfCucumberReport = new PDFCucumberReport(reportData, new
			 * File(filePath)); pdfCucumberReport.createReport();
			 */
			ReportGenerator reportGenerator =  new ReportGenerator(report, new File(filePath));
			reportGenerator.generate();
		} catch (Exception e) {
			disposable.dispose();
			logger.log(Level.SEVERE, "An exception occurred", e);
		}
	}
}
