package me.neoblade298.neoquests.conditions.builtin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CopyQuestConditionsCondition extends Condition {
	private static final String key;
	private String questname;
	
	static {
		key = "copy-quest-conditions";
	}
	
	public CopyQuestConditionsCondition() {}
	
	public CopyQuestConditionsCondition(LineConfig cfg) {
		super(cfg);
		
		questname = cfg.getString("quest", "N/A").toUpperCase();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		Quest q = QuestsManager.getQuest(questname);
		if (q == null) {
			Bukkit.getLogger().warning("[NeoQuests] CopyQuestCondition failed to find quest " + questname);
			NeoQuests.showWarning("CopyQuestCondition failed to find quest " + questname);
			return false;
		}
		return ConditionManager.getBlockingCondition(p, q.getConditions()) == null;
	}

	@Override
	public String getExplanation(Player p) {
		return ConditionManager.getBlockingCondition(p, QuestsManager.getQuest(questname).getConditions()).getExplanation(p);
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new CopyQuestConditionsCondition(cfg);
	}
}
