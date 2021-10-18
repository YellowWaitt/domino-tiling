package application.util;

import javafx.scene.image.Image;

public class FilesUtil {

	public static Image getIcon() {
		return new Image("file:resources/images/icon_32.png");
	}
	
	public static String getLogFilename() {
		return "resources/logs/logs.txt";
	}
	
	private FilesUtil() {}
}
