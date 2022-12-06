package me.neoblade298.neoquests.conditions.builtin;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conditions.Condition;

public class HasGlobalTagCondition extends Condition {
	private static final String key;
	private String tag;
	private boolean negate;
	
	static {
		key = "has-global-tag";
	}
	
	public HasGlobalTagCondition() {}
	
	public HasGlobalTagCondition(LineConfig cfg) {
		super(cfg);
		tag = cfg.getString("tag", null);
		negate = cfg.getBool("negate", false);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		return NeoQuests.getGlobalPlayerTags().exists(tag, p.getUniqueId()) ^ negate;
	}

	@Override
	public String getExplanation(Player p) {
		return "You're missing a global tag somewhere!";
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new HasGlobalTagCondition(cfg);
	}
}
