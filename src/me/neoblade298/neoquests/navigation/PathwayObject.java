package me.neoblade298.neoquests.navigation;

import java.util.LinkedList;

public interface PathwayObject {
	public void addTo(LinkedList<Point> points);
	public String serializePath();
	public int size();
}
