package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.quests.QuestsManager;

public class StartQuestAction implements Action {
	private static String key = "start-quest";
	private String quest;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new StartQuestAction());
	}
	
	public StartQuestAction() {}
	
	public StartQuestAction(LineConfig cfg) {
		this.quest = cfg.getString("key", "N/A");
	}

	@Override
	public Action create(LineConfig cfg) {
		return new StartQuestAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		QuestsManager.startQuest(p, quest);
	}
	
	public String getQuest() {
		return quest;
	}

}
