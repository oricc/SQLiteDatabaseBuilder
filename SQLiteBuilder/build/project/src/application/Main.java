package application;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			// Create the FXMLLoader
			FXMLLoader loader = new FXMLLoader();
			// Path to the FXML File
			String fxmlDocPath = "src/application/TableBuilder.fxml";
			FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

			// Create the Pane and all Details
			BorderPane root = (BorderPane) loader.load(fxmlStream);

			// Create the Scene
			Scene scene = new Scene(root);
			// Set the Scene to the Stage
			stage.setScene(scene);
			// Set the Title to the Stage
			stage.setTitle("Android SQlite database builder");
			// Display the Stage
			stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
