package application.view.polygondialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PolygonDialogController {
	@FXML
	private Pane questionPane;
	@FXML
	private ComboBox<PolygonType<?>> choices;

	private PolygonType<?> currentType;
	private List<PolygonType<?>> types;

	private Stage stage;
	private boolean confirmClicked;

	public PolygonDialogController() throws IOException {
		types = new ArrayList<>();
		types.add(new Custom());
		types.add(new Rectangle());
		types.add(new Diamond());
		types.add(new Hearth());
		types.add(new Plus());
		types.add(new Cross());
	}

	public String getBoundary() {
		return confirmClicked ? currentType.getBoundary() : null;
	}

	@FXML
	private void handleCancel() {
		stage.close();
	}

	@FXML
	private void handleCancelKey(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			event.consume();
			handleCancel();
		}
	}

	@FXML
	private void handleConfirm() {
		confirmClicked = true;
		stage.close();
	}

	@FXML
	private void handleConfirmKey(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			event.consume();
			handleConfirm();
		}
	}

	@FXML
	private void initialize() {
		choices.getItems().setAll(types);
		choices.valueProperty().addListener(
				(observable, oldValue, newValue) ->
				setController(newValue)
				);
		choices.setButtonCell(new ChoicesCell());
		choices.setCellFactory(listView -> new ChoicesCell());
		reset();
	}

	public void reset() {
		choices.setValue(types.get(0));
		for (PolygonType<?> polygonType : types) {
			polygonType.reset();
		}
		confirmClicked = false;
	}

	private void setController(PolygonType<?> newValue) {
		currentType = newValue;
		questionPane.getChildren().setAll(currentType.getRenderer());
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}


	private class ChoicesCell extends ListCell<PolygonType<?>> {
		@Override
		protected void updateItem(PolygonType<?> item, boolean empty) {
			super.updateItem(item, empty);
			if (!empty && item != null) {
				setText(item.getName());
			} 
		}
	}
}
