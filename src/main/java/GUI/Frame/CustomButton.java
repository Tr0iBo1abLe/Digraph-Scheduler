package GUI.Frame;


import com.jfoenix.controls.JFXButton;

/**
 * CustomButton is a JFXButton which use a customer css style.
 *
 * @author Vincent
 * @see JFXButton
 */
public final class CustomButton extends JFXButton {

    public CustomButton(String text) {
        super(text);
        this.getStyleClass().add("button-raised");
    }


}

