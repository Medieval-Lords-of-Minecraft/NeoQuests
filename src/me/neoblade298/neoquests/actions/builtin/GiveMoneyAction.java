package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class GiveMoneyAction extends RewardAction {
	private static String key = "give-money";
	private int amount;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveMoneyAction());
	}
	
	public GiveMoneyAction() {}
	
	public GiveMoneyAction(LineConfig cfg) {
		super(cfg);
		this.amount = cfg.getInt("amount", 1);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveMoneyAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		return "§f" + amount + " §6Gold";
	}

	@Override
	public void run(Player p) {
		NeoCore.getEconomy().depositPlayer(p, amount);
	}

}
