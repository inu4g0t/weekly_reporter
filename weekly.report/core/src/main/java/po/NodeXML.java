package po;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to warp node
 * 
 * @author You Li
 */

@SuppressWarnings("restriction")
@XmlRootElement(name = "Node")
public class NodeXML {
	
	public NodeXML() {
		children = new ArrayList<NodeXML>();
		level = 0;
	}
	
	public NodeXML(String o) {
		children = new ArrayList<NodeXML>();
		text = o;
		level = 0;
	}
	
	private NodeXML parent;

	private List<NodeXML> children;
	
	private int level;
	
	private String text;
	
	private String htmlText;
	
	@XmlElement(name = "htmlText")
	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String text) {
		this.htmlText = text;
	}

	@XmlElement(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String object) {
		this.text = object;
	}

	
	public NodeXML getParent() {
		return parent;
	}

	public void setParent(NodeXML parent) {
		this.parent = parent;
	}

	@XmlElement(name = "child")
	public List<NodeXML> getChildren() {
		return children;
	}

	public void setChildren(List<NodeXML> children) {
		this.children = children;
	}
	
	public void addChild(NodeXML c) {
		children.add(c);
		c.setLevel(this.getLevel() + 1);
		// c.setParent(this);
	}

	@XmlElement(name = "level")
	public int getLevel() {
		// TODO Auto-generated method stub
		return level;
	}
	
	public void setLevel(int l) {
		// TODO Auto-generated method stub
		level = l;
	}
	
	
}
