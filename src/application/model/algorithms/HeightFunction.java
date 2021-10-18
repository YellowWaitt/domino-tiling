package application.model.algorithms;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class HeightFunction implements Iterable<Point> {

	private Set<Point> domain;
	private Integer[][] function;
	private boolean wellDefined;
	private int dx;

	public HeightFunction(Polygon polygon) {
		this(polygon, true);
	}

	public HeightFunction(Polygon polygon, boolean buildMinimal) {
		Rectangle box = polygon.getBoundingBox();
		Point bottomLeft = polygon.getBottomLeft();
		dx = bottomLeft.x - box.x;
		function = new Integer[box.width + 4][box.height + 4];
		domain = new HashSet<>();
		wellDefined = buildInitialFunction(polygon, buildMinimal);
	}

	/**************************************************************************/

	private boolean addNewHeight(int x, int y, int newHeight) {
		return addNewHeight(new Point(x, y), newHeight);
	}

	private boolean addNewHeight(Point p, int newHeight) {
		Integer oldHeight = at(p);
		if (oldHeight == null) {
			domain.add(p);
			setHeight(p.x, p.y, newHeight);
		} else if (oldHeight != newHeight) {
			return false;
		}
		return true;
	}

	private void setHeight(int x, int y, int height) {
		function[x + 2 + dx][y + 2] = height;
	}

	/**************************************************************************/

	public Integer at(int x, int y) {
		return function[x + 2 + dx][y + 2];
	}

	public int at(int x, int y, int def) {
		Integer height = at(x, y);
		return height == null ? def : height;
	}

	public Integer at(Point p) {
		return at(p.x, p.y);
	}

	public int at(Point p, int def) {
		return at(p.x, p.y, def);
	}

	/**************************************************************************/

	// TODO A conserver ?
	public boolean isEqualTo(HeightFunction f) {
		if (domain.size() != f.getDomain().size()) {
			return false;
		}
		for (Point p : domain) {
			if (this.at(p) != f.at(p)) {
				return false;
			}
		}
		return true;
	}

	// TODO Mettre ça dans Sampler ?
	public boolean equalsOnDomain(HeightFunction f, List<Point> domain) {
		for (Point p : domain) {
			if (this.at(p) != f.at(p)) {
				return false;
			}
		}
		return true;
	}

	/**************************************************************************/

	// TODO Remplacer les tests par des asserts voir les supprimer ?
	public void flip(int x, int y) {
		if (isMaximumLocal(x, y)) {
			setHeight(x, y, at(x, y) - 4);
		}
		else if (isMinimumLocal(x, y)) {
			setHeight(x, y, at(x, y) + 4);
		}
	}

	public void flip(Point p) {
		flip(p.x, p.y);
	}

	public void flipDown(int x, int y) {
		if (isMaximumLocal(x, y)) {
			setHeight(x, y, at(x, y) - 4);
		}
	}

	public void flipDown(Point p) {
		flipDown(p.x, p.y);
	}

	public void flipUp(int x, int y) {
		if (isMinimumLocal(x, y)) {
			setHeight(x, y, at(x, y) + 4);
		}
	}

	public void flipUp(Point p) {
		flipUp(p.x, p.y);
	}

	/**************************************************************************/

	public Set<Point> getDomain() {
		return domain;
	}

	// TODO Mettre ça dans Polygon ?
	public List<Point> getInterior() {
		List<Point> points = new ArrayList<>(); 
		for (Point p : domain) {
			if (isInside(p)) {
				points.add(p);
			}
		}
		return points;
	}

	private boolean isInside(Point p) {
		for (int i = -1; i < 2; ++i) {
			for (int j = -1; j < 2; ++j) {
				if (at(p.x + i, p.y + j) == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**************************************************************************/

	public boolean isExtremumLocal(int x, int y) {
		return isMinimumLocal(x, y) || isMaximumLocal(x, y);
	}

	public boolean isExtremumLocal(Point p) {
		return isExtremumLocal(p.x, p.y);
	}

	public boolean isMaximumLocal(int x, int y) {
		int height = at(x, y, Integer.MIN_VALUE);
		int toCompare = at(x - 1, y, Integer.MAX_VALUE);
		if (height < toCompare) {
			return false;
		}
		toCompare = at(x, y - 1, Integer.MAX_VALUE);
		if (height < toCompare) {
			return false;
		}
		toCompare = at(x + 1, y, Integer.MAX_VALUE);
		if (height < toCompare) {
			return false;
		}
		toCompare = at(x, y + 1, Integer.MAX_VALUE);
		return height > toCompare;
	}

	public boolean isMaximumLocal(Point p) {
		return isMaximumLocal(p.x, p.y);
	}

	public boolean isMinimumLocal(int x, int y) {
		int height = at(x, y, Integer.MAX_VALUE);
		int toCompare = at(x - 1, y, Integer.MIN_VALUE);
		if (height > toCompare) {
			return false;
		}
		toCompare = at(x, y - 1, Integer.MIN_VALUE);
		if (height > toCompare) {
			return false;
		}
		toCompare = at(x + 1, y, Integer.MIN_VALUE);
		if (height > toCompare) {
			return false;
		}
		toCompare = at(x, y + 1, Integer.MIN_VALUE);
		return height < toCompare;
	}

	public boolean isMinimumLocal(Point p) {
		return isMinimumLocal(p.x, p.y);
	}

	/**************************************************************************/

	public boolean isWellDefined() {
		return wellDefined;
	}

	@Override
	public Iterator<Point> iterator() {
		return domain.iterator();
	}

	/**************************************************************************
	 *                    Construction of the function                        *
	 **************************************************************************/

	private boolean buildInitialFunction(Polygon polygon, boolean buildMinimal) {
		BoundaryMap boundaryMap = new BoundaryMap(this, buildMinimal);

		if (!setBoundaryValues(polygon, boundaryMap)) {
			return false;
		}
		return setInternalValues(boundaryMap);
	}

	private boolean setBoundaryValues(Polygon polygon, BoundaryMap boundaryMap) {
		Direction[] boundary = polygon.getNormalizedBoundary();
		int curY = 0, curX = 0;
		int curHeight = 1;
		int delta = 1;

		Point orig = new Point(0, 0);
		addNewHeight(orig, 0);
		boundaryMap.put(orig, boundary[0]);
		for(int i = 1; i < boundary.length; ++i) {
			switch (boundary[i - 1]) {
			case LEFT:
				--curX;
				break;
			case RIGHT:
				++curX;
				break;
			case DOWN:
				--curY;
				break;
			case UP: 
				++curY;
				break;
			default: break;
			}
			Point newPoint = new Point(curX, curY);
			addNewHeight(newPoint, curHeight);
			boundaryMap.put(newPoint, boundary[i]);

			if (boundary[i - 1] == boundary[i]) {
				delta = -delta;
			}
			curHeight += delta;
		}

		return addNewHeight(0, 0, curHeight);
	}

	private Point[] dominoBoundary(Point point, Direction dir) {
		Point[] domino = new Point[5];
		switch (dir) {
		case DOWN:
			domino[0] = new Point(point.x    , point.y + 1);
			domino[1] = new Point(point.x + 1, point.y + 1);
			domino[2] = new Point(point.x + 1, point.y);
			domino[3] = new Point(point.x + 1, point.y - 1);
			domino[4] = new Point(point.x    , point.y - 1);
			break;
		case LEFT:
			domino[0] = new Point(point.x + 1, point.y);
			domino[1] = new Point(point.x + 1, point.y - 1);
			domino[2] = new Point(point.x    , point.y - 1);
			domino[3] = new Point(point.x - 1, point.y - 1);
			domino[4] = new Point(point.x - 1, point.y);
			break;
		case RIGHT:
			domino[0] = new Point(point.x - 1, point.y);
			domino[1] = new Point(point.x - 1, point.y + 1);
			domino[2] = new Point(point.x    , point.y + 1);
			domino[3] = new Point(point.x + 1, point.y + 1);
			domino[4] = new Point(point.x + 1, point.y);
			break;
		case UP:
			domino[0] = new Point(point.x    , point.y - 1);
			domino[1] = new Point(point.x - 1, point.y - 1);
			domino[2] = new Point(point.x - 1, point.y);
			domino[3] = new Point(point.x - 1, point.y + 1);
			domino[4] = new Point(point.x    , point.y + 1);
			break;
		default: break;
		}
		return domino;
	}

	private boolean setInternalValues(BoundaryMap boundaryMap) {
		boolean success = true;

		while (!boundaryMap.isEmpty() && success) {
			Entry<Point, Direction> entry = boundaryMap.firstEntry();
			Point point = entry.getKey();
			Direction dir = entry.getValue();
			int height = at(point);
			boundaryMap.remove(point);

			Point[] domino = dominoBoundary(point, dir);
			if (boundaryMap.buildMinimal()) {
				success &= addNewHeight(domino[1], height - 2)
						&& addNewHeight(domino[3], height - 2)
						&& addNewHeight(domino[2], height - 3);
			} else {
				success &= addNewHeight(domino[1], height + 2)
						&& addNewHeight(domino[3], height + 2)
						&& addNewHeight(domino[2], height + 3);
			}
			if (!success) {
				return false;
			}

			Direction up = dir.left();
			Direction left = up.left();
			Direction down = left.left();
			success &= updateBoundary(boundaryMap, domino, dir, up, left, down);
		}

		return success;
	}

	private BoundaryMap[] splitBoundary(BoundaryMap boundaryMap, Point point,
			Direction newDir) {
		BoundaryMap newBoundary = new BoundaryMap(this, boundaryMap.buildMinimal());
		Direction olDir = boundaryMap.get(point);
		boundaryMap.put(point, newDir);

		int curX = point.x, curY = point.y;
		do {
			Point newPoint = new Point(curX, curY);
			Direction dir = boundaryMap.get(newPoint);
			newBoundary.put(newPoint, dir);
			boundaryMap.remove(newPoint);
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
		} while (curX != point.x || curY != point.y);

		boundaryMap.put(point, olDir);

		return new BoundaryMap[]{newBoundary, boundaryMap};
	}

	private boolean updateBoundary(BoundaryMap boundaryMap, Point[] domino,
			Direction right, Direction up, Direction left, Direction down) {
		boolean success = true;
		Point a0 = domino[0];
		Point a1 = domino[1];
		Point a2 = domino[2];
		Point a3 = domino[3];
		Point a4 = domino[4];

		if (boundaryMap.containsKey(a1) || boundaryMap.containsKey(a3)) {
			if (boundaryMap.get(a4) == up) {
				boundaryMap.remove(a4);
				success = caseA4Up(boundaryMap, domino, right, up, left, down);
			} else { // boundaryMap.get(a4) == right or down
				success = caseA4NotUp(boundaryMap, domino, right, up, left, down);
			}
		} else { // The domino does not intersect the boundary
			boundaryMap.put(a0, up);
			boundaryMap.put(a1, right);
			boundaryMap.put(a2, right);
			boundaryMap.put(a3, down);
		}

		return success;
	}

	private boolean caseA4Up(BoundaryMap boundaryMap, Point[] domino,
			Direction right, Direction up, Direction left, Direction down) {
		boolean success = true;
		Point a3 = domino[3];

		if (boundaryMap.get(a3) == left) {
			boundaryMap.remove(a3);
			success &= caseA3Left(boundaryMap, domino, right, up, left, down);
		} else { // boundaryMap.get(a3) == right or up
			success &= caseA3NotLeft(boundaryMap, domino, right, up, left, down);
		}
		return success;
	}

	private boolean caseA4NotUp(BoundaryMap boundaryMap, Point[] domino,
			Direction right, Direction up, Direction left, Direction down) {
		boolean success = true;
		Point a3 = domino[3];
		BoundaryMap[] splittedBoundary;

		if (boundaryMap.containsKey(a3)) {
			if (boundaryMap.get(a3) == left) {
				boundaryMap.put(a3, down);
				success &= caseA3Left(boundaryMap, domino, right, up, left, down);
			} else { // boundaryMap.get(a3) == up
				splittedBoundary = splitBoundary(boundaryMap, a3, down);
				success &= setInternalValues(splittedBoundary[0]);
				success &= caseA3NotLeft(splittedBoundary[1], domino, right, up, left, down);
			}
		} else {
			boundaryMap.put(a3, down);
			success &= caseA3NotLeft(boundaryMap, domino, right, up, left, down);
		}
		return success;
	}

	private boolean caseA3Left(BoundaryMap boundaryMap, Point[] domino,
			Direction right, Direction up, Direction left, Direction down ) {
		boolean success = true;
		Point a0 = domino[0];
		Point a1 = domino[1];
		Point a2 = domino[2];

		if (boundaryMap.get(a2) == left) {
			boundaryMap.remove(a2);
			caseA2Left(boundaryMap, up, down, a0, a1);
		} else { // boundaryMap.get(a2) == up
			success = caseA2NotLeft(boundaryMap, right, up, down, a0, a1);
		}
		return success;
	}

	private boolean caseA3NotLeft(BoundaryMap boundaryMap, Point[] domino,
			Direction right, Direction up, Direction left, Direction down) {
		boolean success = true;
		Point a0 = domino[0];
		Point a1 = domino[1];
		Point a2 = domino[2];

		if (boundaryMap.get(a2) == left) {
			boundaryMap.put(a2, right);
			caseA2Left(boundaryMap, up, down, a0, a1);
		} else { // a2 is not in the boundary
			boundaryMap.put(a2, right);
			success = caseA2NotLeft(boundaryMap, right, up, down, a0, a1);
		}
		return success;
	}

	private void caseA2Left(BoundaryMap boundaryMap, Direction up,
			Direction down, Point a0, Point a1) {
		if (boundaryMap.get(a1) == down) {
			boundaryMap.remove(a0);
			boundaryMap.remove(a1);
		} else { // boundaryMap.get(a1) == up or right
			boundaryMap.put(a0, up);
		}
	}

	private boolean caseA2NotLeft(BoundaryMap boundaryMap, Direction right,
			Direction up, Direction down, Point a0, Point a1) {
		boolean success = true;
		BoundaryMap[] splittedBoundary;

		if (boundaryMap.containsKey(a1)) {
			if (boundaryMap.get(a1) == down) {
				boundaryMap.remove(a0);
				boundaryMap.put(a1, right);
			} else { // boundaryMap.get(a1) == left
				boundaryMap.put(a0, up);
				splittedBoundary = splitBoundary(boundaryMap, a1, right);
				success &= setInternalValues(splittedBoundary[0])
						&& setInternalValues(splittedBoundary[1]);
			}
		} else {
			boundaryMap.put(a0, up);
			boundaryMap.put(a1, right);
		}
		return success;
	}
}
