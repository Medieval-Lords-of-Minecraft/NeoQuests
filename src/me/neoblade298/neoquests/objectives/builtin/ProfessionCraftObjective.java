package me.neoblade298.neoquests.objectives.builtin;

import me.Neoblade298.NeoProfessions.Events.ProfessionCraftSuccessEvent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class ProfessionCraftObjective extends Objective {
	private String recipe;
	
	public ProfessionCraftObjective() {
		super();
	}

	public ProfessionCraftObjective(LineConfig cfg) {
		super(ObjectiveEvent.PROFESSION_CRAFT, cfg, true);
		recipe = cfg.getString("recipe", null);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new ProfessionCraftObjective(cfg);
	}

	@Override
	public String getKey() {
		return "profession-craft";
	}

	public boolean checkEvent(ProfessionCraftSuccessEvent e, ObjectiveInstance o) {
		if (recipe == null || recipe.equalsIgnoreCase(e.getRecipe().getKey())) {
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
