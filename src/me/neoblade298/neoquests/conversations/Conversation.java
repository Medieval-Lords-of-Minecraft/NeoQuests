package me.neoblade298.neoquests.conversations;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class Conversation {
	private String key, fileLocation;
	private ArrayList<Condition> conditions;
	private ArrayList<ConversationStage> stages;
	private ActionSequence startActions = new ActionSequence(), endActions = new ActionSequence();
	
	public Conversation(File file, ConfigurationSection cfg) throws NeoIOException {
		this.key = cfg.getName();
		this.fileLocation = file.getPath();
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		stages = new ArrayList<ConversationStage>();
		ConfigurationSection scfg = cfg.getConfigurationSection("stages");
		for (String stageNum : scfg.getKeys(false)) {
			int num = Integer.parseInt(stageNum);
			stages.add(new ConversationStage(scfg.getConfigurationSection(stageNum), num));
		}
		
		this.startActions.load(cfg.getStringList("start-actions"));
		this.endActions.load(cfg.getStringList("end-actions"));
	}
	
	public String getKey() {
		return key;
	}
	
	public ConversationStage getStage(int num) {
		return stages.get(num);
	}
	
	public int getTotalStages() {
		return stages.size();
	}
	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}
	
	public ActionSequence getStartActions() {
		return startActions;
	}
	
	public ActionSequence getEndActions() {
		return endActions;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public ArrayList<ConversationStage> getStages() {
		return stages;
	}
}
