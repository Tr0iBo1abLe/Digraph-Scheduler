package GUI.Frame;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduler");

        initLayout();
    }

    public void initLayout() {
        try {
        	FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Layout.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            Scene scene = new Scene(personOverview);
            scene.getStylesheets().add(Main.class.getResource("view/Style.css").toExternalForm());
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
