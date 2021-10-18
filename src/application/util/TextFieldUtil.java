package application.util;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.converter.IntegerStringConverter;

public class TextFieldUtil {

	public static TextFormatter<Integer> integerFormat() {
		UnaryOperator<Change> integerFilter = change -> {
			String input = change.getText();
			if (input.matches("[0-9]*")) { 
				return change;
			}
			return null;
		};
		return new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter);
	}
	
	public static TextFormatter<String> boundaryFormat() {
		UnaryOperator<Change> boundaryFilter = change -> {
			String input = change.getText();
			if (input.matches("[bdhg()^0-9 ]*")) { 
				return change;
			}
			return null;
		};
		return new TextFormatter<>(boundaryFilter);
	}

	private TextFieldUtil() {}
}
