package me.neoblade298.neoquests.conditions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public abstract class Condition implements LineConfigParser<Condition> {
	protected ConditionResult result;
	
	public Condition() {}
	
	public Condition(LineConfig cfg) {
		this.result = ConditionResult.valueOf(cfg.getString("result", "INVISIBLE").toUpperCase());
	}
	
	public abstract boolean passes(Player p);
	public abstract String getExplanation(Player p);
	
	public ConditionResult getResult() {
		return this.result;
	}
}
