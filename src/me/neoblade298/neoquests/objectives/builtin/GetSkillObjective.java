package me.neoblade298.neoquests.objectives.builtin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;
import com.sucy.skill.api.player.PlayerData;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class GetSkillObjective extends Objective {
	private String skill;
	
	public GetSkillObjective() {
		super();
	}

	public GetSkillObjective(LineConfig cfg) {
		super(ObjectiveEvent.GET_SKILL, cfg, true);
		skill = cfg.getString("skill", null);
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new GetSkillObjective(cfg);
	}

	@Override
	public String getKey() {
		return "get-skill";
	}

	public boolean checkEvent(PlayerSkillUnlockEvent e, ObjectiveInstance o) {
		if (skill == null) {
			o.incrementCount();
			return true;
		}
		else if (e.getUnlockedSkill().getData().getName().equalsIgnoreCase(skill)) {
			o.incrementCount();
			return true;
		}
		return false;
	}
	
	public boolean checkEvent(PlayerSkillUpgradeEvent e, ObjectiveInstance o) {
		if (skill == null) {
			o.incrementCount();
			return true;
		}
		else if (e.getUpgradedSkill().getData().getName().equalsIgnoreCase(skill)) {
			o.incrementCount();
			return true;
		}
		return false;
	}

	@Override
	public String getDisplay() {
		return "Hide this objective!";
	}
	
	@Override
	public void initialize(ObjectiveInstance oi) {
		if (skill != null) {
			PlayerData data = SkillAPI.getPlayerData(oi.getPlayer());
			if (data != null && data.hasSkill(skill)) {
				oi.incrementCount();
			}
		}
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}
}
