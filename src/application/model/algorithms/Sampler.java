package application.model.algorithms;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Sampler {

	private Random random = new Random();

	/**************************************************************************/

	private boolean flipsEquals(HeightFunction f, Set<Point> flipsF,
			HeightFunction g, Set<Point> flipsG) {
		if (flipsF.size() != flipsG.size()) {
			return false;
		}
		for (Point p : flipsF) {
			if (f.at(p) != g.at(p)) {
				return false;
			}
		}
		return true;
	}

	private void getFlips(HeightFunction f, Set<Point> set) {
		for (Point p : f) {
			if (f.isExtremumLocal(p)) {
				set.add(p);
			}
		}
	}

	private void updateFlip(HeightFunction f, Set<Point> flips, Point toFlip) {
		if (f.isExtremumLocal(toFlip.x - 1, toFlip.y)) {
			flips.add(new Point(toFlip.x - 1, toFlip.y));
		} else {
			flips.remove(new Point(toFlip.x - 1, toFlip.y));
		}
		if (f.isExtremumLocal(toFlip.x + 1, toFlip.y)) {
			flips.add(new Point(toFlip.x + 1, toFlip.y));
		} else {
			flips.remove(new Point(toFlip.x + 1, toFlip.y));
		}
		if (f.isExtremumLocal(toFlip.x, toFlip.y - 1)) {
			flips.add(new Point(toFlip.x, toFlip.y - 1));
		} else {
			flips.remove(new Point(toFlip.x, toFlip.y - 1));
		}
		if (f.isExtremumLocal(toFlip.x, toFlip.y + 1)) {
			flips.add(new Point(toFlip.x, toFlip.y + 1));
		} else {
			flips.remove(new Point(toFlip.x, toFlip.y + 1));
		}
	}

	/**************************************************************************/

	public void maximalTiling(Polygon polygon) {
		polygon.setHeightFunction(new HeightFunction(polygon, false));
	}

	public void minimalTiling(Polygon polygon) {
		polygon.setHeightFunction(new HeightFunction(polygon, true));
	}

	/**************************************************************************/

	private HeightFunction sampleByFlip(HeightFunction f, HeightFunction g) {
		List<Point> interior = f.getInterior();
		int nbstep = 1;
		int exp = 0;
		while (!f.equalsOnDomain(g, interior)) {
			for (int step = 0; step < nbstep; ++step) {
				Point p = interior.get(random.nextInt(interior.size()));
				if (f.isExtremumLocal(p)) {
					if (g.isExtremumLocal(p)) {
						if (random.nextBoolean()) {
							f.flipDown(p);
							g.flipDown(p);
						} else {
							f.flipUp(p);
							g.flipUp(p);
						}
					} else {
						if (random.nextBoolean()) {
							f.flip(p);
						}
					}
				} else if (g.isExtremumLocal(p)) {
					if (random.nextBoolean()) {
						g.flip(p);
					}
				}
			}
			if (exp < 32) {
				nbstep *= 2;
				++exp;
			}
		}
		return f;
	}

	private HeightFunction sampleByFlip2(HeightFunction f, HeightFunction g) {
		Set<Point> flipsF = new HashSet<>();
		Set<Point> flipsG = new HashSet<>();
		getFlips(f, flipsF);
		getFlips(g, flipsG);

		List<Point> interior = f.getInterior();
		while (!flipsEquals(f, flipsF, g, flipsG)) {
			Point p = interior.get(random.nextInt(interior.size()));
			if (f.isExtremumLocal(p)) {
				if (g.isExtremumLocal(p)) {
					if (random.nextBoolean()) {
						f.flipDown(p);
						g.flipDown(p);
					} else {
						f.flipUp(p);
						g.flipUp(p);
					}
					updateFlip(f, flipsF, p);
					updateFlip(g, flipsG, p);
				} else {
					if (random.nextBoolean()) {
						f.flip(p);
						updateFlip(f, flipsF, p);
					}
				}
			} else if (g.isExtremumLocal(p)) {
				if (random.nextBoolean()) {
					g.flip(p);
					updateFlip(g, flipsG, p);
				}
			}
		}
		return f;
	}

	public void sampleByFlip(Polygon polygon) {
		HeightFunction newFunction = sampleByFlip(
				new HeightFunction(polygon, true), 
				new HeightFunction(polygon, false));
		polygon.setHeightFunction(newFunction);
	}
	
	public void sampleByFlip2(Polygon polygon) {
		HeightFunction newFunction = sampleByFlip2(
				new HeightFunction(polygon, true), 
				new HeightFunction(polygon, false));
		polygon.setHeightFunction(newFunction);
	}

	/**************************************************************************/

	private HeightFunction sampleByMarkov(HeightFunction f, int nbStep) {
		RandomSet<Point> flips = new RandomSet<>();

		getFlips(f, flips);

		for (int step = 0; step < nbStep; ++step) {
			Point toFlip = flips.getRandom(random);
			f.flip(toFlip);
			updateFlip(f, flips, toFlip);
		}

		return f;
	}

	public void sampleByMarkov(Polygon polygon) {
		HeightFunction newFunction = sampleByMarkov(
				new HeightFunction(polygon),
				10_000_000);
		polygon.setHeightFunction(newFunction);
	}

	/**************************************************************************/

	private HeightFunction sampleByPoint(HeightFunction f, int nbStep) {
		List<Point> interior = f.getInterior();
		for (int step = 0; step < nbStep; ++step) {
			Point p = interior.get(random.nextInt(interior.size()));
			if (f.isExtremumLocal(p) && random.nextBoolean()) {
				f.flip(p);
			}
		}
		return f;
	}

	public void sampleByPoint(Polygon polygon) {
		HeightFunction newFunction = sampleByPoint(
				new HeightFunction(polygon),
				10_000_000);
		polygon.setHeightFunction(newFunction);
	}

	/**************************************************************************/
}
