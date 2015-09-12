package po;

import java.io.Serializable;
import java.sql.Time;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import components.TextMutableTreeNode;

public class Report extends DefaultTreeModel implements Serializable {
	
	public Report(TreeNode root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	private Time time;
	private String name;
	private String reportString;

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
	
	public String getTitle() {
		if (!(root instanceof TextMutableTreeNode)) {
			return "Title";
		}
		TextMutableTreeNode rootNode = (TextMutableTreeNode)root;
		return (String)rootNode.getUserObject();
		
	}
}
