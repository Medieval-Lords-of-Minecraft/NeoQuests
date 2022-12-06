package me.neoblade298.neoquests;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtils {
	private static final int BLOCKS_PER_PARTICLE = 2;
	private static final double[] arrowRotations = new double[] {0.05, -0.1};

	public static void drawLine(Player p, Location l1, Location l2, int perPoint, double offset, double speed, DustOptions options, double blocksPerParticle) { 
		Location start = l1.clone();
		Location end = l2.clone();
	    
		Vector v = end.subtract(start).toVector();
		int iterations = (int) (v.length() / blocksPerParticle);
		for (int i = 1; i < iterations; i++) {
		    v.normalize();
		    v.multiply(i * blocksPerParticle);
		    start.add(v);
		    p.spawnParticle(Particle.REDSTONE, start, perPoint, offset, offset, offset, speed, options);
			start.subtract(v);
		}
	}
	
	public static void drawLine(Player p, Location l1, Location l2, int perPoint, double offset, double speed, DustOptions options) {
		drawLine(p, l1, l2, perPoint, offset, speed, options, BLOCKS_PER_PARTICLE);
	}
	
	public static void drawArrow(Player p, Location l1, Location l2, int perPoint, double offset, double speed, DustOptions options) {
		drawLine(p, l1, l2, perPoint, offset, speed, options);
		Location start = l1.clone();
		Location end = l2.clone();
		Vector tip = end.subtract(start).toVector();

		tip.multiply(0.9);
		for (double i : arrowRotations) {
			tip.rotateAroundY(i);
			start.add(tip);
			drawLine(p, start, l2, perPoint, offset, speed, options, 0.5);
			start.subtract(tip);
		}
	}
}
