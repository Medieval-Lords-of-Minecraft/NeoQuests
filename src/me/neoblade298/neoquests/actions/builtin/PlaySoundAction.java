package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;

public class PlaySoundAction implements Action {
	private static String key = "play-sound";
	private Sound sound;
	private float pitch;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new PlaySoundAction());
	}
	
	public PlaySoundAction() {}
	
	public PlaySoundAction(LineConfig cfg) {
		this.pitch = (float) cfg.getDouble("pitch", 1.0);
		this.sound = Sound.valueOf(cfg.getString("sound", "entity.generic.explode").toUpperCase());
	}

	@Override
	public Action create(LineConfig cfg) {
		return new PlaySoundAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(Player p) {
		p.playSound(p.getLocation(), sound, 1.0F, pitch);
	}

}
