package application.model.algorithms;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class Polygon {

	private Direction[] boundary;
	private Direction[] normalizedBoundary;
	private Rectangle boundingBox;
	private Point bottomLeft;
	private boolean boundaryValid;

	private HeightFunction heightFunction;

	public Polygon(String boundaryWord) {
		initialize(boundaryWord);
	}

	public Point getBottomLeft() {
		return bottomLeft;
	}

	public Direction[] getBoundary() {
		return boundary;
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public HeightFunction getHeightFunction() {
		return heightFunction;
	}

	public Direction[] getNormalizedBoundary() {
		return normalizedBoundary;
	}

	public boolean isBoundaryValid() {
		return boundaryValid;
	}

	public boolean isTilleable() {
		return heightFunction != null && heightFunction.isWellDefined();
	}

	void setHeightFunction(HeightFunction heightFunction) {
		this.heightFunction = heightFunction;
	}

	/**************************************************************************
	 *                        Initialization                                  *
	 **************************************************************************/

	protected final void initialize(String boundaryWord) {
		translateBoundaryWord(boundaryWord);
		boundaryValid = boundaryWord.length() != 0 && boundaryIsValid();
		int start = computeBoxAndBottom();
		if (boundaryValid) {
			normalizeBoundary(start);
		} else {
			normalizedBoundary = new Direction[0];
		}
		heightFunction = boundaryValid ? new HeightFunction(this) : null;
	}

	private void translateBoundaryWord(String boundaryWord) {
		boundary = new Direction[boundaryWord.length()];
		int i = 0;
		while (i < boundaryWord.length()) {
			boundary[i] = Direction.getDirectionFromChar(boundaryWord.charAt(i));
			++i;
		}
	}

	private boolean boundaryIsValid() {
		Set<Point> boundarySet = new HashSet<>();
		boolean valid = true;
		int curY = 0, curX = 0;
		for (Direction dir : boundary) {
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
			default:
				valid = false;
				break;
			}
			if (!boundarySet.add(new Point(curX, curY))) {
				return false;
			}
		}
		return valid && curY == 0 && curX == 0;
	}

	private int computeBoxAndBottom() {
		int minDown = 0, maxUp = 0, minLeft = 0, maxRight = 0, minDownLeft = 0;
		int curY = 0, curX = 0;
		int start = 0;

		for (int i = 0; i < boundary.length; ++i) {
			switch (boundary[i]) {
			case UP:
				++curY;
				if (maxUp < curY) {
					maxUp = curY;
				}
				break;
			case DOWN:
				--curY;
				if (minDown > curY) {
					minDown = curY;
					minDownLeft = curX;
					start = i;
				} else if (minDown == curY && minDownLeft > curX) {
					minDownLeft = curX;
					start = i;
				}
				break;
			case RIGHT:
				++curX;
				if (maxRight < curX) {
					maxRight = curX;
				}
				break;
			case LEFT:
				--curX;
				if (minDown == curY && minDownLeft > curX) {
					minDownLeft = curX;
					start = i;
				}
				if (minLeft > curX) {
					minLeft = curX;
				}
				break;
			default: break;
			}
		}

		boundingBox = new Rectangle(minLeft, minDown, maxRight - minLeft,
				maxUp - minDown);
		bottomLeft = new Point(minDownLeft, minDown);

		return start;
	}

	private void normalizeBoundary(int start) {
		int boundarySize = boundary.length;
		normalizedBoundary = new Direction[boundarySize];

		if (bottomLeft.x != 0 || bottomLeft.y != 0) {
			++start;
		}
		int endPartSize = (boundarySize - start);

		if (boundary[start] == Direction.RIGHT) {
			for (int i = 0; i < endPartSize; ++i) {
				normalizedBoundary[i] = boundary[i + start];
			}
			for (int i = endPartSize; i < boundarySize; ++i) {
				normalizedBoundary[i] = boundary[i - endPartSize];
			}
		} else { // boundary[start] == Direction.UP
			for (int i = 0; i < endPartSize; ++i) {
				normalizedBoundary[boundarySize - i - 1] =
						boundary[i + start].opposite();
			}
			for (int i = endPartSize; i < boundarySize; ++i) {
				normalizedBoundary[boundarySize - i - 1] =
						boundary[i - endPartSize].opposite();
			}
		}
	}
}
