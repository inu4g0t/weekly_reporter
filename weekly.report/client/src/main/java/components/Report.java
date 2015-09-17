package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import po.ReportXML;

public class Report extends DefaultTreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -329937835048256249L;

	private ReportXML reportXML;
	private JAXBContext context = null;

	public ReportXML getReportXML() {
		try {
			context = JAXBContext.newInstance(ReportXML.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reportXML;
	}

	public void setReportXML(ReportXML reportXML) {
		this.reportXML = reportXML;
	}

	public Report(TreeNode root, ReportXML r) {
		super(root);
		// TODO Auto-generated constructor stub
		reportXML = r;
	}

	public void loadReport(File file) {
		try {
			Unmarshaller um = context.createUnmarshaller();
			reportXML = (ReportXML) um.unmarshal(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveReport(File outputFile) {
		try {
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(reportXML, outputFile);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}

	}

}
