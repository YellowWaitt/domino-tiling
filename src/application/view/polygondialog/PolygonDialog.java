package application.view.polygondialog;

import java.io.IOException;

import application.MainApp;
import application.util.FilesUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PolygonDialog extends Stage {

	private PolygonDialogController controller;

	public PolygonDialog(MainApp mainApp) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PolygonDialog.class.getResource("PolygonDialog.fxml"));
		Parent renderer = loader.load();
		controller = loader.getController();
		controller.setStage(this);

		setTitle("Nouveau polygone");
		initModality(Modality.WINDOW_MODAL);
		initOwner(mainApp.getPrimaryStage());
		setResizable(false);
		getIcons().add(FilesUtil.getIcon());
		setScene(new Scene(renderer));
	}

	public String getBoundary() {
		controller.reset();
		showAndWait();
		return controller.getBoundary();
	}
}
