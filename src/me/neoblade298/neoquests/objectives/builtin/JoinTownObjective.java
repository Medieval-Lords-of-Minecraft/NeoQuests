package me.neoblade298.neoquests.objectives.builtin;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class JoinTownObjective extends Objective {
	
	public JoinTownObjective() {
		super();
	}

	public JoinTownObjective(LineConfig cfg) {
		super(ObjectiveEvent.JOIN_TOWN, cfg, true);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new JoinTownObjective(cfg);
	}

	@Override
	public String getKey() {
		return "join-town";
	}

	public boolean checkEvent(TownAddResidentEvent e, ObjectiveInstance o) {
		o.incrementCount();
		return true;
	}

	@Override
	public String getDisplay() {
		return "Hide this objective!";
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}
}
