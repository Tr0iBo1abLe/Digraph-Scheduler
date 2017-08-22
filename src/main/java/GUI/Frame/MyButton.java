package GUI.Frame;


import com.jfoenix.controls.JFXButton;

/**
 * MyButton is a JFXButton which use a customer css style.
 * 
 * @author Vincent
 * @see JFXButton
 */
public final class MyButton extends JFXButton {

	public MyButton(String text) {
		super(text);
		this.getStyleClass().add("button-raised");
	}
	

}

