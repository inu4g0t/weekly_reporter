package docs.generator;

import java.io.File;

import po.Report;

public interface DocxGenerator {
	public void exportReportToDocx(Report r, String outputPath);
	
	public void exportReportToDocx(Report r, File outputFile);
}
