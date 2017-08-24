package GUI.Frame;

import java.io.IOException;

import GUI.Events.GraphLoaderThread;
import GUI.Frame.view.Controller;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * This is an application class to show all information about our program
 * 
 * @author Vincent
 * @see DataVisualization
 */
public class GUIEntry extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Scheduler");

		initLayout();

//        createAndSetSwingContent(Controller.swingNode);

//        SwingUtilities.invokeLater(() -> {
//            System.out.println("test run");
//            Controller.swingNode.setContent(Controller.viewPanel);
//        });
        GraphLoaderThread graphLoaderThread = new GraphLoaderThread();
        graphLoaderThread.start();

        System.out.println("get here");

        Controller.sysInfoMonitoringThread.start();
	}

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("test run");
                swingNode.setContent(Controller.viewPanel);
            }
        });
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

	public static void main(String[] args) {
		launch(args);
	}
}
