package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.quests.QuestsManager;

public class LeaveQuestlineAction implements Action {
	private static String key = "leave-questline";
	private String ql;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new LeaveQuestlineAction());
	}
	
	public LeaveQuestlineAction() {}
	
	public LeaveQuestlineAction(LineConfig cfg) {
		this.ql = cfg.getString("ql", "N/A");
	}

	@Override
	public Action create(LineConfig cfg) {
		return new LeaveQuestlineAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		QuestsManager.getQuester(p).removeQuestline(ql);
	}

}
