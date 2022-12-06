package me.neoblade298.neoquests.objectives.builtin;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.events.ConversationEvent;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class RespondConversationObjective extends Objective {
	private String conv;
	private int stage, response;
	
	public RespondConversationObjective() {
		super();
	}

	public RespondConversationObjective(LineConfig cfg) {
		super(ObjectiveEvent.RESPOND_CONVERSATION, cfg);
		
		conv = cfg.getString("conv", null);
		stage = cfg.getInt("stage", -1);
		response = cfg.getInt("response", -1);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new RespondConversationObjective(cfg);
	}

	@Override
	public String getKey() {
		return "respond-conversation";
	}

	public boolean checkEvent(ConversationEvent e, ObjectiveInstance o) {
		if (e.getConv().getKey().equalsIgnoreCase(this.conv)) {
			if (stage != -1 && e.getStage().getNumber() != stage) return false;
			if (response != -1 && e.getResp().getResponseNum() != response) return false;
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return "";
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
