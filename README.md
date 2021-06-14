Refer to this [article](https://grasshopper.tech/2555/) for more details.

[Sample Report](https://github.com/grasshopper7/pdf-extent-report/blob/master/pdf-extent-report/reports/TESTSimplePDFReport.pdf)
[BDD Sample Report](https://github.com/grasshopper7/pdf-extent-report/blob/master/pdf-extent-report/reports/BDDPDFReport.pdf)

The report contains four sections – summary section with a dashboard and attribute summary, test details section, attribute details section and media section. There is a one to one mapping between the Spark and PDF reports. The summary section maps to the dashboard tab of the Spark report. The test details section maps to the test tab of the Spark report. The attribute details section maps to the category, author, device and exception tabs.

The PDF report is a single file with the medias embedded in it. This allows it to be shared easily without need of zipping and extracting contents. This convenience comes with the issue of file size, if report contains numerous medias. This report currently does not support base64 media.

The report uses MIT or Apache licensed code for PDF display which removes any licensing issues. Following are the PDF related dependencies – PDFBox, easytable and pdfbox-layout. The report uses the open source Liberation Sans font family.

### Dashboard

![sample](https://raw.githubusercontent.com/grasshopper7/pdf-extent-report/master/pdf-extent-report/images/dashboard_default.png)

### Attribute Summary
![sample](https://raw.githubusercontent.com/grasshopper7/pdf-extent-report/master/pdf-extent-report/images/attribute_summary.png)

### Tests Summary
![sample](https://raw.githubusercontent.com/grasshopper7/pdf-extent-report/master/pdf-extent-report/images/test_details.png)

### Attribute Details
![sample](https://raw.githubusercontent.com/grasshopper7/pdf-extent-report/master/pdf-extent-report/images/attribute_details.png)

### Expanded Medias
![sample](https://raw.githubusercontent.com/grasshopper7/pdf-extent-report/master/pdf-extent-report/images/log_media_click_expanded.png)
