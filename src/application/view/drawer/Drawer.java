package application.view.drawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import application.model.adapter.ObservablePolygon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class Drawer extends ResizableCanvas {

	private DrawerController controller = new DrawerController();
	private DrawerSettings settings = new DrawerSettings(this);

	public Drawer() {
		super();
		addEventHandler(MouseEvent.MOUSE_CLICKED,
				mouseEvent -> {
					if (settings.drawTilingProperty.get()) {
						controller.flip(mouseEvent.getX(), mouseEvent.getY(), 20);
						draw();
					}
				});
	}

	/*************************************************************************/

	@Override
	public void draw() {
		double width = getWidth();
		double height = getHeight();

		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(settings.backgroundColor);
		gc.fillRect(0, 0, width, height);

		if (controller.isPolygonNull()) {
			return;
		}

		double[] datas = controller.getEdgeAndOrig(width, height,
				settings.padx, settings.pady);
		double edge = datas[0];
		double origX = datas[1];
		double origY = datas[2];

		if (settings.drawCheckerBoardProperty.get()) {
			drawCheckerboard(gc, edge, origX, origY);
		} else if (settings.drawGridProperty.get()) {
			drawGrid(gc, edge, origX, origY);
		}

		if (settings.drawColoredTilingProperty.get()) {
			drawColoredTiling(gc, edge, origX, origY);
		} else if (settings.drawTilingProperty.get()) {
			drawTiling(gc, edge, origX, origY);
		} else {
			drawPolygon(gc, edge, origX, origY);
		}

		if (settings.drawHeightFunctionProperty.get()) {
			drawHeightFunction(gc, edge, origX, origY);
		}
	}

	private void drawGrid(GraphicsContext gc, double edge, double origX,
			double origY) {
		double width = getWidth();
		double height = getHeight();
		gc.setStroke(settings.gridColor);
		gc.setLineWidth(settings.gridLineWidth);
		for (double x = origX; x > 0; x -= edge) {
			gc.strokeLine(x, 0, x, height);
		}
		for (double x = origX + edge; x < width; x += edge) {
			gc.strokeLine(x, 0, x, height);
		}
		for (double y = origY; y > 0; y -= edge) {
			gc.strokeLine(0, y, width, y);
		}
		for (double y = origY + edge; y < height; y += edge) {
			gc.strokeLine(0, y, width, y);
		}
	}

	private void drawCheckerboard(GraphicsContext gc, double edge, double origX,
			double origY) {
		double widthLimit = getWidth() + edge;
		double edge2 = 2 * edge;
		gc.setFill(settings.checkerboardColor);

		for (double x = origX; x > -edge; x -= edge2) {
			drawBoardLine(gc, edge, origY, x);
		}
		for (double x = origX + edge2; x < widthLimit; x += edge2) {
			drawBoardLine(gc, edge, origY, x);
		}
	}

	private void drawBoardLine(GraphicsContext gc, double edge, double origY,
			double x) {
		double heightLimit = getHeight() + edge;
		boolean bottomLeftWhite = controller.isBottomLeftWhite();
	
		double deltaX = bottomLeftWhite ? 0.0 : -edge;
		for (double y = origY; y > -edge; y -= edge) {
			gc.fillRect(x + deltaX, y, edge, edge);
			deltaX = -deltaX - edge;
		}
	
		deltaX = bottomLeftWhite ? -edge : 0.0;
		for (double y = origY + edge; y < heightLimit; y += edge) {
			gc.fillRect(x + deltaX, y, edge, edge);
			deltaX = -deltaX - edge;
		}
	}

	private void drawPolygon(GraphicsContext gc, double edge, double origX,
			double origY) {
		double[][] points = controller.getBoundaryPoints(edge, origX, origY);
		double[] xPoints = points[0];
		double[] yPoints = points[1];
		gc.setStroke(settings.polygonColor);
		gc.setLineWidth(settings.polygonLineWidth);
		gc.strokePolyline(xPoints, yPoints, xPoints.length);
	}

	private void drawHeightFunction(GraphicsContext gc, double edge, double origX,
			double origY) {
		Map<Point, Integer> heights = controller.getHeightsValue();
		gc.setStroke(settings.heightColor);
		gc.setLineWidth(settings.heightLineWidth);
		for (Entry<Point, Integer> entry : heights.entrySet()) {
			Point p = entry.getKey();
			gc.strokeText(entry.getValue().toString(),
					p.x * edge + origX,
					p.y * edge + origY);
		}
	}

	private void drawTiling(GraphicsContext gc, double edge, double origX,
			double origY) {
		List<Rectangle> dominoes = controller.getTiling();
		gc.setStroke(settings.polygonColor);
		gc.setLineWidth(settings.polygonLineWidth);
		for (Rectangle domino : dominoes) {
			gc.strokeRect(domino.x * edge + origX,
					domino.y * edge + origY,
					domino.width * edge,
					domino.height * edge);
		}
	}

	private void drawColoredTiling(GraphicsContext gc, double edge, double origX,
			double origY) {
		List<Rectangle> dominoes = controller.getTiling();
		for (Rectangle domino : dominoes) {
			if (controller.isWhite(domino.x, domino.y)) {
				if (domino.width == 1) {
					gc.setFill(settings.tilingColor1);
				} else {
					gc.setFill(settings.tilingColor2);
				}
			} else {
				if (domino.width == 1) {
					gc.setFill(settings.tilingColor3);
				} else {
					gc.setFill(settings.tilingColor4);
				}
			}
			gc.fillRect(domino.x * edge + origX,
					domino.y * edge + origY,
					domino.width * edge,
					domino.height * edge);
		}
	}

	/*************************************************************************/

	public DrawerSettings getSettings() {
		return settings;
	}

	public void setPolygon(ObservablePolygon polygon) {
		controller.setPolygon(polygon);
		draw();
	}
}
