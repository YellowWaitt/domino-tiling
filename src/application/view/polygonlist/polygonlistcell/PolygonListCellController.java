package application.view.polygonlist.polygonlistcell;

import java.awt.Rectangle;

import application.model.adapter.ObservablePolygon;
import application.view.drawer.Drawer;
import application.view.drawer.DrawerSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class PolygonListCellController {
	@FXML
	private GridPane gridPane;
	@FXML
	private Pane drawerPane;
	@FXML
	private Label boundaryLabel;
	@FXML
	private Label boundaryValidLabel;
	@FXML
	private Label tilleableLabel;
	@FXML
	private Label sizeLabel;

	private Drawer drawer;
	private ObservablePolygon polygon;
	private boolean editorActive;

	public ObservablePolygon getPolygon() {
		return polygon;
	}

	@FXML
	private void initialize() {
		drawer = new Drawer();
		drawerPane.getChildren().add(drawer);
		drawer.setRegion(drawerPane);
		DrawerSettings settings = drawer.getSettings();
		settings.drawGridProperty().set(false);
		settings.setPolygonLineWidth(2.0);
		settings.setPadx(10);
		settings.setPady(10);

		editorActive = false;
	}

	private void refreshDisplay(ObservablePolygon polygon) {
		if (polygon == null) {
			boundaryLabel.setText("");
			boundaryValidLabel.setText("");
			tilleableLabel.setText("");
			sizeLabel.setText("");
		} else {
			Rectangle box = polygon.getBoundingBox();
			boundaryLabel.setText(polygon.getBoundaryWord());
			boundaryValidLabel.setText(polygon.isBoundaryValid() ? "Oui" : "Non");
			tilleableLabel.setText(polygon.isTilleable() ? "Oui" : "Non");
			sizeLabel.setText(String.format("%d x %d", box.width, box.height));
		}

		drawer.draw();
	}

	public void removeEditor(TextField textEditor, boolean cancel) {
		if (!editorActive) {
			return;
		}
		editorActive = false;

		if (!cancel) {
			String boundary = textEditor.getText();
			polygon.setBoundaryWord(boundary);
			refreshDisplay(polygon);
		}
		gridPane.getChildren().remove(textEditor);
		gridPane.add(boundaryLabel, 1, 0);
	}

	public void setEditor(TextField textEditor) {
		if (editorActive) {
			return;
		}
		editorActive = true;

		textEditor.setText(polygon.getBoundaryWord());
		gridPane.getChildren().remove(boundaryLabel);
		gridPane.add(textEditor, 1, 0);
		textEditor.requestFocus();
	}

	public void setPolygon(ObservablePolygon polygon) {
		this.polygon = polygon;
		drawer.setPolygon(polygon);

		refreshDisplay(polygon);
	}
}
