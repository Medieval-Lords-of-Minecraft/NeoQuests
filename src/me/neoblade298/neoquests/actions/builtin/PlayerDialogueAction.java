package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;

public class PlayerDialogueAction implements Action, DialogueAction {
	private static final String key = "player";
	private String dialogue;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new PlayerDialogueAction());
		dialogueActions.put(key, new NPCDialogueAction());
	}
	
	public PlayerDialogueAction() {}
	
	public PlayerDialogueAction(LineConfig cfg) {
		this.dialogue = "§7: " + cfg.getLine();
	}

	@Override
	public void run(Player p) {
		p.sendMessage("§e" + p.getName() + this.dialogue);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new PlayerDialogueAction(cfg);
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
