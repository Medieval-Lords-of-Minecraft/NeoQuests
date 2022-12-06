package me.neoblade298.neoquests.navigation;

import java.util.LinkedList;
import java.util.ListIterator;

import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.ParticleUtils;


public class PathwayInstance {
	private Player p;
	private BukkitTask task;
	private LinkedList<Point> points = new LinkedList<Point>();
	private World w;
	private EndPoint start, end;
	
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	private static final int END_RADIUS_SQ = 100;
	
	private static final double DISTANCE_SHOWABLE = 5000;
	private static final DustOptions PARTICLE_DATA = new DustOptions(Color.RED, 1.0F);
	
	public PathwayInstance(Player p, EndPoint start, EndPoint end) {
		this.p = p;
		this.w = start.getWorld();
		this.start = start;
		this.end = end;
		
		this.points = start.getPathToDestination(end);

		PathwayInstance pwi = this;
		task = new BukkitRunnable() {
			public void run() {
				if (p == null) {
					this.cancel();
					return;
				}

				// Check if in different world
				if (!p.getWorld().equals(w)) {
					pwi.cancel("no longer in same world.", false);
					return;
				}
				
				// Check if location reached
				if (p.getLocation().distanceSquared(points.peekLast().getLocation()) <= END_RADIUS_SQ) {
					pwi.stop();
					return;
				}
				
				show(p);
			}
		}.runTaskTimer(NeoQuests.inst(), 0L, 20L);
	}
	public void setTask(BukkitTask task) {
		this.task = task;
	}
	public BukkitTask getTask() {
		return task;
	}
	
	public void stop() {
		task.cancel();
		Util.msg(p, "Navigation from &6" + start.getDisplay() + " &7to &6" + end.getDisplay() + " &7was successful!");
		NavigationManager.removeActivePathway(p);
	}
	
	public void cancel(String reason, boolean isIterating) {
		task.cancel();
		Util.msg(p, "Navigation from &6" + start.getDisplay() + "&7 to &6" + end.getDisplay() + "&7 was cancelled: " + reason);
		if (!isIterating) NavigationManager.removeActivePathway(p);
	}
	
	public void show(Player p) {
		showLines(p, this.points, true);
	}
	
	public static void showLines(Player p, LinkedList<Point> points, boolean useDisplayLocation) {
		if (points.size() < 2) {
			return;
		}
		ListIterator<Point> iter = points.listIterator();
		Point l1 = null;
		Point l2 = iter.next();
		if (l2.getLocation().distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE) {
			l2.spawnParticle(p, useDisplayLocation, false);
		}
		while (iter.hasNext()) {
			l1 = l2;
			l2 = iter.next();
			boolean dist1 = l1.getLocation().distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE;
			boolean dist2 = l2.getLocation().distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE;
			if (dist2) {
				l2.spawnParticle(p, useDisplayLocation, !iter.hasNext()); // Spawn end particle at last point only
			}
			
			if (dist1 || dist2) {
				if (l1.getType() != PointType.PORTAL || l2.getType() != PointType.PORTAL) {
					ParticleUtils.drawLine(p, useDisplayLocation ? l1.getDisplayLocation() : l1.getGroundLocation(),
							useDisplayLocation ? l2.getDisplayLocation() : l2.getGroundLocation(), PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_SPEED, PARTICLE_DATA);
				}
			}
		}
	}
	
	public World getWorld() {
		return w;
	}
}

