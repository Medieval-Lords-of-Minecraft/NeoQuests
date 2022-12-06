package me.neoblade298.neoquests.conditions.builtin;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.conditions.Condition;

public class ClassLevelCondition extends Condition {
	private static final String key;
	private int min, max;
	private boolean negate;
	
	static {
		key = "class-level";
	}
	
	public ClassLevelCondition() {}
	
	public ClassLevelCondition(LineConfig cfg) {
		super(cfg);
		negate = cfg.getBool("negate", false);
		
		min = cfg.getInt("min", -1);
		max = cfg.getInt("max", 999);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		if (data == null) return false;
		
		PlayerClass cls = data.getClass("class");
		if (cls == null) return false;
		
		int level = cls.getLevel();
		boolean passes = level >= min && level <= max;
		return passes ^ negate;
	}

	@Override
	public String getExplanation(Player p) {
		String prefix = negate ? "You must not be " : "You must be ";
		if (min != -1 && max == 999) {
			return prefix + "at least level §e" + min;
		}
		else if (max != 999 && min == -1) {
			return prefix + "at most level §e" + max;
		}
		else {
			return prefix + "between level §e" + min + " §fand §e" + max;
		}
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new ClassLevelCondition(cfg);
	}
}
