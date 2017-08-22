package GUI.Frame;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is pane to show the status of cpu usage, memory usage, time
 * remaining and other relevant information.
 * 
 * @author Vincent
 * @see Gauge
 */
public class DataVisualization extends GridPane {
	private Gauge cpu;
	private Gauge memory;
	private Gauge progress;
	private Label id;
	private Label time;

	/**
	 * DataVisualization.
	 * <p>
	 * public DataVisualization()
	 * <p>
	 * Creates and initialize DataVisualization to show informations.
	 */
	public DataVisualization() {
		GaugeBuilder builder = GaugeBuilder.create().skinType(SkinType.SLIM);
		cpu = builder.decimals(1).maxValue(100).unit("%").build();
		memory = builder.decimals(1).maxValue(100).unit("%").build();
		progress = builder.decimals(1).maxValue(100).unit("%").build();
		id = new Label("50");
		time = new Label("100");

		VBox cpuPart = getGaugeVBox("CPU USAGE", Color.rgb(100, 100, 200), cpu);
		VBox memoryPart = getGaugeVBox("RAM USAGE", Color.rgb(100, 200, 200), memory);
		VBox progressPart = getGaugeVBox("PROGRESS", Color.rgb(200, 100, 200), progress);
		VBox idPart = getLabelVBox("Last task ID : ", Color.rgb(150, 200, 50), id);
		VBox timePart = getLabelVBox("Time Remaining : ", Color.rgb(50, 200, 150), time);

		progress.setBarColor(Color.rgb(200, 100, 200));

		setCpu(30);
		setRam(100);
		setProgress(40);

		setPadding(new Insets(20));
		setHgap(10);
		setVgap(15);
		setBackground(new Background(new BackgroundFill(Color.rgb(40, 45, 50), CornerRadii.EMPTY, Insets.EMPTY)));

		add(idPart, 0, 0);
		add(timePart, 0, 1);
		add(cpuPart, 0, 2);
		add(memoryPart, 0, 3);
		add(progressPart, 0, 4);

	}

	/**
	 * Add a bar and a short explain for a gauge. And set a color for them.
	 * 
	 * @param TEXT
	 * @return VBox
	 */
	private VBox getGaugeVBox(final String TEXT, final Color COLOR, final Gauge gauge) {
		Rectangle bar = new Rectangle(200, 3);
		bar.setArcWidth(6);
		bar.setArcHeight(6);
		bar.setFill(COLOR);

		Label label = new Label(TEXT);
		label.setTextFill(COLOR);
		label.setAlignment(Pos.CENTER);
		label.setPadding(new Insets(0, 0, 10, 0));

		gauge.setBarBackgroundColor(Color.rgb(40, 45, 50));
		gauge.setAnimated(true);

		VBox vBox = new VBox(bar, label, gauge);
		vBox.setSpacing(3);
		vBox.setAlignment(Pos.CENTER);
		return vBox;
	}

	/**
	 * Add a bar and a short explain for a label. And set a color for them.
	 * 
	 * @param TEXT
	 * @return VBox
	 */
	private VBox getLabelVBox(final String TEXT, final Color COLOR, Label LABEL) {
		Rectangle bar = new Rectangle(200, 3);
		bar.setArcWidth(6);
		bar.setArcHeight(6);
		bar.setFill(COLOR);

		Label label = new Label(TEXT);
		label.setTextFill(COLOR);
		label.setAlignment(Pos.CENTER);

		LABEL.setTextFill(COLOR);
		LABEL.setAlignment(Pos.CENTER);

		VBox vBox = new VBox(bar, label, LABEL);
		vBox.setSpacing(3);
		vBox.setAlignment(Pos.CENTER);
		return vBox;
	}

	/**
	 * Set current cpu usage
	 * 
	 * @param
	 */
	public void setCpu(double usage) {
		Color color = Color.rgb(100 + (int) (usage * 1.55), 255 - (int) (usage * 1.55), 50);
		cpu.setValue(usage);
		cpu.setBarColor(color);

	}

	/**
	 * Set current memory usage
	 * 
	 * @param
	 */
	public void setRam(double usage) {
		memory.setValue(usage);
		memory.setBarColor(Color.rgb(100 + (int) (usage * 1.55), 255 - (int) (usage * 1.55), 50));
	}

	/**
	 * Set how much work has been done
	 * 
	 * @param
	 */
	public void setProgress(double p) {
		progress.setValue(p);
	}

	/**
	 * Set the time remaining.
	 * 
	 * @param
	 */
	public void setTimeRemaining(String time) {
		this.time.setText(time);
	}

	/**
	 * Set the last task id.
	 * 
	 * @param
	 */
	public void setTaskId(String id) {
		this.id.setText(id);
	}
}
