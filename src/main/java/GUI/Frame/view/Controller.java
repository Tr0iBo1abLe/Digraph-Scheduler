package GUI.Frame.view;

import CommonInterface.ISearchState;
import CommonInterface.ISolver;
import Exporter.GraphExporter;
import GUI.Events.SolversThread;
import GUI.Events.SysInfoMonitoringThread;
import GUI.Events.ThreadCompleteListenerAdapter;
import GUI.Frame.CustomButton;
import GUI.GraphViewer;
import GUI.Interfaces.IUpdatableState;
import GUI.Models.GMouseManager;
import GUI.Models.SysInfoModel;
import GUI.ScheduleChart;
import GUI.Util.ColorManager;
import Graph.Graph;
import Solver.AbstractSolver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import com.jfoenix.controls.JFXToggleButton;

import GUI.Frame.DataVisualization;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Synchronized;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Controller is used to get the components in fxml file as java instances and
 * set event handler for those components. This is the top level controller of
 * all JavaFX GUI components. It takes charge of listening to every possible
 * events that are fired from different sources. However, it is NOT the only
 * event handler for GS component. See #GMouseManager and #GraphViewer.
 * =====================================================================================================================================
 * THIS IS A UTIL CLASS AND IS USED BY JFX FRAMEWORK IN ITS ON APPLICATION
 * THREAD. DO NOT EVER TRY TO INSTANTIATE IT IN THE MAIN THREAD.
 * =====================================================================================================================================
 * 
 * @author Vincent Chen, Mason Shi
 */
public class Controller implements IUpdatableState{

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
	private AnchorPane graphPane;
	private AnchorPane solutionPane;

	
	private long startTime;
	private DataVisualization data;

	private CustomButton start;
	private CustomButton pause;
	private CustomButton stop;

	public static org.graphstream.graph.Graph visualGraph;
	public static GraphViewer viewer;
	public static ViewPanel viewPanel;
	public static SwingNode swingNode;

	public ScheduleChart<Number, String> scheduleChart;
	private static final String procStr = "Processor";

	public static AbstractSolver abstractSolver = null;
	public static ISolver solver;

	public static Graph graph;

	public static SolversThread solversThread;
	public static SysInfoMonitoringThread sysInfoMonitoringThread;

	private static AtomicBoolean atomicBoolean = new AtomicBoolean();

	public Controller() {

	}

	@FXML
	private void initialize() {

		initDataPane();

		initButtons();

		initMainPane();

		initSwitch();

		initGraph();

		initChart();

		initSysInfoMonitor();
	}

	private void initSysInfoMonitor() {
		sysInfoMonitoringThread = new SysInfoMonitoringThread(SysInfoModel.getInstance());
		sysInfoMonitoringThread.addListener(new ThreadCompleteListenerAdapter(this));
	}

	private void initGraph() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		viewer = new GraphViewer(visualGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.initializeLabels(visualGraph);
		viewer.enableAutoLayout();
		viewPanel = viewer.addDefaultView(false);
		viewPanel.addMouseListener(new GMouseManager(viewPanel, visualGraph, viewer));
		viewPanel.addMouseWheelListener(new GMouseManager(viewPanel, visualGraph, viewer));
		swingNode = new SwingNode();
		SwingUtilities.invokeLater(() -> {
			swingNode.setContent(viewPanel);
		});
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(swingNode);
		stackPane.setPrefHeight(481);
		stackPane.setPrefWidth(738);
		graphPane.getChildren().add(swingNode);
		AnchorPane.setBottomAnchor(swingNode, 0d);
		AnchorPane.setTopAnchor(swingNode, 0d);
		AnchorPane.setLeftAnchor(swingNode, 0d);
		AnchorPane.setRightAnchor(swingNode, 0d);
	}

