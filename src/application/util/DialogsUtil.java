package application.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DialogsUtil {

	private static Window window;

	public static void exceptionAlert(String title, String header, String content,
			Exception ex) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		setOwner(alert);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Trace de l'exception:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	public static FileChooser fileChooserXML() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}

	public static void noSelection() {
		simpleAlert(AlertType.WARNING,
				"Aucune selection",
				"Aucun polygone de sélectionné",
				"Vous devez choisir un polygone."
				);
	}

	private static void setOwner(Alert alert) {
		if (window != null && window.isShowing()) {
			alert.initOwner(window);
		} else {
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(FilesUtil.getIcon());
		}
	}

	public static void setWindow(Window window) {
		DialogsUtil.window = window;
	}
	
	public static void simpleAlert(Alert.AlertType type, String title,
			String header, String content) {
		Alert alert = new Alert(type);
		setOwner(alert);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private DialogsUtil() {}
}
