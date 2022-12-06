package me.neoblade298.neoquests.objectives.builtin;

import me.Neoblade298.NeoProfessions.Events.OpenProfessionInvEvent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class OpenProfessionInventoryObjective extends Objective {
	private String inv;
	
	public OpenProfessionInventoryObjective() {
		super();
	}

	public OpenProfessionInventoryObjective(LineConfig cfg) {
		super(ObjectiveEvent.OPEN_PROFESSION_INV, cfg, true);
		inv = cfg.getString("inv", null);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new OpenProfessionInventoryObjective(cfg);
	}

	@Override
	public String getKey() {
		return "open-prof-inv";
	}

	public boolean checkEvent(OpenProfessionInvEvent e, ObjectiveInstance o) {
		if (e.getInventory().getClass().getSimpleName().equalsIgnoreCase(inv)) {
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
