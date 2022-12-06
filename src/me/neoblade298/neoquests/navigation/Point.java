package me.neoblade298.neoquests.navigation;

import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public class Point implements LineConfigParser<Point>, Comparable<Point>, PathwayObject {
	private LineConfig cfg;
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	private static final int PARTICLES_PER_ENDPOINT = 20;
	private static final double ENDPOINT_SPEED = 0.05;
	private EndPoint endpoint;
	
	private Chunk chunk;
	private Location loc, groundLoc, displayLoc;
	private PointType type;
	private HashSet<String> connections = new HashSet<String>();
	private World w;
	
	public Point() {}
	
	public Point(Location loc, PointType type, LineConfig cfg) {
		this(loc, type);
		this.cfg = cfg;
	}
	
	public Point(Location loc, PointType type) {
		this.loc = loc;
		this.groundLoc = loc.clone().add(0, 1, 0);
		this.displayLoc = loc.clone().add(0, 1.5, 0);
		this.type = type;
		this.w = loc.getWorld();
	}

	public Location getLocation() {
		return loc;
	}
	
	public World getWorld() {
		return w;
	}

	public Location getDisplayLocation() {
		return displayLoc;
	}

	public Location getGroundLocation() {
		return displayLoc;
	}

	public String serializeAsPoint() {
		if (cfg != null) {
			return cfg.getFullLine();
		}
		else {
			String serialized = "type:" + type;
			serialized += " x:" + loc.getX();
			serialized += " y:" + loc.getY();
			serialized += " z:" + loc.getZ();
			serialized += " world:" + loc.getWorld().getName();
			return serialized;
		}
	}
	
	@Override
	public String serializePath() {
		return loc.getX() + " " + loc.getY() + " " + loc.getZ();
	}
	
	public PointType toggleType() {
		if (type == PointType.POINT) {
			type = PointType.PORTAL;
		}
		else {
			type = PointType.POINT;
		}
		return type;
	}
	
	public PointType getType() {
		return type;
	}
	
	public void addConnection(String key) {
		connections.add(key);
	}
	
	public void removeConnection(String key) {
		connections.remove(key);
	}
	
	public boolean isConnected() {
		return connections.size() > 0;
	}

	public HashSet<String> getPathwaysUsing() {
		return connections;
	}
	
	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public String serializeLocation() {
		return loc.getX() + " " + loc.getY() + " "+ loc.getZ();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point)) return false;
		
		Point p = (Point) o;
		
		return this.getLocation().equals(p.getLocation()) && this.type == p.getType();
	}

	@Override
	public Point create(LineConfig cfg) {
		double x = cfg.getDouble("x", 0.0);
		double y = cfg.getDouble("y", 0.0);
		double z = cfg.getDouble("z", 0.0);
		World w = Bukkit.getWorld(cfg.getString("world", "Argyll"));
		PointType type = PointType.valueOf(cfg.getString("type", "point").toUpperCase());
		return new Point(new Location(w, x, y, z), type, cfg);
	}

	@Override
	public String getKey() {
		return ""; // Keyless system
	}

	@Override
	public int compareTo(Point o) {
		Location l1 = this.getLocation();
		Location l2 = o.getLocation();
		int compare = (int) (l1.getX() - l2.getX());
		if (compare != 0) return compare;
		
		compare = (int) (l1.getZ() - l2.getZ());
		if (compare != 0) return compare;

		compare = (int) (l1.getY() - l2.getY());
		if (compare != 0) return compare;

		compare = l1.getWorld().getName().compareTo(l2.getWorld().getName());
		if (compare != 0) return compare;
		
		int t1 = this.getType() == PointType.POINT ? 1 : 0;
		int t2 = o.getType() == PointType.POINT ? 1 : 0;
		return t1 - t2;
	}
	
	// useDisplayLocation true for navigating player, false for in pathway creator
	// spawnEndParticle true only in navigation for the last point
	public void spawnParticle(Player p, boolean useDisplayLocation, boolean spawnEndParticle) {
		if (useDisplayLocation) {
		    p.spawnParticle(Particle.REDSTONE, displayLoc, PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_SPEED, type.getDustOptions());
			if (spawnEndParticle) {
			    p.spawnParticle(Particle.END_ROD, displayLoc, PARTICLES_PER_ENDPOINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, ENDPOINT_SPEED);
			}
		}
		else {
			if (isEndpoint()) {
			    p.spawnParticle(Particle.END_ROD, groundLoc, PARTICLES_PER_ENDPOINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, ENDPOINT_SPEED);
			}
		    p.spawnParticle(Particle.REDSTONE, groundLoc, PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_SPEED, type.getDustOptions());
		}
	}
	
	public LineConfig getLineConfig() {
		return cfg;
	}
	
	public boolean isEndpoint() {
		return endpoint != null;
	}
	
	public void setEndpoint(EndPoint ep) {
		endpoint = ep;
	}
	
	public EndPoint getEndpoint() {
		return endpoint;
	}
	
	@Override
	public String toString() {
		return loc.getWorld().getName() + ":" + loc.getX() + " " + loc.getY() + " " +loc.getZ();
	}

	@Override
	public void addTo(LinkedList<Point> points) {
		points.add(this);
	}
	
	@Override
	public int size() {
		return 1;
	}
}
