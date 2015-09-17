package javafx.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import docs.generator.Docx4jDocxGenerator;
import po.NodeXML;
import po.ReportXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.util.Callback;

public class EditorPane extends SplitPane {

	private JAXBContext context = null;

	ScrollPane scrollPane;
	TreeView<String> treeView;
	Pane controllerPane;
	CustomTextTreeItem rootNode;
	CustomTextTreeItem lastNode;
	ReportXML reportXML;

	SplitPane treePane;
	HTMLEditor htmlPane;

	public EditorPane() {
		try {
			context = JAXBContext.newInstance(ReportXML.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastNode = null;
		rootNode = new CustomTextTreeItem("root");
		reportXML = new ReportXML();
		initTreePane();

		this.setOrientation(Orientation.HORIZONTAL);
		this.setDividerPosition(0, 0.4);
		this.getItems().add(treePane);
		htmlPane = new HTMLEditor();
		htmlPane.setPrefSize(480, 550);
		this.getItems().add(htmlPane);
		this.setPrefSize(800, 550);

	}

	public EditorPane(File reportXMLFile) {
		try {
			context = JAXBContext.newInstance(ReportXML.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastNode = null;
		this.loadReport(reportXMLFile);
		initTreePane();

		this.setOrientation(Orientation.HORIZONTAL);
		this.setDividerPosition(0, 0.4);
		this.getItems().add(treePane);
		htmlPane = new HTMLEditor();
		htmlPane.setPrefSize(480, 550);
		this.getItems().add(htmlPane);
		this.setPrefSize(800, 550);

	}

	private void initTreePane() {
		initTreeView();
		initControlPane();

		treePane = new SplitPane();
		treePane.setDividerPosition(0, 0.95);
		treePane.setOrientation(Orientation.VERTICAL);
		treePane.getItems().add(scrollPane);
		treePane.getItems().add(controllerPane);
	}

	private void initTreeView() {
		treeView = new TreeView<String>(rootNode);
		treeView.setPrefSize(300, 550);
		scrollPane = new ScrollPane();
		scrollPane.setContent(treeView);
		treeView.setEditable(true);
		treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {

			@Override
			public TreeCell<String> call(TreeView<String> arg0) {
				// TODO Auto-generated method stub
				TextFieldTreeCellImpl treeCell = new TextFieldTreeCellImpl();

				treeCell.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						if (lastNode != null) {
							lastNode.setHtmlText(htmlPane.getHtmlText());
						}
						saveHtmlText();
					}

				});
				;

				treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// TODO Auto-generated method stub
						Dragboard db = treeView
								.startDragAndDrop(TransferMode.MOVE);
						ClipboardContent content = new ClipboardContent();
						content.putString(treeCell.getId());
						db.setContent(content);
					}

				});
				treeCell.setOnDragOver(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						/* data is dragged over the target */
						// System.out.println("onDragOver");

						/*
						 * accept it only if it is not dragged from the same
						 * node and if it has a string data
						 */
						if (event.getGestureSource() != treeCell
								&& event.getDragboard().hasString()) {
							/* allow for moving */
							event.acceptTransferModes(TransferMode.MOVE);
						}

						event.consume();
					}
				});

				treeCell.setOnDragDropped(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						/* data dropped */
						/*
						 * if there is a string data on dragboard, read it and
						 * use it
						 */
						Dragboard db = event.getDragboard();
						boolean success = false;
						if (db.hasString()) {
							TextFieldTreeCellImpl treeCellOld = (TextFieldTreeCellImpl) treeView
									.lookup("#" + db.getString());
							// System.out.println("from:" +
							// ((TextFieldTreeCellImpl)db.getContent(treeFormat)).getText());
							TreeItem oldTreeItem = treeCellOld.getTreeItem();
							oldTreeItem.getParent().getChildren()
									.remove(oldTreeItem);
							treeCell.getTreeItem().getChildren()
									.add(0, oldTreeItem);
							// treeCellOld.getTreeItem().getParent().getChildren().remove(treeCellOld.getTreeItem());
							success = true;
						}
						/*
						 * let the source know whether the string was
						 * successfully transferred and used
						 */
						event.setDropCompleted(success);

						event.consume();
					}
				});

				return treeCell;

			}

		});
	}

	private void initControlPane() {
		controllerPane = new HBox();
		Button upButton = new Button();
		upButton.setText("Move Up");
		upButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				moveNode(-1);
			}
		});
		Button downButton = new Button();
		downButton.setText("Move Down");
		downButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				moveNode(1);
			}
		});
		Button addButton = new Button();
		addButton.setText("Add");
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addNode();
			}
		});
		Button removeButton = new Button();
		removeButton.setText("Remove");
		removeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				removeNode();
			}
		});
		Button clearButton = new Button();
		clearButton.setText("Clear");
		clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				clearNodes();
			}
		});
		controllerPane.getChildren().addAll(addButton, removeButton,
				clearButton, upButton, downButton);
	}

	private void saveHtmlText() {
		CustomTextTreeItem tr = (CustomTextTreeItem) treeView
				.getSelectionModel().getSelectedItem();
		if (tr != null) {
			htmlPane.setHtmlText(tr.getHtmlText());
		}
		lastNode = tr;
	}

	protected void moveNode(int moveStep) {
		CustomTextTreeItem tr = (CustomTextTreeItem) treeView
				.getSelectionModel().getSelectedItem();
		int current = treeView.getSelectionModel().getSelectedIndex();
		int dest = current + moveStep - 1;
		if (dest < 0) {
			dest = 0;
		}
		if (dest >= tr.getParent().getChildren().size()) {
			dest = tr.getParent().getChildren().size() - 1;
		}
		if (tr != null && tr != rootNode) {
			CustomTextTreeItem trParent = (CustomTextTreeItem) tr.getParent();
			trParent.getChildren().remove(tr);
			trParent.getChildren().add(dest, tr);
			treeView.getSelectionModel().select(tr);
		}

	}

	protected void clearNodes() {
		// TODO Auto-generated method stub
		rootNode.getChildren().removeAll(rootNode.getChildren());
	}

	protected void removeNode() {
		CustomTextTreeItem tr = (CustomTextTreeItem) treeView
				.getSelectionModel().getSelectedItem();
		if (tr != null && tr != rootNode) {
			tr.getParent().getChildren().remove(tr);
		}

	}

	private void addNode() {
		CustomTextTreeItem tr = (CustomTextTreeItem) treeView
				.getSelectionModel().getSelectedItem();
		if (tr != null) {
			tr.getChildren().add(
					new CustomTextTreeItem("TestNode "
							+ treeView.getChildrenUnmodifiable().size()));
		}
		tr.setExpanded(true);

	}

	public void loadReport(File inputFile) {
		try {
			Unmarshaller um = context.createUnmarshaller();
			reportXML = (ReportXML) um.unmarshal(new FileReader(inputFile));
			rootNode = new CustomTextTreeItem(reportXML.getRootNode());
		} catch (FileNotFoundException e) {
			reportXML = new ReportXML();
			rootNode = new CustomTextTreeItem("root");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void exportReport(File outputFile) {
		saveHtmlText();
		NodeXML n = rootNode.exportNodeXML();
		reportXML.setRootNode(n);
		try {
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(reportXML, outputFile);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}

	}

	public void exportReportToDoc(File file) {
		saveHtmlText();
		NodeXML n = rootNode.exportNodeXML();
		reportXML.setRootNode(n);
		Docx4jDocxGenerator docGenerator = new Docx4jDocxGenerator();
		docGenerator.exportReportToDocx(reportXML, file);
	}

}
