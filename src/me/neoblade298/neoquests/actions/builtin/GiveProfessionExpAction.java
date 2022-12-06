package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class GiveProfessionExpAction extends RewardAction {
	private static String key = "give-profession-exp";
	private ProfessionType type;
	private int amount;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveProfessionExpAction());
	}
	
	public GiveProfessionExpAction() {}
	
	public GiveProfessionExpAction(LineConfig cfg) {
		super(cfg);
		this.amount = cfg.getInt("amount", 1);
		this.type = ProfessionType.valueOf(cfg.getString("type", "crafter").toUpperCase());
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveProfessionExpAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		return "§f" + amount + " §6" + type.getDisplay() + " Exp";
	}

	@Override
	public void run(Player p) {
		ProfessionManager.getAccount(p.getUniqueId()).get(type).addExp(p, amount);
	}

}
