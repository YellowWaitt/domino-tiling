package application.view.polygondialog;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public abstract class PolygonType<T> {

	private String name;
	private Node renderer;
	protected DialogController<T> controller;

	public PolygonType(String name, String controllerPath) throws IOException {
		this.name = name;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PolygonType.class.getResource(controllerPath));
		renderer = loader.load();
		controller = loader.getController();
	}

	public abstract String getBoundary();

	public String getName() {
		return name;
	}

	public Node getRenderer() {
		return renderer;
	}

	public void reset() {
		controller.reset();
	}
}

/******************************************************************************/

class Custom extends PolygonType<String> {

	public Custom() throws IOException {
		super("Personnalisé", "custom/Custom.fxml");
	}

	@Override
	public String getBoundary() {
		return controller.getParameters();
	}
}

class Diamond extends PolygonType<Integer> {

	public Diamond() throws IOException {
		super("Diamant", "size/Size.fxml");
	}

	@Override
	public String getBoundary() {
		int size = controller.getParameters();
		return String.format("(dh)^%d (hg)^%d (gb)^%d (bd)^%d",
				size, size, size, size);
	}
}

class Hearth extends PolygonType<Integer> {

	public Hearth() throws IOException {
		super("Coeur", "size/Size.fxml");
	}

	@Override
	public String getBoundary() {
		int size = controller.getParameters();
		if (size == 0) {
			return "";
		}
		return String.format("(dh)^%d (hg)^%d (gb)^%d g (gh)^%d g (gb)^%d (bd)^%d",
				size * 2, size, size - 1, size - 1, size, size * 2);
	}
}

class Rectangle extends PolygonType<Integer[]> {

	public Rectangle() throws IOException {
		super("Rectangle", "rectangle/Rectangle.fxml");
	}

	@Override
	public String getBoundary() {
		Integer[] params = controller.getParameters();
		int width = params[0];
		int height = params[1];
		return String.format("d^%d h^%d g^%d b^%d", width, height, width, height);
	}
}

class Plus extends PolygonType<Integer> {

	public Plus() throws IOException {
		super("Plus", "size/Size.fxml");
	}

	@Override
	public String getBoundary() {
		int size = controller.getParameters();
		return String.format("d^%d h^%d d^%d h^%d g^%d h^%d g^%d b^%d g^%d"
				+ "b^%d d^%d b^%d",
				size, size , size , size, size, size, size, size, size, size,
				size, size);
	}
}

class Cross extends PolygonType<Integer> {

	public Cross() throws IOException {
		super("Croix", "size/Size.fxml");
	}

	@Override
	public String getBoundary() {
		int size = controller.getParameters();
		if (size == 0) {
			return "";
		}
		return String.format(
				"(dh)^%d (hg)^%d hh (dh)^%d (hg)^%d (gb)^%d gg (hg)^%d (gb)^%d"
				+ "(bd)^%d bb (gb)^%d (bd)^%d (dh)^%d dd (bd)^%d",
				size, size - 1, size - 1, size, size - 1, size - 1, size,
				size - 1, size - 1, size, size - 1, size - 1);
	}
}
