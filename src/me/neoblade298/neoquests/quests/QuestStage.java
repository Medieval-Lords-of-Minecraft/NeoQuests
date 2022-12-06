package me.neoblade298.neoquests.quests;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.objectives.ObjectiveSet;

public class QuestStage {
	private Quest quest;
	private ArrayList<ObjectiveSet> objectives;
	private ActionSequence actions = new ActionSequence();
	
	public QuestStage(ConfigurationSection cfg, Quest quest) throws NeoIOException {
		this.quest = quest;
		this.objectives = ObjectiveSet.parseObjectiveSets(cfg.getConfigurationSection("objective-sets"));
		this.actions.load(cfg.getStringList("actions"));
	}
	
	public int runActions(Player p) {
		actions.run(p);
		return actions.getRuntime();
	}
	
	public ArrayList<ObjectiveSet> getObjectives() {
		return objectives;
	}
	
	public static ArrayList<QuestStage> parseQuestStages(ConfigurationSection cfg, Quest quest) throws NeoIOException {
		ArrayList<QuestStage> stages = new ArrayList<QuestStage>();
		for (String key : cfg.getKeys(false)) {
			ConfigurationSection sec = cfg.getConfigurationSection(key);
			stages.add(new QuestStage(sec, quest));
		}
		return stages;
	}
	
	public Quest getQuest() {
		return quest;
	}
}
