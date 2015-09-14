/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package components;

/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.math.BigInteger;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.docx4j.wml.P;
import org.docx4j.wml.PPrBase.NumPr;
import org.docx4j.wml.PPrBase.NumPr.Ilvl;
import org.docx4j.wml.PPrBase.NumPr.NumId;

import javafx.scene.Scene;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import po.Report;
import docs.generator.Docx4jDocxGenerator;
import docs.helper.TemplateElementReader;
import docs.template.SimpleDocTemplate;

public class DynamicTree extends JPanel {
	
	private static String IMPORTANT_CHANGSE = "重要项目产出";
	private static String OTHER_PROJECTS = "其他项目";
	
	protected TextMutableTreeNode rootNode;
	protected TextMutableTreeNode lastNode;
	protected Report report;
	protected JTree tree;
	// protected JEditorPane htmlPane;
	protected HTMLEditor htmlPane;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	@SuppressWarnings("restriction")
	public DynamicTree() {
		super(new GridLayout(1, 0));

		rootNode = new TextMutableTreeNode("运营产品研发团队周报");
		lastNode = null;
		report = new Report(rootNode);

		TextMutableTreeNode ic = insertEssentialChildToParent(null,
				IMPORTANT_CHANGSE);
		TextMutableTreeNode op = insertEssentialChildToParent(null,
				OTHER_PROJECTS);
		// rootNode = new TextMutableTreeNode("Root Node");

		// treeModel = new Report(rootNode);
		report.addTreeModelListener(new MyTreeModelListener());
		tree = new JTree(report);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new MyTreeSelectionListener());
		tree.setShowsRootHandles(true);

		JScrollPane treeView = new JScrollPane(tree);

		// htmlPane = new JEditorPane();
		// htmlPane.setEditable(false);

		final JFXPanel fxPanel = new JFXPanel();
		Platform.runLater(new Runnable() {
			public void run() {
				// javaFX operations should go here
				htmlPane = new HTMLEditor();
				Scene scene = new Scene(htmlPane);
				fxPanel.setScene(scene);
			}
		});

		JScrollPane htmlView = new JScrollPane(fxPanel);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(treeView);
		splitPane.setRightComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(200);
		splitPane.setPreferredSize(new Dimension(800, 600));

		add(splitPane);

	}

	/** Remove all nodes except the root node. */
	public void clear() {
		rootNode.removeAllChildren();
		report.reload();
	}

	/** Remove the currently selected node. */
	public void removeCurrentNode() {
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			TextMutableTreeNode currentNode = (TextMutableTreeNode) (currentSelection
					.getLastPathComponent());
			MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
			if (parent != null) {
				report.removeNodeFromParent(currentNode);
				return;
			}
		}

		// Either there was no selection, or the root was selected.
		toolkit.beep();
	}

	/** Add child to the currently selected node. */
	public TextMutableTreeNode addObject(Object child) {
		TextMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (TextMutableTreeNode) (parentPath
					.getLastPathComponent());
		}

		return addObject(parentNode, child, true, false);
	}

	/** Save the information into current node */

	public TextMutableTreeNode addObject(TextMutableTreeNode parent,
			Object child) {
		return addObject(parent, child, false, false);
	}

	public TextMutableTreeNode addEssentialObject(TextMutableTreeNode parent,
			Object child) {
		return addObject(parent, child, false, true);
	}

	public TextMutableTreeNode addObject(TextMutableTreeNode parent,
			Object child, boolean shouldBeVisible, boolean essential) {
		TextMutableTreeNode childNode = new TextMutableTreeNode(child);

		if (parent == null) {
			parent = rootNode;
		}

		// It is key to invoke this on the TreeModel, and NOT
		// TextMutableTreeNode
		report.insertNodeInto(childNode, parent, parent.getChildCount());

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	class MyTreeModelListener implements TreeModelListener {
		public void treeNodesChanged(TreeModelEvent e) {
			TextMutableTreeNode node;
			node = (TextMutableTreeNode) (e.getTreePath()
					.getLastPathComponent());

			/*
			 * If the event lists children, then the changed node is the child
			 * of the node we've already gotten. Otherwise, the changed node and
			 * the specified node are the same.
			 */

			int index = e.getChildIndices()[0];
			node = (TextMutableTreeNode) (node.getChildAt(index));

			// System.out.println("The user has finished editing the node.");
			// System.out.println("New value: " + node.getUserObject());
		}

		public void treeNodesInserted(TreeModelEvent e) {
		}

		public void treeNodesRemoved(TreeModelEvent e) {
		}

		public void treeStructureChanged(TreeModelEvent e) {
		}
	}

	class MyTreeSelectionListener implements TreeSelectionListener {

		@SuppressWarnings("restriction")
		public void valueChanged(TreeSelectionEvent arg0) {
			// TODO Auto-generated method stub
			TextMutableTreeNode node = null;
			TreePath parentPath = tree.getSelectionPath();

			if (lastNode == null) {
				lastNode = rootNode;
			}
			if (htmlPane.getHtmlText() != null && htmlPane.getHtmlText() != "") {
				saveText(lastNode, htmlPane.getHtmlText());
			}
			
			if (parentPath == null) {
				node = rootNode;
			} else {
				node = (TextMutableTreeNode) (parentPath.getLastPathComponent());
			}
			lastNode = node;
			String node_text = node.getText();
			if (node_text == null) {
				node_text = "";
			}
			final String node_text_final = node_text;
			Platform.runLater(new Runnable() {
				public void run() {
					htmlPane.setHtmlText(node_text_final);
				}
			});
			// htmlPane.setHtmlText(node_text);
			// htmlPane.setEditable(true);
		}

	}

	public TextMutableTreeNode saveText() {

		TextMutableTreeNode node = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			node = rootNode;
		} else {
			node = (TextMutableTreeNode) (parentPath.getLastPathComponent());
		}
		return saveText(node, htmlPane.getHtmlText());
	}

	protected TextMutableTreeNode saveText(TextMutableTreeNode node, String text) {
		node.setText(text);
		return node;
	}

	public TextMutableTreeNode restoreText() {
		// TODO Auto-generated method stub
		TextMutableTreeNode node = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			node = rootNode;
		} else {
			node = (TextMutableTreeNode) (parentPath.getLastPathComponent());
		}
		return restoreText(node);
	}

	protected TextMutableTreeNode restoreText(TextMutableTreeNode node) {
		//htmlPane.setHtmlText(node.getText());
		String node_text = node.getText();
		if (node_text == null) {
			node_text = "";
		}
		final String node_text_final = node_text;
		Platform.runLater(new Runnable() {
			public void run() {
				htmlPane.setHtmlText(node_text_final);
			}
		});
		return node;
	}

	public void export() {
		
		Docx4jDocxGenerator docGenerator = new Docx4jDocxGenerator();
		docGenerator.exportReportToDocx(report, "output.docx");
	}
	
	private TextMutableTreeNode insertEssentialChildToParent(
			TextMutableTreeNode parent, Object child) {
		TextMutableTreeNode childNode = new TextMutableTreeNode(child, true);

		if (parent == null) {
			parent = rootNode;
		}

		// It is key to invoke this on the TreeModel, and NOT
		// TextMutableTreeNode
		report.insertNodeInto(childNode, parent, parent.getChildCount());

		return childNode;
	}
}
