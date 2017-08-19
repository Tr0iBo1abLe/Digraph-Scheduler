package GUI.Frame;

import com.jfoenix.controls.JFXListView;

import javafx.scene.control.Label;

public class MyListView extends JFXListView<Label> {
    
    public MyListView() {
    	super();
    	getItems().add(new Label("Lab 1"));
    	getItems().add(new Label("Lab 2"));
        getStyleClass().add("mylistview");
        depthProperty().set(1);
        setExpanded(true);
    }
}
