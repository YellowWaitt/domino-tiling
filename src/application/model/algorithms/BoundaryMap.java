package application.model.algorithms;

import java.awt.Point;
import java.util.Comparator;
import java.util.TreeMap;

public class BoundaryMap extends TreeMap<Point, Direction> {

	private static final long serialVersionUID = -3013022555730604965L;

	private static Comparator<? super Point> getPointComparator(
			HeightFunction function, boolean minimal) {
		if (minimal) {
			return (p1, p2) -> {
				int cmp = function.at(p2) - function.at(p1);
				if (cmp == 0) {
					cmp = Integer.compare(p1.x, p2.x);
				}
				if (cmp == 0) {
					cmp = Integer.compare(p1.y, p2.y);
				}
				return cmp;
			};
		} else {
			return (p1, p2) -> {
				int cmp = function.at(p1) - function.at(p2);
				if (cmp == 0) {
					cmp = Integer.compare(p1.x, p2.x);
				}
				if (cmp == 0) {
					cmp = Integer.compare(p1.y, p2.y);
				}
				return cmp;
			};
		}
	}


	private boolean minimal;

	public BoundaryMap(HeightFunction function, boolean minimal) {
		super(getPointComparator(function, minimal));
		this.minimal = minimal;
	}

	public boolean buildMinimal() {
		return minimal;
	}
}
