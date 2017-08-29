package ufrn.ia.fuzzy.gui;

import static ufrn.ia.fuzzy.gui.FuzzyScenesManager.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ApplicationSceneController extends Application implements Initializable {

	public ApplicationSceneController() {

		System.out.println("\nApplicationController() [constructor]");
		System.out.println(this);
		System.out.flush();
	}

	@Override
	public void init() throws Exception {

		super.init();
		onInit();
	}

	@Override
	public void start(Stage stage) throws Exception {

		System.out.println("\nApplicationSceneController.start() [START]");
		System.out.println(this);
		System.out.flush();

		scenes().stage(stage);
		onStart(stage);

		System.out.println("\nApplicationSceneController.start() [START]");
		System.out.println(this);
		System.out.flush();

	}

	protected Class<?> applicationClass() {
		return null;
	}

	protected Stage stage() {
		return scenes().stage();
	}

	public Stage stage(ActionEvent event) {

		if (stage() == null) {
			scenes().stage((Stage) ((Node) event.getSource()).getScene().getWindow());
		}
		return stage();
	}

	public void show() {

		onShow(stage());
		stage().show();
	}

	public void hide() {


		if (stage() != null) {
			stage().hide();
		} else {
//			QPumpMessages.log("[ERRO] Stage nao configurado!");
		}
	}

	private void onInit() {

	}

	public void onStart(Stage stage) {

	}

	public void onShow(Stage stage) {

	}

	@Override
	public void initialize(URL url, ResourceBundle resource) { }

}
