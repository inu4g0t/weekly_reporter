package docs.generator;

import java.io.File;

import po.ReportXML;

public interface DocxGenerator {
	public void exportReportToDocx(ReportXML r, String outputPath);
	
	public void exportReportToDocx(ReportXML r, File outputFile);
}
