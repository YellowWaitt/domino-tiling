package application.view.polygondialog.rectangle;

import application.util.TextFieldUtil;
import application.view.polygondialog.DialogController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class RectangleController implements DialogController<Integer[]> {

	@FXML
	private TextField widthField;
	@FXML
	private TextField heightField;

	private TextFormatter<Integer> widthFormat;
	private TextFormatter<Integer> heightFormat;

	@Override
	public Integer[] getParameters() {
		Integer[] ret = new Integer[2];
		ret[0] = widthFormat.getValue() == null ? 0 : widthFormat.getValue();
		ret[1] = heightFormat.getValue() == null ? 0 : heightFormat.getValue();
		return ret;
	}

	@FXML
	private void initialize() {
		widthFormat = TextFieldUtil.integerFormat();
		widthField.setTextFormatter(widthFormat);

		heightFormat = TextFieldUtil.integerFormat();
		heightField.setTextFormatter(heightFormat);
	}

	@Override
	public void reset() {
		widthFormat.setValue(0);
		heightFormat.setValue(0);
	}
}
