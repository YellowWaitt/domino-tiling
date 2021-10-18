package application.view.drawer;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.model.adapter.ObservablePolygon;
import application.model.algorithms.Direction;
import application.model.algorithms.HeightFunction;

public class DrawerController {

	private ObservablePolygon polygon;
	private boolean bottomLeftWhite;
	private Map<Point, Integer> heights;
	private List<Rectangle> tiling;
	private double edge;
	private double borderX;
	private double borderY;

	public DrawerController() {
		this(null);
	}

	public DrawerController(ObservablePolygon polygon) {
		setPolygon(polygon);
	}

	/**************************************************************************/

	public void flip(double clicX, double clicY, double delta) {
		Rectangle box = polygon.getBoundingBox();
		Point bottom = polygon.getBottomLeft();
		int dx = box.x - bottom.x;
		int x = dx + (int) Math.round((clicX - borderX) / edge);
		int y = box.height - (int) Math.round((clicY - borderY) / edge);
		double cx = (x - dx) * edge + borderX;
		double cy = (box.height - y) * edge + borderY;
		if (clicX - delta < cx && cx < clicX + delta
				&& clicY - delta < cy && cy < clicY + delta) {
			HeightFunction f = polygon.getHeightFunction();
			if (f.isExtremumLocal(x, y)) {
				f.flip(x, y);
				Point p = new Point(x + bottom.x, -y - box.y);
				heights.put(p, f.at(x, y));
				computeTiling();
			}
		}
	}

	public double[][] getBoundaryPoints(double edge, double origX, double origY) {
		int nPoints = polygon.getBoundary().length + 1;
		double[] xPoints = new double[nPoints];
		double[] yPoints = new double[nPoints];
		xPoints[0] = origX;
		yPoints[0] = origY;

		int i = 1;
		for (Direction d : polygon.getBoundary()) {
			switch (d) {
			case RIGHT:
				xPoints[i] = xPoints[i - 1] + edge;
				yPoints[i] = yPoints[i - 1];
				break;
			case LEFT:
				xPoints[i] = xPoints[i - 1] - edge;
				yPoints[i] = yPoints[i - 1];
				break;
			case UP:
				xPoints[i] = xPoints[i - 1];
				yPoints[i] = yPoints[i - 1] - edge;
				break;
			case DOWN:
				xPoints[i] = xPoints[i - 1];
				yPoints[i] = yPoints[i - 1] + edge;
				break;
			default: break;
			}
			++i;
		}

		return new double[][]{xPoints, yPoints};
	}

	public double[] getEdgeAndOrig(double width, double height,
			int padx, int pady) {
		Rectangle box = polygon.getBoundingBox();

		double nX = (width - padx) / box.width;
		double nY = (height - pady) / box.height;
		edge = Double.min(nX, nY);

		borderX = (width - edge * box.width) / 2;
		borderY = (height - edge * box.height) / 2;
		
		double origX = borderX - box.x * edge;
		double origY = height - borderY + box.y * edge;

		return new double[]{edge, origX, origY};
	}

	public Map<Point, Integer> getHeightsValue() {
		return heights;
	}

	public List<Rectangle> getTiling() {
		return tiling;
	}

	public boolean isBottomLeftWhite() {
		return bottomLeftWhite;
	}

	public boolean isPolygonNull() {
		return polygon == null;
	}

	public boolean isWhite(int x, int y) {
		Point orig = polygon.getBottomLeft();
		return ((x + orig.x) % 2 == 0) == ((y + orig.y) % 2 == 0);
	}

	/**************************************************************************/

	public void setPolygon(ObservablePolygon polygon) {
		this.polygon = polygon;
		if (polygon == null) {
			return;
		}

		Point bottom = polygon.getBottomLeft();
		bottomLeftWhite = (bottom.x % 2 == 0) == (bottom.y % 2 == 0);

		computeHeights();
		computeTiling();
	}

	private void computeHeights() {
		HeightFunction function = polygon.getHeightFunction();

		heights = new HashMap<>();

		if (function == null) {
			return;
		}

		Point bottom = polygon.getBottomLeft();
		Rectangle box = polygon.getBoundingBox();

		for (Point p : function) {
			Point newPoint = new Point(p.x + bottom.x, -p.y - box.y);
			heights.put(newPoint, function.at(p));
		}
	}

	private void computeTiling() {
		tiling = new ArrayList<>();

		if (!polygon.isTilleable()) {
			return;
		}

		Map<Point, Direction> boundaryMap = getNormalizedBoundaryMap();

		HeightFunction function = polygon.getHeightFunction();
		Rectangle box = polygon.getBoundingBox();
		Point bottom = polygon.getBottomLeft();

		for (Point p : function) {
			int h = function.at(p);
			Direction dir = boundaryMap.get(p);

			Integer height = function.at(p.x - 1, p.y);
			if (height != null && Math.abs(height - h) == 3
					&& dir != Direction.DOWN
					&& boundaryMap.get(new Point(p.x, p.y + 1)) != Direction.DOWN) {
				tiling.add(new Rectangle(
						p.x - 1 + bottom.x,
						-p.y - 1 - box.y,
						1, 2));
				continue;
			}

			height = function.at(p.x, p.y - 1);
			if (height != null && Math.abs(height - h) == 3
					&& dir != Direction.RIGHT
					&& boundaryMap.get(new Point(p.x - 1, p.y)) != Direction.RIGHT) {
				tiling.add(new Rectangle(
						p.x - 1 + bottom.x,
						-p.y - box.y,
						2, 1));
			}
		}
	}

	private Map<Point, Direction> getNormalizedBoundaryMap() {
		int curX = 0, curY = 0;
		Map<Point, Direction> boundaryMap = new HashMap<>();
		for (Direction dir : polygon.getNormalizedBoundary()) {
			boundaryMap.put(new Point(curX, curY), dir);
			switch (dir) {
			case DOWN:
				--curY;
				break;
			case LEFT:
				--curX;
				break;
			case RIGHT:
				++curX;
				break;
			case UP:
				++curY;
				break;
			default: break;
			}
		}
		return boundaryMap;
	}

	/**************************************************************************/
}
