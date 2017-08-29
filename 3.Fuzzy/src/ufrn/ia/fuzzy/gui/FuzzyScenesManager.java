package ufrn.ia.fuzzy.gui;

import static ufrn.ia.fuzzy.gui.FuzzyApplicationScene.APPLICATION_TITLE;
import static ufrn.ia.fuzzy.gui.FuzzyApplicationScene.FXML_APPLICATION_SCENE;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Stage;

public class FuzzyScenesManager extends ScenesManager<FuzzyApplicationScene>{

	private static FuzzyScenesManager singleton;

	private Stage stage;

	public FuzzyScenesManager() {
		singleton = this;
	}

	public void application(FuzzyApplicationScene application) {
		this.application = application;
		System.out.println(application);
	}

	public FuzzyApplicationScene application() {
		return application;
	}

	public Stage stage() {
		return this.stage;
	}

	public Stage stage(Stage stage) {
		return this.stage = stage;
	}

	@Override
	public void start(Stage stage) throws Exception {

		System.out.println("QPumpScenesManager.start() [START]");

		this.stage = stage;

		this.stage.setTitle(APPLICATION_TITLE);

		load(stage, FuzzyApplicationScene.class, FXML_APPLICATION_SCENE).show();

		System.out.println("\nQPumpScenesManager.start() [END]");
	}

	public void init() { 	}

	public static FuzzyScenesManager scenes() {

		if (singleton == null) {
			singleton = new FuzzyScenesManager();
		}
		return singleton;
	}

	public static void launchApplication() {

		scenes();

		// JavaFX initialization
		Application.launch();
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {

		System.out.println("\nQPumpScenesManager.initialize() [START]");

		System.out.println(url);
		System.out.println(resource);

		System.out.println("\nQPumpScenesManager.initialize() [START]");

	}

}
