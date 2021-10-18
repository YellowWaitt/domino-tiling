package application.view.drawer;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

public abstract class ResizableCanvas extends Canvas {

	public abstract void draw();

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double prefHeight(double width) {
		return getHeight();
	}

	@Override
	public double prefWidth(double height) {
		return getWidth();
	}

	public void setRegion(Region region) {
		widthProperty().bind(region.widthProperty());
		heightProperty().bind(region.heightProperty());
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
	}
}