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

public class DataVisualization extends GridPane {   
	private Gauge    cpu;  
	private Gauge    memory;  
	private Gauge    progress;  

	public DataVisualization() { 
		GaugeBuilder builder = GaugeBuilder.create().skinType(SkinType.SLIM);   
		cpu       = builder.decimals(1).maxValue(100).unit("%").build();  
		memory    = builder.decimals(1).maxValue(100).unit("%").build();  
		progress  = builder.decimals(1).maxValue(100).unit("%").build();  

		VBox cpuPart = getVBox("CPU USAGE", Color.rgb(100,100,200), cpu);  
		VBox memoryPart = getVBox("RAM USAGE", Color.rgb(100,200,200), memory);  
		VBox progressPart = getVBox("PROGRESS", Color.rgb(200,100,200), progress);  
		progress.setBarColor(Color.rgb(200,100,200));
		
		setCpu(30);  
		setRam(100); 
		setProgress(40); 

		setPadding(new Insets(20));  
		setHgap(10);  
		setVgap(15);  
		setBackground(new Background(new BackgroundFill(Color.rgb(40,45,50), CornerRadii.EMPTY, Insets.EMPTY)));  

		add(cpuPart, 0, 0);  
		add(memoryPart, 0, 1);  
		add(progressPart, 0, 2);       
	}  

	private VBox getVBox(final String TEXT, final Color COLOR, final Gauge gauge) {  
		Rectangle bar = new Rectangle(200, 3);  
		bar.setArcWidth(6);  
		bar.setArcHeight(6);  
		bar.setFill(COLOR);  

		Label label = new Label(TEXT);  
		label.setTextFill(COLOR);  
		label.setAlignment(Pos.CENTER);  
		label.setPadding(new Insets(0, 0, 10, 0));  

		gauge.setBarBackgroundColor(Color.rgb(40,45,50));  
		gauge.setAnimated(true);  

		VBox vBox = new VBox(bar, label, gauge);  
		vBox.setSpacing(3);  
		vBox.setAlignment(Pos.CENTER);  
		return vBox;  
	}  

	public void setCpu(double usage) {
		Color color=Color.rgb(100+(int)(usage*1.55), 255-(int)(usage*1.55), 50);
		cpu.setValue(usage);
		cpu.setBarColor(color);
		
	}

	public void setRam(double usage) {
		memory.setValue(usage);
		memory.setBarColor(Color.rgb(100+(int)(usage*1.55), 255-(int)(usage*1.55),50));
	}
	
	public void setProgress(double p) {
		progress.setValue(p);
	}

}  

