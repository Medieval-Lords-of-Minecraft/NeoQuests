package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import net.citizensnpcs.api.CitizensAPI;

public class NPCDialogueAction implements Action, DialogueAction {
	private static final String key = "npc";
	private String dialogue, npcname;
	private int npcid;
	
	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new NPCDialogueAction());
		dialogueActions.put(key, new NPCDialogueAction());
	}
	
	public NPCDialogueAction() {}
	
	public NPCDialogueAction(LineConfig cfg) {
		this.npcid = cfg.getInt("id", 0);
		this.dialogue = "§7: " + cfg.getLine();
	}

	@Override
	public void run(Player p) {
		if (npcname == null) {
			npcname = CitizensAPI.getNPCRegistry().getById(npcid).getFullName();
		}
		p.sendMessage(npcname + this.dialogue);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new NPCDialogueAction(cfg);
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
