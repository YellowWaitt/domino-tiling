package application.view.polygondialog.size;

import application.util.TextFieldUtil;
import application.view.polygondialog.DialogController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class SizeController implements DialogController<Integer> {

	@FXML
	private TextField sizeField;

	private TextFormatter<Integer> sizeFormat;

	@Override
	public Integer getParameters() {
		return sizeFormat.getValue() == null ? 0 : sizeFormat.getValue();
	}
	
	@FXML
	private void initialize() {
		sizeFormat = TextFieldUtil.integerFormat();
		sizeField.setTextFormatter(sizeFormat);
	}

	@Override
	public void reset() {
		sizeFormat.setValue(0);
	}
}
