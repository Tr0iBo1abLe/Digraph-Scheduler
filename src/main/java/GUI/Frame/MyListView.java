package GUI.Frame;

import com.jfoenix.controls.JFXListView;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class MyListView extends JFXListView<Label> {
    private JFXListView<Label> list=this;
	
    public MyListView() {
    	super();
        getItems().add(new Label("Graph"));
        getItems().add(new Label("Current Solution"));
        getStyleClass().add("mylistview");
        Platform.runLater(new Runnable() {

			@Override
			public void run() {
				list.depthProperty().set(1);
		        list.setExpanded(true);
				
			}});
    }
}
