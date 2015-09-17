package javafx.component;

import java.io.File;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MainPane extends SplitPane {
	private static final String REPORT_XML = "report.xml";
	Pane controllerPane;
	EditorPane editorPane;
	final FileChooser fileChooser = new FileChooser();
	Stage stage;

	public MainPane(Stage s) {
		stage = s;
		initEditorPane();
		initControlPane();

		this.setPrefSize(800, 600);
		this.setDividerPosition(0, 0.95);
		this.setOrientation(Orientation.VERTICAL);
		this.getItems().add(editorPane);
		this.getItems().add(controllerPane);
	}

	private void initEditorPane() {
		editorPane = new EditorPane();
	}

	private void initControlPane() {
		controllerPane = new HBox();
		Button saveBtn = new Button();
		saveBtn.setText("Save");
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				editorPane.exportReport(new File(REPORT_XML));
			}
		});
		Button exportBtn = new Button();
		exportBtn.setText("Export");
		exportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File file = fileChooser.showSaveDialog(stage);
				if (file != null) {
					editorPane.exportReportToDoc(file);
					Stage dialogStage = new Stage();
					dialogStage.initModality(Modality.WINDOW_MODAL);
					dialogStage.setScene(new Scene(VBoxBuilder.create()
							.children(new Text("Export Finished"))
							.alignment(Pos.CENTER).padding(new Insets(5))
							.build()));
					dialogStage.showAndWait();
				}
			}
		});

		controllerPane.getChildren().add(saveBtn);
		controllerPane.getChildren().add(exportBtn);
	}

}
