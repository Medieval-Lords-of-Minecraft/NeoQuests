package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerMoveEvent;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class ReachLocationObjective extends Objective {
	private Location loc;
	private double radiusSq;
	private String display;
	
	public ReachLocationObjective() {
		super();
	}

	public ReachLocationObjective(LineConfig cfg) {
		super(ObjectiveEvent.MOVE, cfg);

		double x = cfg.getDouble("x", 0.0);
		double y = cfg.getDouble("y", 0.0);
		double z = cfg.getDouble("z", 0.0);
		World w = Bukkit.getWorld(cfg.getString("world", "Argyll"));
		loc = new Location(w, x, y, z);
		radiusSq = cfg.getDouble("radius", 5);
		radiusSq *= radiusSq;
		display = cfg.getLine();
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new ReachLocationObjective(cfg);
	}

	@Override
	public String getKey() {
		return "reach-location";
	}

	public boolean checkEvent(PlayerMoveEvent e, ObjectiveInstance o) {
		if (!e.getTo().getWorld().equals(loc.getWorld())) {
			return false;
		}
		if (e.getTo().distanceSquared(loc) < radiusSq) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return "Reach " + display;
	}

}
