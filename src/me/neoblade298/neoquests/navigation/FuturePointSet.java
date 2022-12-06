package me.neoblade298.neoquests.navigation;

import java.util.Iterator;
import java.util.LinkedList;

public class FuturePointSet implements PathwayObject{
	private EndPoint start, end;
	private LinkedList<Point> points;
	private boolean isReversed = false;
	
	public FuturePointSet(EndPoint start, EndPoint end) {
		this.start = start;
		this.end = end;
	}

	// This should ONLY ever happen by player after loading is done
	@Override
	public void addTo(LinkedList<Point> receiver) {
		for (Point p : points) {
			receiver.add(p);
		}
	}
	
	public void convert() {
		if (points == null) {
			points = new LinkedList<Point>();
			Iterator<PathwayObject> iter = isReversed ? start.getUnconvertedPathToDestination(end).descendingIterator()
						: start.getUnconvertedPathToDestination(end).iterator();
			while(iter.hasNext()) {
				PathwayObject pwo = iter.next();
				if (pwo instanceof Point) {
					points.add((Point) pwo);
				}
				// If the path contains an unconverted future point set, convert that first
				else {
					FuturePointSet set = (FuturePointSet) pwo;
					if (!set.isConverted()) {
						set.convert();
					}
					set.addTo(points);
				}
			}
		}
	}
	
	@Override
	public String serializePath() {
		return start.getKey() + "->" + end.getKey();
	}
	
	public void toggleReversed() {
		isReversed = !isReversed;
	}
	
	public int size() {
		if (points == null) {
			points = new LinkedList<Point>();
			Iterator<Point> iter = isReversed ? start.getPathToDestination(end).descendingIterator() : start.getPathToDestination(end).iterator();
			while(iter.hasNext()) {
				points.add(iter.next());
			}
		}
		return points.size();
	}
	
	public boolean isConverted() {
		return points != null;
	}
}
