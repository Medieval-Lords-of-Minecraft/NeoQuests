package me.neoblade298.neoquests.objectives.builtin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.classes.RPGClass;
import com.sucy.skill.api.event.PlayerClassChangeEvent;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class ReachTierObjective extends Objective {
	private int tier;
	private int level;
	
	public ReachTierObjective() {
		super();
	}

	public ReachTierObjective(LineConfig cfg) {
		super(ObjectiveEvent.CHANGE_CLASS, cfg);
		
		tier = cfg.getInt("tier", 1);
		level = tier == 1 ? 30 : 60;
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new ReachTierObjective(cfg);
	}

	@Override
	public String getKey() {
		return "reach-tier";
	}

	public boolean checkEvent(PlayerClassChangeEvent e, ObjectiveInstance o) {
		RPGClass c = e.getNewClass();
		return check(c, o);
	}
	
	@Override
	public void initialize(ObjectiveInstance oi) {
		PlayerData data = SkillAPI.getPlayerData(oi.getPlayer());
		if (data != null && data.getMainClass() != null) {
			check(SkillAPI.getPlayerData(oi.getPlayer()).getMainClass().getData(), oi);
		}
	}
	
	private boolean check(RPGClass c, ObjectiveInstance o) {
		if (c != null && c.getMaxLevel() >= level && o.getCount() == 0) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return "Reach tier " + 1;
	}

}
