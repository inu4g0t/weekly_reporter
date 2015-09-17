package javafx.client;

import javafx.application.Application;
import javafx.component.MainPane;
import javafx.component.TreePane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WeeklyReporter extends Application {
	@Override
	public void start(Stage primaryStage) {

		MainPane treePane = new MainPane();;

		Scene scene = new Scene(treePane, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
