package me.neoblade298.neoquests.conditions.builtin;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class QuestCompletedCondition extends Condition {
	private static final String key;
	private String questname;
	private boolean negate;
	private int stage;
	
	static {
		key = "quest-completed";
	}
	
	public QuestCompletedCondition() {}
	
	public QuestCompletedCondition(LineConfig cfg) {
		super(cfg);
		negate = cfg.getBool("negate", false);
		
		questname = cfg.getString("quest", "N/A").toUpperCase();
		stage = cfg.getInt("stage", -1);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public boolean passes(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		if (!negate) {
			if (cq != null) {
				if (stage != -1) {
					return cq.getStage() == stage;
				}
				return true;
			}
			return false;
		}
		else {
			if (cq != null) {
				return stage != -1 && cq.getStage() != stage;
			}
			return true;
		}
	}

	@Override
	public String getExplanation(Player p) {
		CompletedQuest cq = QuestsManager.getQuester(p).getCompletedQuest(questname);
		if (!negate) {
			if (cq != null) {
				if (stage != -1) {
					return "You must complete quest " + cq.getQuest().getDisplay() + " in a different way!";
				}
				return "Error";
			}
			return "Quest " + QuestsManager.getQuest(questname).getDisplay() + " is not complete!";
		}
		else {
			if (cq != null) {
				if (stage == -1) {
					return "Quest " + QuestsManager.getQuest(questname).getDisplay() + " must not be complete!";
				}
				else {
					return "You must complete quest " + cq.getQuest().getDisplay() + " in a different way!";
				}
			}
			return "Error";
		}
	}

	@Override
	public Condition create(LineConfig cfg) {
		return new QuestCompletedCondition(cfg);
	}
}
