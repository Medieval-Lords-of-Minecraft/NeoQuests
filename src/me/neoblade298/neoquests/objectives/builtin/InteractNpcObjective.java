package me.neoblade298.neoquests.objectives.builtin;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class InteractNpcObjective extends Objective {
	private int npcid;
	private String npcname;
	
	public InteractNpcObjective() {
		super();
	}

	public InteractNpcObjective(LineConfig cfg) {
		super(ObjectiveEvent.INTERACT_NPC, cfg);
		
		npcid = cfg.getInt("id", -1);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new InteractNpcObjective(cfg);
	}

	@Override
	public String getKey() {
		return "interact-npc";
	}

	public boolean checkEvent(NPCRightClickEvent e, ObjectiveInstance o) {
		int id = e.getNPC().getId();
		if (id == npcid && o.getCount() == 0) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		if (npcname == null) {
			npcname = CitizensAPI.getNPCRegistry().getById(npcid).getFullName();
		}
		return "Talk to NPC " + npcname;
	}

}
