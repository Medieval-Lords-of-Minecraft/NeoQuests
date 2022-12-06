package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class SayObjective extends Objective {
	private String chat;
	
	public SayObjective() {
		super();
	}

	public SayObjective(LineConfig cfg) {
		super(ObjectiveEvent.CHAT, cfg);
		
		chat = cfg.getLine().toUpperCase();
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new SayObjective(cfg);
	}

	@Override
	public String getKey() {
		return "say";
	}

	public boolean checkEvent(AsyncPlayerChatEvent e, ObjectiveInstance o) {
		if (e.getMessage().toUpperCase().contains(chat)) {
			o.incrementCount();
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
