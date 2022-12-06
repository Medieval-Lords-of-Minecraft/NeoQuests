package me.neoblade298.neoquests.objectives.builtin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class ReachLevelObjective extends Objective {
	private int level;
	
	public ReachLevelObjective() {
		super();
	}

	public ReachLevelObjective(LineConfig cfg) {
		super(ObjectiveEvent.LEVEL_UP, cfg);
		
		level = cfg.getInt("level", -1);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new ReachLevelObjective(cfg);
	}

	@Override
	public String getKey() {
		return "reach-level";
	}

	public boolean checkEvent(PlayerLevelUpEvent e, ObjectiveInstance o) {
		return check(e.getLevel(), o);
	}
	
	@Override
	public void initialize(ObjectiveInstance oi) {
		PlayerData data = SkillAPI.getPlayerData(oi.getPlayer());
		if (data != null && data.getMainClass() != null) {
			check(data.getMainClass().getLevel(), oi);
		}
	}
	
	private boolean check(int level, ObjectiveInstance o) {
		if (level >= this.level && o.getCount() == 0) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return "Reach level " + level;
	}

}
