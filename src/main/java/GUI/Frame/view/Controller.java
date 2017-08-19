package GUI.Frame.view;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import com.jfoenix.controls.JFXListView;

import GUI.Frame.DataVisualization;
import GUI.Frame.MyButton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;  

public class Controller {
	
	@FXML  
	private AnchorPane dataPane;

	@FXML  
	private HBox Buttons;
	
	@FXML  
	private AnchorPane ListPane;
	
	public Controller() {
    }

    @FXML
    private void initialize() {
    	DataVisualization data=new DataVisualization();
    	dataPane.getChildren().add(data);
    	AnchorPane.setLeftAnchor(data, 0d);
    	AnchorPane.setRightAnchor(data, 0d);
    	AnchorPane.setTopAnchor(data, 0d);
    	AnchorPane.setBottomAnchor(data, 0d);
    	
    	BuildListView();
    	
    	Buttons.setBackground(new Background(new BackgroundFill(Color.rgb(40,45,50), CornerRadii.EMPTY, Insets.EMPTY)));
    	Buttons.getChildren().add(new MyButton("START"));
    	Buttons.getChildren().add(new MyButton("PAUSE"));
    	Buttons.getChildren().add(new MyButton("STOP"));
    	

    }
    
    private void BuildListView() {
    	JFXListView<Label> list = new JFXListView<>();
        list.getItems().add(new Label("Graph"));
        list.getItems().add(new Label("Current Solution"));
        list.getStyleClass().add("mylistview");
        list.setBackground(new Background(new BackgroundFill(Color.rgb(40,45,50), CornerRadii.EMPTY, Insets.EMPTY)));
        
        ListPane.getChildren().add(list);
        ListPane.setBackground(new Background(new BackgroundFill(Color.rgb(40,45,50), CornerRadii.EMPTY, Insets.EMPTY)));
        Platform.runLater(new Runnable() {

			@Override
			public void run() {
				list.depthProperty().set(1);
		        list.setExpanded(true);
				
			}});
    }
    
}
