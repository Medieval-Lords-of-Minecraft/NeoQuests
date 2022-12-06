package me.neoblade298.neoquests.conditions.builtin;

import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.conditions.Condition;

public class IsMayorCondition extends Condition {
	private static final String key;
	private boolean negate;
	
	static {
		key = "is-mayor";
	}
	
	public IsMayorCondition() {}
	
	public IsMayorCondition(LineConfig cfg) {
		super(cfg);
		negate = cfg.getBool("negate", false);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		return TownyAPI.getInstance().getResident(p).isMayor() ^ negate;
	}

	@Override
	public String getExplanation(Player p) {
		return negate ? "You must not be a town mayor!" : "You must be a town mayor!";
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new IsMayorCondition(cfg);
	}
}
