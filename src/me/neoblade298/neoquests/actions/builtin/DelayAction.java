package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DelayableAction;
import me.neoblade298.neoquests.actions.EmptyAction;

public class DelayAction implements DelayableAction, EmptyAction {
	private static final String key = "delay";
	private int delay = 0;
	
	public static void register(HashMap<String, Action> actions) {
		actions.put(key, new DelayAction());
	}
	
	public DelayAction() {}
	
	public DelayAction(LineConfig cfg) {
		delay = Integer.parseInt(cfg.getFullLine().substring("delay ".length()));
	}

	@Override
	public Action create(LineConfig cfg) {
		return new DelayAction(cfg);
	}

	@Override
	public int getDelay() {
		return delay;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {}
}
