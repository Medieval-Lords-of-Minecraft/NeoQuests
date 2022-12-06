package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import me.neoblade298.neocore.exceptions.NeoIOException;

public class EndPoint {
	private Point point;
	private String key, display;
	private ConfigurationSection cfg;
	private File file;
	private HashMap<EndPoint, LinkedList<PathwayObject>> startPointsUnconverted = new HashMap<EndPoint, LinkedList<PathwayObject>>();
	private HashMap<EndPoint, LinkedList<PathwayObject>> destinationsUnconverted = new HashMap<EndPoint, LinkedList<PathwayObject>>();
	private HashMap<EndPoint, LinkedList<Point>> startPoints = new HashMap<EndPoint, LinkedList<Point>>();
	private HashMap<EndPoint, LinkedList<Point>> destinations = new HashMap<EndPoint, LinkedList<Point>>();
	
	public EndPoint() {}
	
	public EndPoint(File file, ConfigurationSection cfg) throws NeoIOException {
		this.file = file;
		this.key = cfg.getName();
		this.display = cfg.getString("display");
		this.cfg = cfg;
	}
	
	public void loadPathways() throws NeoIOException {
		ConfigurationSection sec = cfg.getConfigurationSection("to");
		if (sec != null) {
			for (String key : sec.getKeys(false)) {
				ConfigurationSection path = sec.getConfigurationSection(key);
				EndPoint end = NavigationManager.getEndpoint(key);
				if (end == null) {
					throw new NeoIOException("Failed to load path from " + this.key + " to " + key + ", " + key + " doesn't exist or isn't an endpoint.");
				}
				LinkedList<PathwayObject> points = parsePoints(path.getStringList("points"), this.key + " -> " + end.getKey());
				destinationsUnconverted.put(end, points);
				end.addStartPointUnconverted(this, points);
				
				if (path.getBoolean("bidirectional", false)) {
					LinkedList<PathwayObject> reversed = NavigationManager.createReversedPathwayObjects(points);
					startPointsUnconverted.put(end, reversed);
					end.addDestinationUnconverted(this, reversed);
				}
			}
		}
		cfg = null; // Remove cfg from memory after it's used
	}
	
	// Notably, when an endpoint is first made it has no file variable (must be set and/or loaded via cfg)
	public EndPoint(String key, String display) {
		this.key = key;
		this.display = display;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getDisplay() {
		return this.display;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void addStartPointUnconverted(EndPoint point, LinkedList<PathwayObject> pw) {
		startPointsUnconverted.put(point, pw);
	}
	
	public void addDestinationUnconverted(EndPoint point, LinkedList<PathwayObject> pw) {
		destinationsUnconverted.put(point, pw);
	}
	
	public void addStartPoint(EndPoint point, LinkedList<Point> pw) {
		startPoints.put(point, pw);
	}
	
	public void addDestination(EndPoint point, LinkedList<Point> pw) {
		destinations.put(point, pw);
	}
	
	public HashMap<EndPoint, LinkedList<PathwayObject>> getStartPointsUnconverted() {
		return startPointsUnconverted;
	}
	
	public HashMap<EndPoint, LinkedList<PathwayObject>> getDestinationsUnconverted() {
		return destinationsUnconverted;
	}
	
	public HashMap<EndPoint, LinkedList<Point>> getStartPoints() {
		return startPoints;
	}
	
	public HashMap<EndPoint, LinkedList<Point>> getDestinations() {
		return destinations;
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof EndPoint;
	}
	
	private LinkedList<PathwayObject> parsePoints(List<String> list, String connection) throws NeoIOException {
		if (list.size() <= 1) {
			throw new NeoIOException("Pathway " + this.key + " has <= 1 points, invalid!");
		}
		
		LinkedList<PathwayObject> points = new LinkedList<PathwayObject>();
		for (String line : list) {
			if (line.contains("->")) {
				String[] args = line.split("->");
				FuturePointSet set = new FuturePointSet(NavigationManager.getEndpoint(args[0]), NavigationManager.getEndpoint(args[1]));
				points.add(set);
				NavigationManager.addFuturePointSet(set);
				continue;
			}
			String args[] = line.split(" ");
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			Point point = NavigationManager.getPoint(new Location(this.point.getWorld(), x, y, z));
			if (point == null) {
				throw new NeoIOException("Pathway " + this.key + " failed to load point " + line);
			}
			points.add(point);
		}
		
		// Passed validation
		setupPoints(points, connection);
		return points;
	}
	
	private void setupPoints(LinkedList<PathwayObject> points, String connection) {
		ListIterator<PathwayObject> iter = points.listIterator();
		PathwayObject p = null;
		while (iter.hasNext()) {
			p = iter.next();
			if (p instanceof FuturePointSet) continue;
			((Point) p).addConnection(connection);
		}
	}
	
	public World getWorld() {
		return point.getWorld();
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	@Override
	public String toString() {
		return this.key;
	}
	
	public Point getPoint() {
		return point;
	}
	
	// This should only ever happen by player, never by loading
	public LinkedList<Point> getPathToDestination(EndPoint dest) {
		return this.destinations.get(dest);
	}
	
	public LinkedList<PathwayObject> getUnconvertedPathToDestination(EndPoint dest) {
		return this.destinationsUnconverted.get(dest);
	}
	
	public void addFuturePathways() {
		for (Entry<EndPoint, LinkedList<PathwayObject>> e : startPointsUnconverted.entrySet()) {
			LinkedList<Point> points = new LinkedList<Point>();
			for (PathwayObject po : this.startPointsUnconverted.get(e.getKey())) {
				po.addTo(points);
			}
			this.startPoints.put(e.getKey(), points);
		}
		startPointsUnconverted = null;

		for (Entry<EndPoint, LinkedList<PathwayObject>> e : destinationsUnconverted.entrySet()) {
			LinkedList<Point> points = new LinkedList<Point>();
			for (PathwayObject po : this.destinationsUnconverted.get(e.getKey())) {
				po.addTo(points);
			}
			this.destinations.put(e.getKey(), points);
		}
		destinationsUnconverted = null;
	}
	
	public ConfigurationSection getConfig() {
		return cfg;
	}
}
