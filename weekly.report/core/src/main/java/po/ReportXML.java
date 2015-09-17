package po;

import java.sql.Time;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to warp report. This is used for saving the
 * report to XML.
 * 
 * @author You Li
 */

@XmlRootElement(name = "report")
public class ReportXML {

	private NodeXML rootNode;	
	private Time time;
	private String name;
	private String reportString;

	public NodeXML getRootNode() {
		return rootNode;
	}

	public void setRootNode(NodeXML rootNode) {
		this.rootNode = rootNode;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReportString() {
		return reportString;
	}

	public void setReportString(String reportString) {
		this.reportString = reportString;
	}
}
