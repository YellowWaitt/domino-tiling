package application.view.rootlayout;

import java.io.IOException;

import application.MainApp;
import application.view.drawer.Drawer;
import application.view.polygonlist.PolygonList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class RootLayout {

	private Node renderer;
	private RootLayoutController controller;

	public RootLayout(MainApp mainApp, PolygonList polygonList, Drawer drawer)
			throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(RootLayout.class.getResource("RootLayout.fxml"));
		renderer = loader.load();
		controller = loader.getController();
		
		Scene scene = new Scene((Parent) renderer);
		mainApp.getPrimaryStage().setScene(scene);
		controller.setMainApp(mainApp, polygonList, drawer);
	}

	public RootLayoutController getController() {
		return controller;
	}

	public Node getRenderer() {
		return renderer;
	}
}
