package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class GiveTagAction extends RewardAction {
	private static String key = "give-tag";
	private String tag;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveTagAction());
	}
	
	public GiveTagAction() {}
	
	public GiveTagAction(LineConfig cfg) {
		tag = cfg.getString("tag", null);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveTagAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		NeoQuests.getPlayerTags(p).set(tag, p.getUniqueId());
	}

	@Override
	public String getDisplay() {
		return null;
	}

}
