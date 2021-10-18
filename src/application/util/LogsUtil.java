package application.util;

import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogsUtil {

	public static final Logger logger = Logger.getLogger("PavageLogger");

	public static void init() {
		logger.setLevel(Level.ALL);
		try {
			System.setErr(new PrintStream(FilesUtil.getLogFilename()));
			FileHandler fh = new FileHandler(FilesUtil.getLogFilename());
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			logger.setUseParentHandlers(false);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Initialisation du fichier log", e);
		}
		// To remove some warnings
		// Logging.getJavaFXLogger().setLevel(PlatformLogger.Level.SEVERE);
	}

	private LogsUtil() {}
}
