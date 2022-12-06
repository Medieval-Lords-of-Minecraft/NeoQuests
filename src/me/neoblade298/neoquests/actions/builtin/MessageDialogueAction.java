package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;

public class MessageDialogueAction implements Action, DialogueAction {
	private static final String key = "msg";
	private String dialogue;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new MessageDialogueAction());
		dialogueActions.put(key, new MessageDialogueAction());
	}
	
	public MessageDialogueAction() {}
	
	public MessageDialogueAction(LineConfig cfg) {
		this.dialogue = cfg.getLine();
	}

	@Override
	public void run(Player p) {
		p.sendMessage(this.dialogue);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new MessageDialogueAction(cfg);
	}
	
	@Override
	public int getDelay() {
		return DialogueAction.getDelay(this.dialogue);
	}
	
	@Override
	public String getKey() {
		return key;
	}
}
