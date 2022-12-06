package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;

public class GiveEffectAction implements Action {
	private static String key = "give-effect";
	private PotionEffect pe;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new GiveEffectAction());
	}
	
	public GiveEffectAction() {}
	
	public GiveEffectAction(LineConfig cfg) {
		int level = cfg.getInt("level", 1) - 1;
		int duration = cfg.getInt("duration", 100);
		String eff = cfg.getString("potion", "SLOWNESS").toUpperCase();
		pe = new PotionEffect(PotionEffectType.getByName(eff), duration, level);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new GiveEffectAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		p.addPotionEffect(pe);
	}

}
