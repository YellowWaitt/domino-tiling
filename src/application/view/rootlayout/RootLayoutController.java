package application.view.rootlayout;

import java.io.File;

import application.MainApp;
import application.util.DialogsUtil;
import application.view.drawer.Drawer;
import application.view.drawer.DrawerSettings;
import application.view.polygonlist.PolygonList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class RootLayoutController {
	@FXML
	private GridPane gridpane;
	@FXML
	private Pane drawerPane;
	@FXML
	private CheckMenuItem drawGrid;
	@FXML
	private CheckMenuItem drawCheckerBoard;
	@FXML
	private CheckMenuItem drawHeightFunction;
	@FXML
	private CheckMenuItem drawTiling;
	@FXML
	private CheckMenuItem drawColoredTiling;

	private MainApp mainApp;

	@FXML
	private void handleExit() {
		mainApp.exit();
	}

	@FXML
	private void handleMaximal() {
		mainApp.sample(0);
	}

	@FXML
	private void handleMinimal() {
		mainApp.sample(-1);
	}

	@FXML
	private void handleNewList() {
		mainApp.getPolygonData().clear();
		mainApp.setPolygonFilePath(null);
	}

	@FXML
	private void handleNewPolygon() {
		mainApp.addPolygon();
	}

	@FXML
	private void handleOnMousePressed() {
		drawerPane.requestFocus();
	}

	@FXML
	private void handleOpen() {
		FileChooser fileChooser = DialogsUtil.fileChooserXML();
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			mainApp.loadPolygons(file);
			mainApp.setPolygonFilePath(file);
		}
	}

	@FXML
	private void handleSample1() {
		mainApp.sample(1);
	}

	@FXML
	private void handleSample2() {
		mainApp.sample(2);
	}

	@FXML
	private void handleSample3() {
		mainApp.sample(3);
	}

	@FXML
	private void handleSave() {
		File file = mainApp.getPolygonFilePath();
		if (file != null) {
			mainApp.savePolygons(file);
		} else {
			handleSaveAs();
		}
	}

	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = DialogsUtil.fileChooserXML();
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.savePolygons(file);
		}
	}

	@FXML
	private void initialize() {
		drawTiling.selectedProperty().addListener(
				(observable, oldValue, selected) -> {
					drawColoredTiling.setDisable(!selected);
					if (!selected) {
						drawColoredTiling.setSelected(false);
					}
				});
	}

	public void setMainApp(MainApp mainApp, PolygonList polygonList,
			Drawer drawer) {
		this.mainApp = mainApp;

		Node polygonListRenderer = polygonList.getRenderer();
		GridPane.setConstraints(polygonListRenderer, 0, 0);
		gridpane.add(polygonListRenderer, 0, 0);

		drawerPane.getChildren().add(drawer);
		drawer.setRegion(drawerPane);

		DrawerSettings settings = drawer.getSettings();
		drawCheckerBoard.selectedProperty().bindBidirectional(
				settings.drawCheckerBoardProperty());
		drawGrid.selectedProperty().bindBidirectional(
				settings.drawGridProperty());
		drawHeightFunction.selectedProperty().bindBidirectional(
				settings.drawHeightFunctionProperty());
		drawTiling.selectedProperty().bindBidirectional(
				settings.drawTilingProperty());
		drawColoredTiling.selectedProperty().bindBidirectional(
				settings.drawColoredTilingProperty());

		drawColoredTiling.setDisable(!drawTiling.isSelected());
	}
}
