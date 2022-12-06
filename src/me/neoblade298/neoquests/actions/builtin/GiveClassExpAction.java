package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class GiveClassExpAction extends RewardAction {
	private static String key = "give-class-exp";
	private int amount;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveClassExpAction());
	}
	
	public GiveClassExpAction() {}
	
	public GiveClassExpAction(LineConfig cfg) {
		super(cfg);
		this.amount = cfg.getInt("amount", 1);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveClassExpAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		return "§f" + amount + " §6Class Exp";
	}

	@Override
	public void run(Player p) {
		SkillAPI.getPlayerData(p).giveExp(amount, ExpSource.QUEST);
	}

}
