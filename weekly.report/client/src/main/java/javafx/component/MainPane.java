package javafx.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;

public class MainPane extends SplitPane {
	SplitPane editorPane;
	Pane controllerPane;
	HTMLEditor htmlPane;

	public MainPane() {
		initEditorPane();
		initControlPane();

		this.setOrientation(Orientation.VERTICAL);
		this.getItems().add(editorPane);
		this.getItems().add(controllerPane);
	}

	private void initEditorPane() {
		editorPane = new SplitPane();
		editorPane.setOrientation(Orientation.HORIZONTAL);
		editorPane.getItems().add(new TreePane(null));
		htmlPane = new HTMLEditor();
		editorPane.getItems().add(htmlPane);
	}

	private void initControlPane() {
		controllerPane = new HBox();
		Button saveBtn = new Button();
		saveBtn.setText("Save");
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			}
		});
		Button exportBtn = new Button();
		exportBtn.setText("Export");
		exportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			}
		});

		controllerPane.getChildren().add(saveBtn);
		controllerPane.getChildren().add(exportBtn);
	}

}
