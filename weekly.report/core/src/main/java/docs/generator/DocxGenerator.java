package docs.generator;

import po.Report;

public interface DocxGenerator {
	public void exportReportToDocx(Report r, String outputPath);
}
