package javafx.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TreePane extends SplitPane {
	ScrollPane scrollPane;
	TreeView<String> treeView;
	Pane controllerPane;
	CustomTextTreeItem rootNode;
	
	public TreePane(CustomTextTreeItem root){
		rootNode = root;
		initTreeView();
		initControlPane();
		
		this.setOrientation(Orientation.VERTICAL);
		this.getItems().add(scrollPane);
		this.getItems().add(controllerPane);
		
	}
	
	private void initTreeView() {
		//treeView = new TreeView<String>(rootNode);
		treeView = new TreeView<String>(new TreeItem("root"));
		scrollPane = new ScrollPane();
		scrollPane.setContent(treeView);
	}
	
	private void initControlPane(){
		controllerPane = new VBox();
		Button upButton = new Button();
		upButton.setText("Move Up");
		upButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Move up");
			}
		});
		Button downButton = new Button();
		downButton.setText("Move Down");
		downButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Move down");
			}
		});
		controllerPane.getChildren().addAll(upButton,downButton);
	}
	
}
