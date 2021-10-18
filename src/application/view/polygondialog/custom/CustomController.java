package application.view.polygondialog.custom;

import application.util.TextFieldUtil;
import application.view.polygondialog.DialogController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CustomController implements DialogController<String> {
	@FXML
	private TextField boundaryField;
	
	@Override
	public String getParameters() {
		return boundaryField.getText();
	}

	@FXML
	private void initialize() {
		boundaryField.setTextFormatter(TextFieldUtil.boundaryFormat());
	}

	@Override
	public void reset() {
		boundaryField.setText("");
	}
}
