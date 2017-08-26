package GUI.Frame;

import java.io.IOException;

import GUI.Frame.view.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This is an application class to fire the event dispatch thread which loads all the gui component using Controller specified in FXML
 * This is the standard entry point of JavaFX framework.
 * 
 * @author Vincent Chen, slightly modified by Mason Shi
 * @see DataVisualization
 */
public class GUIEntry extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Scheduler");

		initLayout();

        Controller.sysInfoMonitoringThread.start();
	}

	/**
	 * Load Layout.fxml to GUIEntry class and set Style.css to scene for using later
	 */
	private void initLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUIEntry.class.getResource("view/Layout.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			Scene scene = new Scene(personOverview);
			scene.getStylesheets().add(GUIEntry.class.getResource("view/Style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
