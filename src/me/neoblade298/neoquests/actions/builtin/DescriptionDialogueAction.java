package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;

public class DescriptionDialogueAction implements Action, DialogueAction {
	private static final String key = "desc";
	private String dialogue;
	
	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new DescriptionDialogueAction());
		dialogueActions.put(key, new DescriptionDialogueAction());
	}
	
	public DescriptionDialogueAction() {}
	
	public DescriptionDialogueAction(LineConfig cfg) {
		this.dialogue = "§7§o" + cfg.getLine();
	}

	@Override
	public void run(Player p) {
		p.sendMessage(this.dialogue);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new DescriptionDialogueAction(cfg);
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
