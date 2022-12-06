package me.neoblade298.neoquests.objectives;

import java.util.ArrayList;
import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.actions.RewardAction;

public class ObjectiveSet {
	private ArrayList<Objective> objs = new ArrayList<Objective>();
	private ArrayList<RewardAction> alternateRewards;
	private ActionSequence actions = new ActionSequence();
	private String key, display;
	private int next;
	
	public ObjectiveSet(ConfigurationSection cfg) throws NeoIOException {
		key = cfg.getName();
		display = cfg.getString("display", "Objectives").replaceAll("&", "§");
		next = cfg.getInt("next", -3);
		for (String line : cfg.getStringList("objectives")) {
			objs.add(ObjectiveManager.get(new LineConfig(line)));
		}
		this.alternateRewards = ActionManager.parseRewards(cfg.getStringList("rewards"));
		this.actions.load(cfg.getStringList("actions"));
	}
	
	public int getNext() {
		return next;
	}
	
	public ArrayList<RewardAction> getAlternateRewards() {
		return alternateRewards;
	}
	
	public boolean hasAlternateRewards() {
		return alternateRewards.size() > 0;
	}
	
	public ArrayList<Objective> getObjectives() {
		return objs;
	}
	
	public static ArrayList<ObjectiveSet> parseObjectiveSets(ConfigurationSection cfg) throws NeoIOException {
		ArrayList<ObjectiveSet> objectives = new ArrayList<ObjectiveSet>();
		for (String key : cfg.getKeys(false)) {
			objectives.add(new ObjectiveSet(cfg.getConfigurationSection(key)));
		}
		return objectives;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public String getKey() {
		return key;
	}
	
	public ActionSequence getActions() {
		return actions;
	}
}
