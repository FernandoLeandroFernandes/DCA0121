package ufrn.ia.fuzzy.gui;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public abstract class ScenesManager<T extends ApplicationSceneController> 
extends Application 
implements Initializable {

	protected T application;
	
	public abstract T application();

	protected static <C> Stage load(Stage stage, Class<C> clazz, String resource) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader();
			InputStream resourceStream = clazz.getResourceAsStream(resource);
			Parent parent = (Parent) loader.load(resourceStream);
			stage.setScene(new Scene(parent));
			
			return stage;
			
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		return null;
	}
	
	protected static Scene loadScene(Class<?> clazz, String resource) {
		return new Scene(loadResource(clazz, resource));
	}

	protected static Parent loadResource(Class<?> clazz, String resource) {

		try {
			return (Parent) FXMLLoader.load(clazz.getResource(resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
