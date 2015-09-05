package components;

import javax.swing.tree.DefaultMutableTreeNode;

public class TextMutableTreeNode extends DefaultMutableTreeNode {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String text;
	private boolean essential;

	public TextMutableTreeNode(String string) {
		// TODO Auto-generated constructor stub
		super(string);
		essential = false;
	}

	public TextMutableTreeNode(Object child) {
		// TODO Auto-generated constructor stub
		super(child);
		essential = false;
	}
	
	public TextMutableTreeNode(String string, boolean e) {
		// TODO Auto-generated constructor stub
		super(string);
		essential = e;
	}
	
	public TextMutableTreeNode(Object child, boolean e) {
		// TODO Auto-generated constructor stub
		super(child);
		essential = e;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
