package application.view.polygonlist.polygonlistcell;

import java.util.logging.Level;

import application.MainApp;
import application.model.adapter.ObservablePolygon;
import application.util.DialogsUtil;
import application.util.LogsUtil;
import application.util.TextFieldUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class PolygonListCell<T> extends TableCell<T, ObservablePolygon> {

	private static MainApp mainApp; 

	public static <T>
	Callback<TableColumn<T, ObservablePolygon>, TableCell<T, ObservablePolygon>>
	forTableColumn() {
		return (TableColumn<T, ObservablePolygon> tableColumn)
				-> new PolygonListCell<>();
	}

	public static void setMainApp(MainApp mainApp) {
		PolygonListCell.mainApp = mainApp;
	}


	private Node renderer;
	private PolygonListCellController rendererController;

	private TextField textEditor;
	private ObservablePolygon polygonEdited;

	public PolygonListCell() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PolygonListCell.class
					.getResource("PolygonCell.fxml"));
			renderer = loader.load();
			rendererController = loader.getController();
		} catch (Exception e) {
			LogsUtil.logger.log(Level.SEVERE, "Chargement tableCell", e);
			DialogsUtil.exceptionAlert("Erreur",
					"Affichage polygone",
					"Impossible de charger l'afffichage du polygone",
					e
					);
			mainApp.exit();
		}

		textEditor = new TextField();
		textEditor.setOnKeyPressed(keyEvent -> {
			switch (keyEvent.getCode()) {
			case ESCAPE:
				cancelEdit();
				break;
			case ENTER:
				commitEdit(rendererController.getPolygon());
				break;
			default: break;
			}
		});
		textEditor.setTextFormatter(TextFieldUtil.boundaryFormat());
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		configureEditorOff(true);
	}

	@Override
	public void commitEdit(ObservablePolygon value) {
		super.commitEdit(value);
		configureEditorOff(false);
		mainApp.setCurrentPolygon(value);
	}

	private void configureEditorOff(boolean cancel) {
		rendererController.removeEditor(textEditor, cancel);
	}

	private void configureEditorOn() {
		rendererController.setEditor(textEditor);
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (!isEditable()
				|| !getTableView().isEditable()
				|| !getTableColumn().isEditable()) {
			return;
		}
		polygonEdited = rendererController.getPolygon();
		configureEditorOn();
	}

	@Override
	protected void updateItem(ObservablePolygon item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			return;
		}
		rendererController.setPolygon(item);
		setGraphic(renderer);
		if (isEditing()) {
			if (item == polygonEdited) {
				configureEditorOn();
			} else {
				configureEditorOff(true);
			}
		}
	}
}
