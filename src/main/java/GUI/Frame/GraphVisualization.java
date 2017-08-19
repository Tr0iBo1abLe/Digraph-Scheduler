package GUI.Frame;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class GraphVisualization extends JFXDrawersStack {

    public GraphVisualization() {
        
    	AnchorPane content = new AnchorPane();
        JFXButton button = new MyButton("Options");
        content.getChildren().add(button);
        AnchorPane.setTopAnchor(button, 0d);
        AnchorPane.setRightAnchor(button, 0d);


        JFXDrawer leftDrawer = new JFXDrawer();
        StackPane leftDrawerPane = new StackPane();
        leftDrawerPane.getChildren().add(new MyListView());
        leftDrawer.setSidePane(leftDrawerPane);
        leftDrawer.setDefaultDrawerSize(150);
        leftDrawer.setOverLayVisible(false);
        leftDrawer.setResizableOnDrag(true);

        setBackground(new Background(new BackgroundFill(Color.rgb(40,45,50), CornerRadii.EMPTY, Insets.EMPTY)));  

        setContent(content);

        leftDrawer.setId("left");

        button.addEventHandler(MOUSE_PRESSED, e -> this.toggle(leftDrawer));
    }


}
