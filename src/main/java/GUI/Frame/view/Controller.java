package GUI.Frame.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import com.jfoenix.controls.JFXToggleButton;

import GUI.Frame.DataVisualization;
import GUI.Frame.MyButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Controller is used to get the components in fxml file as java instants and
 * set event handler for those components.
 * 
 * @author Vincent
 */
public class Controller {

	@FXML
	private AnchorPane parentPane;

	@FXML
	private AnchorPane dataPane;

	@FXML
	private HBox buttonsPane;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private AnchorPane pane;

	private JFXToggleButton switchButton;
	private Pane graphPane;
	private Pane solutionPane;

	private MyButton start;
	private MyButton pause;
	private MyButton stop;

	public Controller() {

	}

	@FXML
	private void initialize() {

		initDataPane();

		initButtons();

		initMainPane();

		initSwitch();

	}

	private void initDataPane() {
		DataVisualization data = new DataVisualization();
		dataPane.getChildren().add(data);
		AnchorPane.setBottomAnchor(data, 0d);
		AnchorPane.setTopAnchor(data, 0d);
		AnchorPane.setLeftAnchor(data, 0d);
		AnchorPane.setRightAnchor(data, 0d);
	}

	private void initButtons() {
		buttonsPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(40, 45, 50), CornerRadii.EMPTY, Insets.EMPTY)));
		start = new MyButton("START");
		pause = new MyButton("PAUSE");
		stop = new MyButton("STOP");
		buttonsPane.getChildren().addAll(start, pause, stop);
	}

	private void initMainPane() {
		mainPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(40, 45, 50), CornerRadii.EMPTY, Insets.EMPTY)));
		graphPane = new Pane();
		graphPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(255, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		solutionPane = new Pane();
		solutionPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)));

		pane.getChildren().add(graphPane);
		AnchorPane.setBottomAnchor(graphPane, 0d);
		AnchorPane.setTopAnchor(graphPane, 0d);
		AnchorPane.setLeftAnchor(graphPane, 0d);
		AnchorPane.setRightAnchor(graphPane, 0d);
	}

	private void initSwitch() {
		switchButton = new JFXToggleButton();
		switchButton.setText("Graph");
		switchButton.setTextFill(Color.rgb(255, 255, 255));
		switchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (switchButton.getText().equals("Graph")) {
					switchButton.setText("Current Optimal Solution");
					pane.getChildren().remove(graphPane);
					pane.getChildren().add(solutionPane);
					AnchorPane.setBottomAnchor(solutionPane, 0d);
					AnchorPane.setTopAnchor(solutionPane, 0d);
					AnchorPane.setLeftAnchor(solutionPane, 0d);
					AnchorPane.setRightAnchor(solutionPane, 0d);
				} else {
					switchButton.setText("Graph");
					pane.getChildren().remove(solutionPane);
					pane.getChildren().add(graphPane);
					AnchorPane.setBottomAnchor(graphPane, 0d);
					AnchorPane.setTopAnchor(graphPane, 0d);
					AnchorPane.setLeftAnchor(graphPane, 0d);
					AnchorPane.setRightAnchor(graphPane, 0d);
				}
			}
		});
		mainPane.getChildren().add(switchButton);
		AnchorPane.setTopAnchor(switchButton, 5d);
		AnchorPane.setLeftAnchor(switchButton, 25d);

	}
}
