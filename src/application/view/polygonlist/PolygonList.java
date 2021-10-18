package application.view.polygonlist;

import java.io.IOException;

import application.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class PolygonList {

	private Node renderer;
	private PolygonListController controller;

	public PolygonList(MainApp mainApp) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PolygonList.class.getResource("PolygonList.fxml"));
		renderer = loader.load();
		controller = loader.getController();

		controller.setMainApp(mainApp);
	}

	public PolygonListController getController() {
		return controller;
	}

	public Node getRenderer() {
		return renderer;
	}
}
