package application.view.polygonlist;

import application.MainApp;
import application.model.adapter.ObservablePolygon;
import application.util.DialogsUtil;
import application.view.polygonlist.polygonlistcell.PolygonListCell;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PolygonListController {
	@FXML
	private TableView<ObservablePolygon> polygonTable;
	@FXML
	private TableColumn<ObservablePolygon, ObservablePolygon> wordColumn;

	private MainApp mainApp;

	@FXML
	private void handleAddPolygon() {
		mainApp.addPolygon();
	}

	@FXML
	private void handleDeletePolygon() {
		int selectedIndex = polygonTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			polygonTable.getItems().remove(selectedIndex);
		} else {
			DialogsUtil.noSelection();
		}
	}

	@FXML
	private void initialize() {
		polygonTable.setEditable(true);
		wordColumn.setCellFactory(PolygonListCell.forTableColumn());

		wordColumn.setCellValueFactory(
				cellData ->
				new SimpleObjectProperty<ObservablePolygon>(cellData.getValue())
				);

		polygonTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) ->
				mainApp.setCurrentPolygon(newValue)
				);

		polygonTable.setPlaceholder(new Label("Aucun polygone disponible."));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		polygonTable.setItems(mainApp.getPolygonData());
		PolygonListCell.setMainApp(mainApp);
	}
}
