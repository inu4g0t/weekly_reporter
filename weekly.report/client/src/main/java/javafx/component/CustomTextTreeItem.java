package javafx.component;

import po.NodeXML;
import po.adapter.NodeXMLAdpterInterface;
import javafx.scene.control.TreeItem;

public class CustomTextTreeItem extends TreeItem<String> implements
		NodeXMLAdpterInterface {
	private String htmlText;

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public CustomTextTreeItem() {
		super();
	}

	public CustomTextTreeItem(String s) {
		super(s);
	}

	public CustomTextTreeItem(NodeXML n) {
		super();
		parseNodeXML(n);
	}
	
	@Override
	public void parseNodeXML(NodeXML n) {
		setValue(n.getText());;
		htmlText = n.getHtmlText();
		for (NodeXML c : n.getChildren()) {
			this.getChildren().add(new CustomTextTreeItem(c));
		}
	}

	@Override
	public NodeXML exportNodeXML() {
		// TODO Auto-generated method stub
		NodeXML root = new NodeXML();
		root.setText(getValue());
		root.setHtmlText(getHtmlText());
		for (TreeItem<String> c : this.getChildren()) {
			root.addChild(((CustomTextTreeItem)c).exportNodeXML());
		}
		return root;
	}

}