	public void initChart() {
		final NumberAxis xAxis = new NumberAxis();
		final CategoryAxis yAxis = new CategoryAxis();
		final int procN = solver.getProcessorCount();
		String[] procStrNames = new String[procN];
		scheduleChart = new ScheduleChart<>(xAxis, yAxis);
		XYChart.Series[] seriesArr = new XYChart.Series[solver.getProcessorCount()];

		IntStream.range(0, procN).forEach(i -> {
			XYChart.Series series = new XYChart.Series();
			seriesArr[i] = series;
			procStrNames[i] = procStr.concat(String.valueOf(i + 1));
			seriesArr[i].getData().add(new XYChart.Data(0, procStrNames[i],
					new ScheduleChart.ExtraData(0, "rgba(0,0,0,0), rgba(0,0,0,0)", "rgba(0,0,0,0);", "")));
		});
		String[] nodeNameArr = new String[visualGraph.getNodeSet().size()];
		for (int i = 0; i < seriesArr.length; i++) {
			nodeNameArr[i] = visualGraph.getNode(i).getId();
		}

		xAxis.setLabel("");
		xAxis.setMinorTickCount(4);

		yAxis.setLabel("");
		yAxis.setTickLabelGap(10);
		yAxis.setCategories(FXCollections.observableArrayList(procStrNames));

		scheduleChart.setLegendVisible(false);
		scheduleChart.setBlockHeight(50);
		scheduleChart.getData().addAll(seriesArr);

		scheduleChart.getStylesheets().add(DataVisualization.class.getResource("view/GanttChart.css").toExternalForm());

		scheduleChart.setPrefHeight(481);
		scheduleChart.setPrefWidth(738);
		scheduleChart.setMinWidth(738);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefHeight(481);
		scrollPane.setPrefWidth(738);
		scrollPane.setContent(scheduleChart);
		solutionPane.getChildren().add(scrollPane);
		AnchorPane.setBottomAnchor(scrollPane, 0d);
		AnchorPane.setTopAnchor(scrollPane, 0d);
		AnchorPane.setLeftAnchor(scrollPane, 0d);
		AnchorPane.setRightAnchor(scrollPane, 0d);
	}

	private void initDataPane() {
		data = new DataVisualization();
		dataPane.getChildren().add(data);
		AnchorPane.setBottomAnchor(data, 0d);
		AnchorPane.setTopAnchor(data, 0d);
		AnchorPane.setLeftAnchor(data, 0d);
		AnchorPane.setRightAnchor(data, 0d);
	}

