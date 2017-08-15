package GUI.Frame.view;
import javafx.fxml.FXML;  
import javafx.scene.control.Button;  
import javafx.event.ActionEvent;  
import javafx.scene.control.Label;  

public class Controller {

	@FXML  
	private Button Button1;
	@FXML  
	private Label mLabel;

	@FXML
	public void onButtonClick(ActionEvent event) {  
		mLabel.setText("HelloWorld");
	}
}
