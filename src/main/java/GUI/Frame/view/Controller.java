package GUI.Frame.view;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import GUI.Frame.DataVisualization;
import GUI.Frame.GraphVisualization;
import GUI.Frame.MyButton;
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
    	
    	GraphVisualization graph=new GraphVisualization();
    	ListPane.getChildren().add(graph);
    	AnchorPane.setLeftAnchor(graph, 0d);
    	AnchorPane.setRightAnchor(graph, 0d);
    	AnchorPane.setTopAnchor(graph, 0d);
    	AnchorPane.setBottomAnchor(graph, 0d);
    	
    	Buttons.setBackground(new Background(new BackgroundFill(Color.rgb(40,45,50), CornerRadii.EMPTY, Insets.EMPTY)));
    	Buttons.getChildren().add(new MyButton("START"));
    	Buttons.getChildren().add(new MyButton("PAUSE"));
    	Buttons.getChildren().add(new MyButton("STOP"));
    	

    }
}
