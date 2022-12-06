package me.neoblade298.neoquests.objectives.builtin;

import me.Neoblade298.NeoProfessions.Events.ProfessionPlantSeedEvent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class PlantSeedObjective extends Objective {
	private int seed;
	
	public PlantSeedObjective() {
		super();
	}

	public PlantSeedObjective(LineConfig cfg) {
		super(ObjectiveEvent.PLANT_SEED, cfg, true);
		seed = cfg.getInt("seed", -1);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new PlantSeedObjective(cfg);
	}

	@Override
	public String getKey() {
		return "plant-seed";
	}

	public boolean checkEvent(ProfessionPlantSeedEvent e, ObjectiveInstance o) {
		if (seed == -1 || e.getSeed().getId() == seed) {
			o.incrementCount();
			return true;
		}
		return false;
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