	private void initButtons() {
		buttonsPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(40, 45, 50), CornerRadii.EMPTY, Insets.EMPTY)));
		start = new CustomButton("START");
		pause = new CustomButton("PAUSE");
		stop = new CustomButton("STOP");
		buttonsPane.getChildren().addAll(start, pause, stop);
		start.setOnAction(event -> {
			startTime=System.currentTimeMillis();
			solversThread = new SolversThread(Controller.this, solver);
			solversThread.addListener(new ThreadCompleteListenerAdapter(this));
			solversThread.start();
			pause.setDisable(false);
			stop.setDisable(false);
			start.setDisable(true);
		});
		pause.setOnAction(event -> {
			if ((solversThread != null) && (solversThread.isAlive())) {
				solversThread.suspend();
				start.setOnAction(eventResume -> {
					solversThread.resume();
					pause.setDisable(false);
					start.setDisable(true);
				});
				pause.setDisable(true);
				start.setDisable(false);
				stop.setDisable(false);
			}
		});
		stop.setOnAction(event -> {
			solversThread.suspend();
			solver.getTimer().cancel();
			SolversThread.isStoped = true;
			clearCharts();
			solversThread.stop();
		});
		pause.setDisable(true);
		stop.setDisable(true);
	}

	private void initMainPane() {
		mainPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(40, 45, 50), CornerRadii.EMPTY, Insets.EMPTY)));
		graphPane = new AnchorPane();
		graphPane.setBackground(
				new Background(new BackgroundFill(Color.rgb(255, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		solutionPane = new AnchorPane();
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

	@Synchronized
	@Override
	public void updateWithState(ISearchState searchState, AbstractSolver abstractsolver) {
		Platform.runLater(() -> {
            abstractSolver = abstractsolver;
			if (searchState == null)
				return;
			// Remove the past data
			visualGraph.getNodeSet().forEach(e -> {
				e.removeAttribute("ui.class");
				e.removeAttribute("processor");
				e.removeAttribute("startTime");
			});
			int[] processors = searchState.getProcessors();
			int[] startTimes = searchState.getStartTimes();
			List<XYChart.Series> seriesList = new ArrayList<>();
			visualGraph.getNodeSet().forEach(n -> {
				int index = n.getIndex();
				int procOn = processors[index];
				if (procOn != -1) {
					XYChart.Series series = new XYChart.Series();
					n.addAttribute("ui.class", "sched");
					n.addAttribute("processor", procOn + 1);
					n.addAttribute("startTime", startTimes[index]);

					Integer cost = null;
					try {
						Double d = n.getAttribute("Weight");
						cost = d.intValue();
					} catch (ClassCastException e) {
						cost = n.getAttribute("Weight");
						// Weight attr has to be double or int
					}
					series.getData().add(new XYChart.Data(startTimes[index], procStr + String.valueOf(procOn + 1),
							new ScheduleChart.ExtraData(cost,
									ColorManager.getColorSetForGanttTasks().get(String.valueOf(procOn + 1)),
									ColorManager.getColorSetForGanttTasksBorder().get(String.valueOf(procOn + 1)),
									n.getId())));

					seriesList.add(series);
				}
			});

			viewer.updateNodes(); // update the GS viewer

			updateLabels(searchState); // update labels at top right corner

			scheduleChart.getData().clear();
			XYChart.Series[] seriesArr = new XYChart.Series[seriesList.size()];
			scheduleChart.getData().addAll(seriesList.toArray(seriesArr));
			scheduleChart.setBlockHeight(1000d / Controller.solver.getProcessorCount());

			Platform.runLater(() -> {
				if ((atomicBoolean.get()) && (!(SolversThread.isStoped))) { // do the following only when the solver is
																			// not terminated by Stop()
					Platform.runLater(() -> {
						// Update colorNode() AND set progress bar to maximum here;
						// This is to deal with when there are too many timers created due to multiple
						// solvers switching among one another;
						// Make sure Controller can always catch the last timer alive.
						viewer.colorNodes(visualGraph);
						data.setProgress(100d);
					});
				} else {
					Platform.runLater(() -> {
						int curSize = AbstractSolver.getStateCounter();
						double temp = ((double) curSize / ((double) curSize + 10000000d)) * 100d;
						if (temp < 99.9d) { // avoid progress bar overflow
							data.setProgress(temp); // update progress bar
						}
					});
				}
			});
		});
	}

	@Synchronized
	private void updateLabels(ISearchState searchState) {
		data.setTaskId(searchState.getLastVertex().getId() + "");
		data.setFinishingTime(searchState.getUnderestimate() + "");
		data.setConsumingTime(System.currentTimeMillis()-startTime);
	}

	@Synchronized
	private void clearCharts() {
		visualGraph.getNodeSet().stream().forEach(n -> {
			n.removeAttribute("ui.class");
			n.removeAttribute("processor");
			n.removeAttribute("startTime");
		});
		viewer.updateNodes();
		scheduleChart.getData().clear();
		data.setProgress(0d);
		start.setDisable(true);
		pause.setDisable(true);
		stop.setDisable(true);
	}
	
	public void setCpu(double usage) {
		data.setCpu(usage);

	}

	public void setRam(double usage) {
		data.setRam(usage);
	}
	
	@Synchronized
	public void completed() {
		atomicBoolean.set(true);
        if (!(SolversThread.isStoped)){ //do the following only when the solver is not terminated by Stop()
            Platform.runLater(() -> {
                //Update colorNode AND set progress bar to maximum here;
                viewer.colorNodes(visualGraph);
                start.setDisable(true);
                pause.setDisable(true);
                stop.setDisable(true);
                // print output, graph exporter
                System.out.println(GraphExporter.exportGraphToString(graph));
                Platform.runLater(() -> data.setProgress(100d));
            });
        }
	}
}
