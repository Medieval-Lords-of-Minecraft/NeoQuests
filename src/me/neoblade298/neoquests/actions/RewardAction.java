package me.neoblade298.neoquests.actions;

import me.neoblade298.neocore.shared.io.LineConfig;

public abstract class RewardAction implements Action {
	private boolean hide;
	
	public RewardAction() {}
	
	public RewardAction(LineConfig cfg) {
		this.hide = cfg.getBool("hide", false);
	}
	
	public abstract String getDisplay();
	public boolean isHidden() { return hide; }
}
