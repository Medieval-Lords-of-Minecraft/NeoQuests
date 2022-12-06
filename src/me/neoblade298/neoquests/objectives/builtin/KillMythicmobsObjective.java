package me.neoblade298.neoquests.objectives.builtin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class KillMythicmobsObjective extends Objective {
	private List<String> types;
	private String display;
	
	public KillMythicmobsObjective() {
		super();
	}

	public KillMythicmobsObjective(LineConfig cfg) {
		super(ObjectiveEvent.KILL_MYTHICMOB, cfg);

		types = Arrays.asList(cfg.getString("type", null).split(","));
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new KillMythicmobsObjective(cfg);
	}

	@Override
	public String getKey() {
		return "kill-mythicmobs";
	}

	public boolean checkEvent(MythicMobDeathEvent e, ObjectiveInstance o) {
		String name = e.getMobType().getInternalName();
		for (String type : types) {
			if (name.equals(type)) {
				o.incrementCount();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDisplay() {
		if (display == null) {
			try {
				MobManager mm = MythicBukkit.inst().getMobManager();
				display = mm.getMythicMob(types.get(0)).get().getDisplayName().get();
				if (types.size() > 1) {
					for (int i = 1; i < types.size(); i++) {
						display += "§7, " + mm.getMythicMob(types.get(i)).get().getDisplayName().get();
					}
				}
			}
			catch (Exception e) {
				Bukkit.getLogger().warning("[NeoQuests] Failed to retrieve mob display for Mythic Mob " + type + ".");
				e.printStackTrace();
				display = "Error";
			}
		}
		return "Kill " + display;
	}

}
