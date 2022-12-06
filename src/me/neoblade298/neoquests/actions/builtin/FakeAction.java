package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class FakeAction extends RewardAction {
	private static String key = "fake";
	private String display;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new FakeAction());
	}
	
	public FakeAction() {}
	
	public FakeAction(LineConfig cfg) {
		super(cfg);
		this.display = cfg.getLine();
	}

	@Override
	public Action create(LineConfig cfg) {
		return new FakeAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		return display;
	}

	@Override
	public void run(Player p) { }

}
