package application;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.model.adapter.ObservablePolygon;
import application.model.adapter.WrapperPolygonList;
import application.model.algorithms.Sampler;
import application.util.DialogsUtil;
import application.util.FilesUtil;
import application.util.LogsUtil;
import application.view.drawer.Drawer;
import application.view.polygondialog.PolygonDialog;
import application.view.polygonlist.PolygonList;
import application.view.rootlayout.RootLayout;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MainApp extends Application {

	public static void main(String[] args) {
		LogsUtil.init();
		launch(args);
	}

	/**************************************************************************/

	// Graphic variables
	private Stage primaryStage;
	private Drawer drawer;
	private PolygonDialog newPolygonDialog;

	// Data variables
	private ObservableList<ObservablePolygon> polygonData =
			FXCollections.observableArrayList();
	private ObservablePolygon polygon;
	private Sampler sampler = new Sampler();


	public MainApp() {}

	/**************************************************************************/

	@Override
	public void stop() {
		LogsUtil.logger.info("Fin de l'application");
	}

	public void exit() {
		File file = getPolygonFilePath();
		if (file != null) {
			savePolygons(file);
		}
		Platform.exit();
	}

	/**************************************************************************/

	@Override
	public void start(Stage primaryStage) {
		LogsUtil.logger.info("Démarrage de l'application");

		initPrimaryStage(primaryStage);

		File file = getPolygonFilePath();
		if (file != null) {
			loadPolygons(file);
		}

		Drawer widgetDrawer = initDrawer();
		PolygonList widgetList = initPolygonList();
		initRootLayout(widgetList, widgetDrawer);
		initPolygonDialog();

		this.primaryStage.show();
	}

	private void startError(Exception e) {
		DialogsUtil.exceptionAlert("Erreur",
				"Lancement de l'application",
				"Impossible de démarrer l'application",
				e
				);
		exit();
	}

	private void initPrimaryStage(Stage primaryStage) {
		DialogsUtil.setWindow(primaryStage);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Pavages");
		this.primaryStage.getIcons().add(FilesUtil.getIcon());
		this.primaryStage.setMinHeight(400);
		this.primaryStage.setMinWidth(600);
		this.primaryStage.setOnCloseRequest(evt -> exit());
	}

	private Drawer initDrawer() {
		drawer = new Drawer();
		return drawer;
	}

	private void initPolygonDialog() {
		try {
			newPolygonDialog = new PolygonDialog(this);
		} catch (IOException e) {
			LogsUtil.logger.log(Level.SEVERE, "Chargement polygonDialog", e);
			startError(e);
		}
	}

	private PolygonList initPolygonList() {
		PolygonList widgetList = null;
		try {
			widgetList = new PolygonList(this);
		} catch (Exception e) {
			LogsUtil.logger.log(Level.SEVERE, "Chargement polygonList", e);
			startError(e);
		}
		return widgetList;
	}

	private void initRootLayout(PolygonList widgetList, Drawer widgtDrawer) {
		try {
			new RootLayout(this, widgetList, widgtDrawer);
		} catch (Exception e) {
			LogsUtil.logger.log(Level.SEVERE, "Chargement rootLayout", e);
			startError(e);
		}
	}

	/**************************************************************************/

	public void loadPolygons(File file) {
		LogsUtil.logger.log(Level.INFO, "Chargement polygones");
		try {
			JAXBContext context = JAXBContext
					.newInstance(WrapperPolygonList.class);
			Unmarshaller um = context.createUnmarshaller();
			WrapperPolygonList wrapper = (WrapperPolygonList) um.unmarshal(file);
			if (wrapper.getPolygons() != null) {
				polygonData.addAll(wrapper.getPolygons());
			}
		} catch (Exception e) {
			LogsUtil.logger.log(Level.WARNING, "Chargement des polygones", e);
			DialogsUtil.exceptionAlert("Erreur",
					"Chargement de la liste des polygones",
					"Erreur lors du chargement de la liste des polygones",
					e
					);
		}
	}

	public void savePolygons(File file) {
		LogsUtil.logger.log(Level.INFO, "Sauvegarde des polygones");
		try {
			JAXBContext context = JAXBContext
					.newInstance(WrapperPolygonList.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			WrapperPolygonList wrapper = new WrapperPolygonList();
			wrapper.setPolygons(polygonData);
			m.marshal(wrapper, file);
		} catch (Exception e) {
			LogsUtil.logger.log(Level.WARNING, "Sauvegarde polygones", e);
			DialogsUtil.exceptionAlert("Erreur",
					"Sauvegarde de la liste des polygones",
					"Erreur lors de la sauvegarde de la liste des polygones",
					e
					);
		}
	}

	/**************************************************************************/

	public File getPolygonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		return filePath == null ? null : new File(filePath);
	}

	public void setPolygonFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle("Pavages - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("Pavages");
		}
	}

	/**************************************************************************/

	// TODO Faire un truc plus propre
	public void sample(int algo) {
		if (polygon == null) {
			DialogsUtil.noSelection();
			return;
		}
		if (!polygon.isTilleable()) {
			DialogsUtil.simpleAlert(AlertType.WARNING,
					"Action impossible",
					"Polygone non pavable",
					"Les mélanges ne sont pas possible pour les régions non pavable");
			return;
		}
		Service<Void> service = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>(){
					@Override
					protected Void call() throws Exception {
						primaryStage.getScene().setCursor(Cursor.WAIT);
//						long start = System.currentTimeMillis();
						if (algo == -1) {
							sampler.minimalTiling(polygon);
						} else if (algo == 0) {
							sampler.maximalTiling(polygon);
						} else if (algo == 1) {
							sampler.sampleByFlip(polygon);
						} else if (algo == 2) {
							sampler.sampleByPoint(polygon);
						} else if (algo == 3) {
							sampler.sampleByMarkov(polygon);
						}
//						long stop = System.currentTimeMillis();
						drawer.setPolygon(polygon);
//						System.out.println((stop - start) + " millisecondes");
						primaryStage.getScene().setCursor(Cursor.DEFAULT);
						return null;
					}
				};
			}
		};
		service.start();
	}

	/**************************************************************************/

	public void addPolygon() {
		String boundary = newPolygonDialog.getBoundary();
		if (boundary == null || boundary.length() == 0) {
			return;
		}
		LogsUtil.logger.info("Ajout d'un polygone");
		ObservablePolygon newPolygon = new ObservablePolygon(boundary);
		polygonData.add(newPolygon);
	}

	public void setCurrentPolygon(ObservablePolygon polygon) {
		this.polygon = polygon;
		drawer.setPolygon(polygon);
//		System.out.println("===========================");
	}

	/**************************************************************************/

	public ObservableList<ObservablePolygon> getPolygonData() {
		return polygonData;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
