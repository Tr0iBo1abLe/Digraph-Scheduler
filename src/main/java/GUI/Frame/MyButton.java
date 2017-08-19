package GUI.Frame;


import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class MyButton extends JFXButton {
	
	public MyButton(String text) {
		super(text);
		this.getStyleClass().add("button-raised");
	}
	

}

