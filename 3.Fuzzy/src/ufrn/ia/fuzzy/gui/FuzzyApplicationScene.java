package ufrn.ia.fuzzy.gui;
import static ufrn.ia.fuzzy.Fuzzy.fuzzy;
import static ufrn.ia.fuzzy.gui.FuzzyScenesManager.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FuzzyApplicationScene extends ApplicationSceneController implements FuzzyApplication {

	private static final int MAX_TANK_LEVEL = 30;

	private static final int DEFAULT_GRAPH_SERIES = 20;

	public static final String FXML_APPLICATION_SCENE = "mainScene3.fxml";

	public static final String APPLICATION_TITLE = "UFRN/IAA - Fuzzy Specialist";

	private static FuzzyApplicationScene singleton;

	@FXML
    private MenuButton controllerAButton;

	@FXML
    private MenuItem menuControllerPTankA;

	@FXML
    private MenuItem menuControllerPDTankA;

	@FXML
    private MenuItem menuControllerPITankA;

	@FXML
    private MenuItem menuControllerPIDTankA;

	@FXML
    private MenuItem menuControllerPI_DTankA;

	@FXML
    private MenuButton controllerBButton;

	@FXML
    private MenuItem menuControllerPTankB;

	@FXML
    private MenuItem menuControllerPDTankB;

	@FXML
    private MenuItem menuControllerPITankB;

	@FXML
    private MenuItem menuControllerPIDTankB;

	@FXML
    private MenuItem menuControllerPI_DTankB;

    @FXML
    private MenuButton trButton;

    @FXML
    private MenuButton tsButton;

    @FXML
    private Label labelParamA;

    @FXML
    private Label labelParamB;

    @FXML
    private Label tpTimeLabel;

    @FXML
    private Label mpValueLabel;

    @FXML
    private Label trTimeLabel;

    @FXML
    private Label tsTimeLabel;

    @FXML
    private CheckBox checkBackwardWindupFilter;

    @FXML
    private CheckBox checkConditionalWindupFilter;;

    @FXML
    private CheckBox checkDerivativeFilter;

    @FXML
    private TextField kpTankA;

    @FXML
    private TextField kiTankA;

    @FXML
    private TextField kdTankA;

    @FXML
    private TextField kpTankB;

    @FXML
    private TextField kiTankB;

    @FXML
    private TextField kdTankB;

    @FXML
    private ProgressBar tankABar;

	@FXML
    private ProgressBar tankBBar;

    @FXML
    private MenuButton loopButton;

    @FXML
    private Label tankALevelLabel;

    @FXML
    private Label tankBLevelLabel;

    @FXML
    private Slider sliderLevelTankA;

    @FXML
    private TextField textLevelTankA;

    @FXML
    private Slider sliderLevelTankB;

    @FXML
    private TextField textLevelTankB;

	@FXML
	private ImageView connectionImage;

	@FXML
	private ImageView controllerImage;

	@FXML
	private Button exitButton;

	@FXML
	private Button simulationButton;

	@FXML
    private TextArea messagesBoard;

    @FXML
    private MenuItem openLoopMenu;

    @FXML
    private MenuItem closedLoopForwardedMenu;

    @FXML
    private MenuItem closedLoopCascadingMenu;

    @FXML
    private MenuItem observerStateSpaceMenu;

    @FXML
    private Button connectionButton;

    @FXML
    private Button aboutButton;

    @FXML
    private StackedAreaChart<NumberAxis, NumberAxis> variablesGraphA;

    @FXML
    private LineChart<NumberAxis, NumberAxis> signalsGraphA;

    @FXML
    private StackedAreaChart<NumberAxis, NumberAxis> variablesGraphB;

    @FXML
    private LineChart<NumberAxis, NumberAxis> signalsGraphB;

    private AnimationTimer graphsUpdater;

	protected Class<?>  applicationClass() {
		return FuzzyApplicationScene.class;
	}

	public FuzzyApplicationScene() {

		super();

		System.out.println("\nQPumpApplicationScene() [constructor]");
		System.out.println(this);
		System.out.flush();
	}

    @Override
	public void start(Stage stage) throws Exception {

		System.out.println("\nQPumpApplicationScene.start() [START]");
		System.out.println(this);
		System.out.flush();

		super.start(stage);

		System.out.println("\nQPumpApplicationScene.start() [END]");
		System.out.println(this);
		System.out.flush();
    }

	@Override
	public void clearGraphs() {

		if (variablesGraphA != null) {
			variablesGraphA.getData().clear();
		}

		if (signalsGraphA != null) {
			signalsGraphA.getData().clear();
		}

		if (variablesGraphB != null) {
			variablesGraphB.getData().clear();
		}

		if (signalsGraphB != null) {
			signalsGraphB.getData().clear();
		}
	}

//	private SignalHandler mute(SignalHandler handler) {
//
//		// This handler must be, by default, muted.
//		handler.disconnect();
//		return handler;
//	}

	public void setup() {

		sliderLevelTankA.valueProperty().addListener(
				new ChangeListener<Number>() {
					public void changed(
									ObservableValue<? extends Number> ov,
									Number oldValue, Number newValue) {

//						qpump().model().tankA().SP(newValue.floatValue());

			    		textLevelTankA.setText(String.format("%.0f", newValue));

						if (sliderLevelTankA.isVisible()) {
							if (newValue.doubleValue() > sliderLevelTankA.valueProperty().doubleValue()) {
								sliderLevelTankA.setValue(newValue.doubleValue());
							}
						}
            }
        });

		textLevelTankA.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {
			    	try {

			    		String text = textLevelTankA.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().model().tankA().SP(value);

			    		sliderLevelTankA.setValue(value < 0 ? 0 : value > MAX_TANK_LEVEL ? MAX_TANK_LEVEL : value);

					} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		sliderLevelTankB.valueProperty().addListener(
				new ChangeListener<Number>() {
					public void changed(
									ObservableValue<? extends Number> ov,
									Number oldValue, Number newValue) {

//						qpump().model().tankB().SP(newValue.floatValue());

						textLevelTankB.setText(String.format("%.0f", newValue));

						if (newValue.doubleValue() > sliderLevelTankB.valueProperty().doubleValue()) {
							sliderLevelTankB.setValue(newValue.doubleValue());
						}
            }
        });

		textLevelTankB.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {
			    	try {

			    		String text = textLevelTankB.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//						qpump().model().tankB().SP(value);

			    		sliderLevelTankB.setValue(value < 0 ? 0 : value > MAX_TANK_LEVEL ? MAX_TANK_LEVEL : value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		kpTankA.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {

			    	try {

			    		String text = kpTankA.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().options().parametersControllerA().Kp().setK(value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		kiTankA.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {

			    	try {

			    		String text = kiTankA.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().options().parametersControllerA().Ki().setK(value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		kdTankA.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {

			    	try {

			    		String text = kdTankA.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().options().parametersControllerA().Kd().setK(value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		kpTankB.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {

			    	try {

			    		String text = kpTankB.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().options().parametersControllerB().Kp().setK(value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		kiTankB.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {

			    	try {

			    		String text = kiTankB.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().options().parametersControllerB().Ki().setK(value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

		kdTankB.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(
		    				ObservableValue<? extends Boolean> observable,
		    				Boolean oldValue, Boolean newValue) {

		    	if (!newValue) {

			    	try {

			    		String text = kdTankB.getText();
			    		float value = (text != null && !text.isEmpty() ? Float.parseFloat(text.replace(',', '.')) : 0f);

//			    		qpump().options().parametersControllerB().Kd().setK(value);

			    	} catch (NumberFormatException nfex) {
					}
		    	}
		    }
		});

//    	Graphs.register(new Graph(TANK_A_VARIABLES_GRAPH, "Variaveis de Controle", variablesGraphA));
//    	Graphs.register(new Graph(TANK_A_SIGNALS_GRAPH, "Sinais de Controle", signalsGraphA));
//
//    	Graphs.register(new Graph(TANK_B_VARIABLES_GRAPH, "Variaveis de Controle", variablesGraphB));
//    	Graphs.register(new Graph(TANK_B_SIGNALS_GRAPH, "Sinais de Controle", signalsGraphB));

		startControlsUpdater();
	}

    private void startControlsUpdater() {

        // AnimationTimer is executed in the JavaFX Main thread
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
            	updateInteface();
            }
        }.start();
    }

	private AnimationTimer graphsUpdater() {

		if (graphsUpdater == null) {
	        // AnimationTimer is executed in the JavaFX Main thread
	        // Every frame to take any data from queue and add to chart
			graphsUpdater =  new AnimationTimer() {

	            @Override
	            public void handle(long now) {
	            	try {
	            		updateGraphs();
	            	} catch (Throwable thr) {
	            		thr.printStackTrace();
	            	}
	            }

	        };
		}

		return graphsUpdater;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {

		System.out.println("\nQPumpApplicationSceneV1.initialize() [START]");
		System.out.println(this);
		System.out.flush();

		scenes().application(this);

		setup();

		System.out.println("\nQPumpApplicationSceneV1.initialize() [END]");
		System.out.println(this);
		System.out.flush();
	}

    private void updateInteface() {

    	if (simulationButton.getUserData() == null || fuzzy().isRunning() != ((Boolean) simulationButton.getUserData())) {

    		if (fuzzy().isRunning()) {
	    		simulationButton.setText("Finalizar controle");
	    		graphsUpdater().start();

    		} else {
        		simulationButton.setText("Iniciar controle");
        		graphsUpdater().stop();
    		}

	    	simulationButton.setUserData(fuzzy().isRunning());
    	}

//    	if (simulationButton.isDisable() && !options().isModeNone()) {
//    		simulationButton.setDisable(false);
//    	}
//
//   		tankALevelLabel.setText(String.format("%.1f cm", tankA().level()));
////   		tankABar.setProgress(tankA().percentualLevel());
//
//  		tankBLevelLabel.setText(String.format("%.1f cm", tankB().level()));
////   		tankBBar.setProgress(tankB().percentualLevel());
//
//    	while (QPumpMessages.hasUnprocessedMessages()) {
////    		messagesBoard.appendText(QPumpMessages.process());
//    		messagesBoard.appendText("\n");
//    	}
    }

	private void updateGraphs() {

//    	try {
//
//    		while (QPumpGraphs.graphVariablesTankA().hasUnprocessedData()) {
//	    		QPumpGraphs.graphVariablesTankA().process();
//	    	}
//
//	    	while (QPumpGraphs.graphVariablesTankB().hasUnprocessedData()) {
//	    		QPumpGraphs.graphVariablesTankB().process();
//	    	}
//
//    	} catch (Throwable thr) {
//    		thr.printStackTrace();
//    	}

    }

	@FXML
    public void onShowAbout(ActionEvent event) {
//		AboutController controller = new AboutController(stage(event));
//		controller.show();
    }

    @FXML
    public void onConfigure(ActionEvent event) {
//		ConnectionController controller = new ConnectionController(stage(event));
//		controller.show();
    }

    @FXML
    public void onSimulate(ActionEvent event) {

    	if (!fuzzy().isRunning()) {

//    		if (fuzzy().options().isOpenLoopMode()) {
//	    		SimulationController controller = new SimulationController(stage(event));
//	    		controller.showModal(event);
//
//    		} else {
//
//    			fuzzy().control();
//
//    			fuzzy().start();
//    		}

    	} else {

    		fuzzy().stop();
    	}
    }

    @FXML
    public void onExit(ActionEvent event) {

    	if (fuzzy().isRunning()) {
    		fuzzy().stop();
    	}

    	hide();
    }

    @FXML
    public void onSelectOpenLoop(ActionEvent event) {

    	MenuItem menu = (MenuItem) event.getSource();

    	loopButton.setText(menu.getText());

        labelParamA.setText("Parâmetros do Controlador TA");
    	controllerAButton.setText("Controlador Tanque A");
    	controllerAButton.setDisable(true);

    	kpTankA.setDisable(true);
    	kiTankA.setDisable(true);
    	kdTankA.setDisable(true);
    	textLevelTankA.setDisable(false);
    	sliderLevelTankA.setDisable(false);

        labelParamB.setText("Parâmetros do Controlador TB");
    	controllerBButton.setText("Controlador Tanque B");
    	controllerBButton.setDisable(true);

    	kpTankB.setDisable(true);
    	kiTankB.setDisable(true);
    	kdTankB.setDisable(true);
    	textLevelTankB.setDisable(true);
    	sliderLevelTankB.setDisable(true);

    	trButton.setDisable(true);
    	tsButton.setDisable(true);

    	checkBackwardWindupFilter.setDisable(true);
    	checkConditionalWindupFilter.setDisable(true);
    	checkDerivativeFilter.setDisable(true);

//    	fuzzy().options().selectOpenLoopControl();

//    	qpump().options().openLoopMode((QuanserControl) menu.getUserData());
    }

    @FXML
    public void onSelectForwardingClosedLoop(ActionEvent event) {

    	MenuItem menu = (MenuItem) event.getSource();

    	loopButton.setText(menu.getText());

        labelParamA.setText("Parâmetros do Controlador TA");
    	controllerAButton.setText("Controlador Tanque A");
    	controllerAButton.setDisable(false);

    	kpTankA.setDisable(true);
    	kiTankA.setDisable(true);
    	kdTankA.setDisable(true);
    	textLevelTankA.setDisable(false);
    	sliderLevelTankA.setDisable(false);

        labelParamB.setText("Parâmetros do Controlador TB");
    	controllerBButton.setText("Controlador Tanque B");
    	controllerBButton.setDisable(true);

    	kpTankB.setDisable(true);
    	kiTankB.setDisable(true);
    	kdTankB.setDisable(true);
    	textLevelTankB.setDisable(true);
    	sliderLevelTankB.setDisable(true);

    	checkBackwardWindupFilter.setDisable(false);
    	checkConditionalWindupFilter.setDisable(false);
    	checkDerivativeFilter.setDisable(false);

//    	fuzzy().options().selectForwadingControl();
    }

    @FXML
    public void onSelectCascadingClosedLoop(ActionEvent event) {

    	MenuItem menu = (MenuItem) event.getSource();

    	loopButton.setText(menu.getText());

        labelParamA.setText("Parâmetros do Controlador TA");
    	controllerAButton.setText("Controlador SLAVE");
    	controllerAButton.setDisable(true);

    	kpTankA.setDisable(false);
    	kiTankA.setDisable(false);
    	kdTankA.setDisable(false);
    	textLevelTankA.setDisable(true);
    	sliderLevelTankA.setDisable(true);

        labelParamB.setText("Parâmetros do Controlador TB");
    	controllerBButton.setText("Controlador MASTER");
    	controllerBButton.setDisable(true);

    	kpTankB.setDisable(false);
    	kiTankB.setDisable(false);
    	kdTankB.setDisable(false);
    	textLevelTankB.setDisable(false);
    	sliderLevelTankB.setDisable(false);

    	checkBackwardWindupFilter.setDisable(false);
    	checkConditionalWindupFilter.setDisable(false);
    	checkDerivativeFilter.setDisable(false);

//    	fuzzy().options().selectCascadingControl();
    }

    @FXML
    public void onSelectObserverStateSpace(ActionEvent event) {

    	MenuItem menu = (MenuItem) event.getSource();

    	loopButton.setText(menu.getText());

        labelParamA.setText("Parâmetros do Controlador TA");
    	controllerAButton.setText("Controlador >> P");
    	controllerAButton.setDisable(true);

    	kpTankA.setDisable(false);
    	kiTankA.setDisable(true);
    	kdTankA.setDisable(true);
    	textLevelTankA.setDisable(true);
    	sliderLevelTankA.setDisable(true);

        labelParamB.setText("Parâmetros do Observador");
    	controllerBButton.setText("Ação integativa");
    	controllerBButton.setDisable(true);

    	kpTankB.setDisable(true);
    	kiTankB.setDisable(false);
    	kdTankB.setDisable(true);
    	textLevelTankB.setDisable(false);
    	sliderLevelTankB.setDisable(false);

    	checkBackwardWindupFilter.setDisable(true);
    	checkConditionalWindupFilter.setDisable(true);
    	checkDerivativeFilter.setDisable(true);

//    	ObserverController controller = new ObserverController(stage(event));
//		controller.show();
    }

	@Override
	public void onShow(Stage stage) {

 	}

	@Override
	public void onStart(Stage stage) {
//		prepareTimeline();
	}

	public static FuzzyApplicationScene application() {

		if (singleton == null) {
			singleton = new FuzzyApplicationScene();
		}
		return singleton;
	}

	public static void main(String[] args) {
		System.exit(0);
	}

//	public Collection<GraphSerie> configureSensorGraphSeries(Collection<Sensor> sensors) {
//
//		if (sensors != null && !sensors.isEmpty()) {
//
//			Collection<GraphSerie> graphDataSeries = new ArrayList<GraphSerie>(DEFAULT_GRAPH_SERIES);
//
//			// Adiciona as series dos sensores que estao selecionados no modelo
//			for (Sensor sensor : sensors) {
//				graphDataSeries.add(new GraphSerie(sensor, sensor.label(), sensor.description()));
//			}
//
//			return graphDataSeries;
//		}
//
//		return null;
//	}

	public void configureGraphSeries() {

//		Tank tank;
//
//		// Tank A graphs
//		tank = qpump().model().tankA();
//		graphVariablesTankA().configureSeries(
//				new GraphSerie(tank.SP(),  tank.SP().label(),  tank.SP().description()),
//				new GraphSerie(tank.PV(),  tank.PV().label(),  tank.PV().description()),
//				new GraphSerie(tank.MV(),  tank.MV().label(),  tank.MV().description()),
//				new GraphSerie(tank.EPV(), tank.EPV().label(), tank.EPV().description()));
//
//		// Tank B graphs
//		tank = qpump().model().tankB();
//		graphVariablesTankB().configureSeries(
//				new GraphSerie(tank.SP(),  tank.SP().label(),  tank.SP().description()),
//				new GraphSerie(tank.PV(),  tank.PV().label(),  tank.PV().description()),
//				new GraphSerie(tank.MV(),  tank.MV().label(),  tank.MV().description()),
//				new GraphSerie(tank.EPV(), tank.EPV().label(), tank.EPV().description()));

	}

}