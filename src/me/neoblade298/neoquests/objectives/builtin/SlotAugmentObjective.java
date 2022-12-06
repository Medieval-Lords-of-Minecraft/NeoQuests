package me.neoblade298.neoquests.objectives.builtin;

import me.Neoblade298.NeoProfessions.Events.ProfessionSlotSuccessEvent;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class SlotAugmentObjective extends Objective {
	private String augment;
	
	public SlotAugmentObjective() {
		super();
	}

	public SlotAugmentObjective(LineConfig cfg) {
		super(ObjectiveEvent.SLOT_AUGMENT, cfg, true);
		augment = cfg.getString("augment", null);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new SlotAugmentObjective(cfg);
	}

	@Override
	public String getKey() {
		return "slot-augment";
	}

	public boolean checkEvent(ProfessionSlotSuccessEvent e, ObjectiveInstance o) {
		if (augment == null || augment.equalsIgnoreCase(e.getAugment().getName())) {
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
