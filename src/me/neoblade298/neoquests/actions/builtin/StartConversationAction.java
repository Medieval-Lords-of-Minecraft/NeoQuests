package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.conversations.ConversationManager;

public class StartConversationAction implements Action {
	private static String key = "start-conversation";
	private String conv;
	private boolean ignoreConditions;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new StartConversationAction());
	}
	
	public StartConversationAction() {}
	
	public StartConversationAction(LineConfig cfg) {
		this.conv = cfg.getString("conv", "N/A");
		this.ignoreConditions = cfg.getBool("ignoreConditions", false);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new StartConversationAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		ConversationManager.startConversation(p, conv, ignoreConditions);
	}

}
