package javafx.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.component.MainPane;
import javafx.component.EditorPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WeeklyReporter extends Application {
	@Override
	public void start(Stage primaryStage) {

		MainPane treePane = new MainPane(primaryStage);;

		Scene scene = new Scene(treePane, 800, 600);

		primaryStage.setTitle("逐风者周报之怒");
		Image icon;
		try {
			icon = new Image(new FileInputStream("icon.jpg"));
			primaryStage.getIcons().add(icon);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
