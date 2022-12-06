package me.neoblade298.neoquests.quests;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class Questline implements Comparator<Questline> {
	private String key, display, fileLocation;
	private ArrayList<Quest> quests;

	public Questline(ConfigurationSection cfg, File file) throws NeoIOException {
		key = cfg.getName();
		fileLocation = file.getPath();
		display = cfg.getString("display");
		
		List<String> recs = cfg.getStringList("quests");
		quests = new ArrayList<Quest>(recs.size());
		for (String line : recs) {
			Quest q = QuestsManager.getQuest(line);
			if (q == null) {
				throw new NeoIOException("Quest in questline " + key + " doesn't exist: " + line);
			}
			q.setQuestline(this);
			quests.add(q);
		}
	}
	
	public Quest getFirstQuest() {
		return quests.get(0);
	}

	public Quest getNextQuest(Player p) {
		for (Quest quest : quests) {
			if (ConditionManager.getBlockingCondition(p, quest.getConditions()) == null) {
				return quest;
			}
		}
		return null;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public String getLastQuest() {
		return quests.get(quests.size() - 1).getKey();
	}
	
	public ArrayList<Quest> getQuests() {
		return quests;
	}

	@Override
	public int compare(Questline q1, Questline q2) {
		return q1.getKey().compareTo(q2.getKey());
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
}
