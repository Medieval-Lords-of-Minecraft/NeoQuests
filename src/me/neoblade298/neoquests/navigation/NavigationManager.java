package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;

public class NavigationManager implements Manager {
	private static HashMap<Player, PathwayInstance> activePathways = new HashMap<Player, PathwayInstance>();
	private static HashMap<Chunk, ArrayList<Point>> pointMap = new HashMap<Chunk, ArrayList<Point>>();
	private static TreeSet<Point> points = new TreeSet<Point>();
	private static ArrayList<FuturePointSet> toConvert = new ArrayList<FuturePointSet>();
	private static HashMap<String, EndPoint> endpoints = new HashMap<String, EndPoint>();
	private static HashMap<Player, PathwayEditor> pathwayEditors = new HashMap<Player, PathwayEditor>();
	private static FileLoader pointLoader, endpointsLoader;
	private static File data = new File(NeoQuests.inst().getDataFolder(), "navigation");
	
	private static LineConfigManager<Point> mngr = new LineConfigManager<Point>(NeoQuests.inst(), "points");
	
	static {
		mngr.register(new Point());
		
		pointLoader = (cfg, file) -> {
			for (String line : cfg.getStringList("points")) {
				try {
					addPoint(mngr.get(new LineConfig(line)));
				} catch (NeoIOException e) {
					NeoQuests.showWarning("Failed to load nav point " + line, e);
				}
			}
		};
		
		endpointsLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				try {
					if (endpoints.containsKey(key.toUpperCase())) {
						NeoQuests.showWarning("Duplicate endpoint " + key + " in file " + file.getPath() + ", " +
								"the loaded pathway with this key is in " + endpoints.get(key.toUpperCase()).getFile().getPath());
						continue;
					}
					ConfigurationSection sec = cfg.getConfigurationSection(key);
					World w = Bukkit.getWorld(sec.getString("world", "Argyll"));
					String[] args = sec.getString("location").split(" ");
					double x = Double.parseDouble(args[0]);
					double y = Double.parseDouble(args[1]);
					double z = Double.parseDouble(args[2]);
					Point point = getPoint(new Location(w, x, y, z));
					if (point == null) {
						NeoQuests.showWarning("Failed to load endpoint " + key + ", point is null");
						continue;
					}
					EndPoint ep = new EndPoint(file, sec);
					addEndpoint(ep, point, false);
				}
				catch (NeoIOException e) {
					NeoQuests.showWarning("Failed to load endpoints", e);
				}
			}
		};
	}

	public NavigationManager() throws NeoIOException {
		new BukkitRunnable() {
			public void run() {
				for (Player p : pathwayEditors.keySet()) {
					pathwayEditors.get(p).show();
				}
			}
		}.runTaskTimer(NeoQuests.inst(), 0L, 30L);
		reload();
	}
	
	@Override
	public void reload() {
		try {
			points.clear();
			endpoints.clear();
			pointMap.clear();
			
			for (PathwayInstance pi : activePathways.values()) {
				pi.cancel("navigation reloaded.", true);
			}
			activePathways.clear();
			
			// Load in files
			NeoCore.loadFiles(new File(data, "points.yml"), pointLoader);
			NeoCore.loadFiles(new File(data, "endpoints"), endpointsLoader);
			
			// Loads in pathways, but does not load future point sets
			for (EndPoint ep : endpoints.values()) {
				try {
					ep.loadPathways();
				} catch (NeoIOException e) {
					NeoQuests.showWarning("Failed to load endpoint pathway for " + ep.getKey(), e);
				}
			}
			
			// Load in future point sets
			for (FuturePointSet set : toConvert) {
				set.convert();
			}
			toConvert.clear();
			
			// Finish loading pathways by adding the future point sets in
			for (EndPoint ep : endpoints.values()) {
				ep.addFuturePathways();
			}
		}
		catch (Exception e) {
			NeoQuests.showWarning("Failed to reload NavigationManager", e);
		}
	}
	
	@Override
	public String getKey() {
		return "NavigationManager";
	}
	
	public static boolean startNavigation(Player p, String start, String end) {
		if (!endpoints.containsKey(start.toUpperCase())) {
			Bukkit.getLogger().warning("[NeoQuests] Could not start pathway from " + start + " for player " + p.getName() + ", start point doesn't exist");
			Util.msg(p, "&cThat start point doesn't exist!");
			return false;
		}
		if (!endpoints.containsKey(end.toUpperCase())) {
			Bukkit.getLogger().warning("[NeoQuests] Could not start pathway to " + end + " for player " + p.getName() + ", end point doesn't exist");
			Util.msg(p, "&cThat end point doesn't exist!");
			return false;
		}
		EndPoint startPoint = endpoints.get(start.toUpperCase());
		EndPoint endPoint = endpoints.get(end.toUpperCase());
		if (!endPoint.getStartPoints().containsKey(startPoint)) {
			Bukkit.getLogger().warning("[NeoQuests] Could not start pathway from " + start + " to " + end + " for player " + p.getName() +
					", " + start + " doesn't connect to " + end + "!");
			Util.msg(p, "&cThat end point doesn't connect to that start point!");
			return false;
		}

		if (activePathways.containsKey(p)) {
			activePathways.get(p).cancel("started a new pathway.", false);
		}
		Util.msg(p, "&7Started navigation from " + startPoint.getDisplay() + " &7to &6" + endPoint.getDisplay());
		PathwayInstance pi = new PathwayInstance(p, startPoint, endPoint);
		activePathways.put(p, pi);
		return true;
	}
	
	public static void stopNavigation(Player p) {
		if (activePathways.containsKey(p)) {
			activePathways.get(p).cancel("cancelled by player", false);
			return;
		}
		Util.msg(p, "&cYou're not currently navigating anywhere!");
	}
	
	public static void startPathwayEditor(Player p) throws NeoIOException {
		if (pathwayEditors.containsKey(p)) {
			exitPathwayEditor(p);
			return;
		}

		Util.msg(p, "Successfully started pathway editor");
		Util.msg(p, "&oMake sure you're holding a stick!");
		Util.msg(p, "&cLeft Click&7: Place Point, &cShift-Left Click&7: Delete Point");
		Util.msg(p, "&cRight Click&7: Select/Connect Points, &cShift-Right Click&7: Undo Last Connection");
		Util.msg(p, "&cThrow Stick&7: Create endpoint");
		pathwayEditors.put(p, new PathwayEditor(p));
	}
	
	public static void addExistingPathway(Player p, String startStr, String endStr) {
		if (!pathwayEditors.containsKey(p)) {
			Util.msg(p, "§cYou aren't in the pathway editor!");
			return;
		}
		EndPoint start = getEndpoint(startStr);
		EndPoint end = getEndpoint(endStr);
		if (start == null) {
			Util.msg(p, "§cThe endpoint " + startStr + " doesn't exist!");
			return;
		}
		if (end == null) {
			Util.msg(p, "§cThe endpoint " + endStr + " doesn't exist!");
			return;
		}
		Util.msg(p, "&7Successfully added existing pathway!");
		pathwayEditors.get(p).addExistingPathway(p, start, end);
	}
	
	public static void exitPathwayEditor(Player p) {
		if (pathwayEditors.remove(p) != null) {
			Util.msg(p, "Successfully exited pathway editor");
		}
		else {
			Util.msg(p, "&cYou aren't in a pathway editor!");
		}
	}
	
	public static void savePoints() throws IOException {
		LinkedList<String> serialized = new LinkedList<String>();
		for (Point point : points) {
			serialized.add(point.serializeAsPoint());
		}
		YamlConfiguration cfg = new YamlConfiguration();
		cfg.set("points", serialized);
		cfg.save(new File(data, "points.yml"));
	}
	
	public static void showNearbyPoints(Player p) {
		for (Chunk c : Chunk.getSurroundingChunks(p.getLocation())) {
			if (pointMap.containsKey(c)) {
				for (Point point : pointMap.get(c)) {
					point.spawnParticle(p, false, false);
				}
			}
		}
	}
	
	public static PathwayEditor getEditor(Player p) {
		return pathwayEditors.get(p);
	}
	
	public static Point getOrCreatePoint(Location loc) {
		Point point = getPoint(loc);
		if (point == null) {
			point = new Point(loc, PointType.POINT);
			addPoint(point);
			return null;
		}
		else {
			return point;
		}
	}
	
	public static Point getPoint(Location loc) {
		if (Chunk.containsKey(loc)) {
			Chunk c = Chunk.getChunk(loc);
			if (pointMap.containsKey(c)) {
				for (Point point : pointMap.get(c)) {
					if (point.getLocation().equals(loc)) {
						return point;
					}
				}
			}
		}
		return null;
	}
	
	public static void addPoint(Point point) {
		Chunk c = Chunk.getOrCreateChunk(point.getLocation());
		ArrayList<Point> list = pointMap.getOrDefault(c, new ArrayList<Point>());
		list.add(point);
		point.setChunk(c);
		pointMap.put(c, list);
		points.add(point);
	}
	
	public static boolean deletePoint(Point toDelete) {
		Chunk c = toDelete.getChunk();
		Point found = null;
		for (Point point : pointMap.get(c)) {
			if (point.getLocation().equals(toDelete.getLocation())) {
				found = point;
				break;
			}
		}
		if (found != null) {
			if (found.isConnected()) {
				Bukkit.getLogger().warning("[NeoQuests] Failed to delete nav point, it's still connected");
				return false;
			}
			pointMap.get(c).remove(found);
			points.remove(toDelete);
			return true;
		}
		Bukkit.getLogger().warning("[NeoQuests] Failed to delete nav point, could not find point");
		return false;
	}
	
	public static EndPoint getEndpoint(String key) {
		return endpoints.get(key.toUpperCase());
	}
	
	public static File getDataFolder() {
		return data;
	}

	public static LinkedList<PathwayObject> createReversedPathwayObjects(LinkedList<PathwayObject> list) {
		LinkedList<PathwayObject> rev = new LinkedList<PathwayObject>();
		Iterator<PathwayObject> iter = list.descendingIterator();
		while (iter.hasNext()) {
			PathwayObject po = iter.next();
			if (po instanceof FuturePointSet) {
				((FuturePointSet) po).toggleReversed();
			}
			rev.add(iter.next());
		}
		return rev;
	}

	public static LinkedList<Point> createReversedPoints(LinkedList<Point> list) {
		LinkedList<Point> rev = new LinkedList<Point>();
		Iterator<Point> iter = list.descendingIterator();
		while (iter.hasNext()) {
			rev.add(iter.next());
		}
		return rev;
	}
	
	public static void removeEndpoint(String key) {
		EndPoint ep = endpoints.get(key.toUpperCase());
		ep.getPoint().setEndpoint(null);
		try {
			unsaveEndpoint(ep);
		} catch (IOException e) {
			NeoQuests.showWarning("Failed to remove endpoint " + key, e);
			e.printStackTrace();
		}
	}
	
	public static void addEndpoint(EndPoint ep, Point point, boolean save) {
		endpoints.put(ep.getKey().toUpperCase(), ep);
		point.setEndpoint(ep);
		ep.setPoint(point);
		if (save) saveEndpoint(ep);
	}
	
	public static void saveEndpoint(EndPoint ep) {
		try {
			savePoints();
			
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(PathwayEditor.endpointFile);
			Point point = ep.getPoint();
	
			ep.setFile(PathwayEditor.endpointFile);
			ConfigurationSection sec = cfg.createSection(ep.getKey());
			sec.set("display", ep.getDisplay());
			sec.set("location", point.serializeLocation());
			sec.set("world", point.getLocation().getWorld().getName());
			cfg.save(PathwayEditor.endpointFile);
		} catch (IOException e) {
			e.printStackTrace();
			NeoQuests.showWarning("Failed to save endpoint " + ep.getKey(), e);
		}
	}
	
	public static void unsaveEndpoint(EndPoint ep) throws IOException {
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(ep.getFile());
		cfg.set(ep.getKey(), null);
		cfg.save(ep.getFile());
	}
	
	public static void removeActivePathway(Player p) {
		activePathways.remove(p);
	}
	
	public static void clearUnusedPoints(CommandSender s) {
		ArrayList<Point> toRemove = new ArrayList<Point>();
		for (Point point : points) {
			if (!point.isConnected()) {
				toRemove.add(point);
			}
		}
		for (Point point : toRemove) {
			deletePoint(point);
			Util.msg(s, "&7Deleted point at " + point);
		}
	}
	
	@Override
	public void cleanup() {	}
	
	public static void convert() {
		
	}
	
	public static void addFuturePointSet(FuturePointSet set) {
		toConvert.add(set);
	}
}
