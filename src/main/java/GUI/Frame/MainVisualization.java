package GUI.Frame;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainVisualization extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduler");

        initRootLayout();
    }

    public void initRootLayout() {
        try {
        	FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainVisualization.class.getResource("view/MainGUI.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            Scene scene = new Scene(personOverview);
            scene.getStylesheets().add(MyButton.class.getResource("view/jfoenix-components.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
