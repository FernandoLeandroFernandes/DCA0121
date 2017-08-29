package ufrn.ia.fuzzy.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class SceneController implements Initializable {

	private Stage mainStage;

	private Stage stage;

	private Scene scene;

	private String resource;

	public SceneController() { }

	public SceneController(Stage mainStage) {
		super();
		this.mainStage = mainStage;
	}

	public SceneController(Stage mainStage, String resource) {
		this(mainStage);
		this.resource = resource;
	}

	public Stage stage(ActionEvent event) {

		if (stage == null) {
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		}
		return stage;
	}

	public Stage stage() {

		if (this.stage == null) {
			this.stage = new Stage();
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(this.mainStage);
		}
		return stage;
	}

	private Scene scene() {

		if (this.scene == null) {
			this.scene = load(resource());
		}
		return scene;
	}

	protected String resource() {
		return (resource != null ? resource : getClass().getSimpleName().replace("Controller", "").toLowerCase().concat("Scene.fxml"));
	}

	protected Scene load(String resource) {

		try {

			URL resourceLoaded = getClass().getResource(resource);
			if (resourceLoaded == null) {
				throw new FileNotFoundException();
			}

			scene = new Scene((Parent) FXMLLoader.load(resourceLoaded));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return scene;
	}

	public void show() {

		if (scene() != null) {
			onShow(stage());
			stage().setScene(scene);
			stage().show();
		}
	}

	public void showModal(ActionEvent event) {

		if (scene() != null) {
			onShow(stage());
			stage().setScene(scene);

			stage().initModality(Modality.WINDOW_MODAL);
		    stage.initOwner(((Node) event.getSource()).getScene().getWindow());

			stage().show();
		}
	}

	public abstract void onShow(Stage stage);

}
