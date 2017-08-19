package GUI.Frame;


import com.jfoenix.controls.JFXButton;

public final class MyButton extends JFXButton {
	
	public MyButton(String text) {
		super(text);
		this.getStyleClass().add("button-raised");
	}
	

}

