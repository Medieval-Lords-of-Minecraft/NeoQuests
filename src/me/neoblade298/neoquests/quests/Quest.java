package me.neoblade298.neoquests.quests;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.actions.RewardAction;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.conditions.builtin.QuestTakeableCondition;

public class Quest {
	private String key, display, startConv, fileLocation;
	private ArrayList<Condition> conditions;
	private ArrayList<RewardAction> rewards;
	private ArrayList<QuestStage> stages;
	private Questline ql;
	private boolean canRepeat = false, canRetry = false;
	
	public Quest(ConfigurationSection cfg, File file) throws NeoIOException {
		this.key = cfg.getName();
		this.fileLocation = file.getPath();
		
		this.display = cfg.getString("display");
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		this.rewards = ActionManager.parseRewards(cfg.getStringList("rewards"));
		this.startConv = cfg.getString("start-conversation");
		
		this.stages = QuestStage.parseQuestStages(cfg.getConfigurationSection("stages"), this);
		this.canRepeat = cfg.getBoolean("repeatable", false);
		this.canRetry = cfg.getBoolean("retryable", false);
		
		if (!canRetry) {
			conditions.add(new QuestTakeableCondition(this.key));
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public ArrayList<QuestStage> getStages() {
		return stages;
	}
	
	public ArrayList<RewardAction> getRewards() {
		return rewards;
	}
	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}
	
	public boolean canRepeat() {
		return canRepeat;
	}
	
	public boolean canRetry() {
		return canRetry;
	}
	
	public void setQuestline(Questline ql) throws NeoIOException {
		if (this.ql != null) {
			throw new NeoIOException("Quest " + this.key + " is already in questline " + this.ql.getKey() + ", can't set it in other questline " + ql.getKey());
		}
		this.ql = ql;
	}
	
	public Questline getQuestline() {
		return ql;
	}
	
	public String getStartConversation() {
		return startConv;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
}
