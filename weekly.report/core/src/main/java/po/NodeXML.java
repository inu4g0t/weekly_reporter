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
	
	public NodeXML(Object o) {
		children = new ArrayList<NodeXML>();
		userObject = o;
		level = 0;
	}
	
	private Object userObject;

	private NodeXML parent;

	private List<NodeXML> children;
	
	private int level;

	private String text;
	
	@XmlElement(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@XmlElement(name = "value")
	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object object) {
		this.userObject = object;
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
