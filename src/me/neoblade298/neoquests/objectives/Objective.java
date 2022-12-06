package me.neoblade298.neoquests.objectives;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public abstract class Objective implements LineConfigParser<Objective> {
	protected LineConfig cfg;
	protected ObjectiveEvent type;
	protected ObjectiveSet set;
	protected int needed;
	protected String endpoint;
	protected boolean hide;
	
	public Objective() {}
	
	public Objective(ObjectiveEvent type, LineConfig cfg) {
		this(type, cfg, false);
	}
	
	public Objective(ObjectiveEvent type, LineConfig cfg, boolean hide) {
		this.cfg = cfg;
		this.type = type;
		this.needed = cfg.getInt("needed", 1);
		this.endpoint = cfg.getString("endpoint", null);
		this.hide = cfg.getBool("hide", hide);
	}
	
	public ObjectiveEvent getType() {
		return type;
	}
	public ObjectiveSet getSet() {
		return set;
	}
	public void setSet(ObjectiveSet set) {
		this.set = set;
	}
	public int getNeeded() {
		return needed;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public boolean isHidden() {
		return hide;
	}
	// Sets up obj count on quest startup, returns true if quest complete
	public void initialize(ObjectiveInstance oi) {}
	// Done after stage ends, basically just for GetStoredItem (to remove the items)
	public void finalize(Player p) { } 
	
	public abstract String getKey();
	public abstract String getDisplay();
}
