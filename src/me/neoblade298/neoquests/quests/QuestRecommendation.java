package me.neoblade298.neoquests.quests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;

public class QuestRecommendation {
	private int min, max;
	private Quest quest;
	private String endpoint;
	
	public QuestRecommendation(LineConfig cfg) throws NeoIOException {
		this.min = cfg.getInt("min", 1);
		this.max = cfg.getInt("max", 60);
		this.endpoint = cfg.getString("endpoint", null);
		this.quest = QuestsManager.getQuest(cfg.getString("quest", "N/A").toUpperCase());
		
		if (this.quest == null) {
			throw new NeoIOException("Failed to load quest " + cfg.getString("quest", "N/A") + " for recommendation " + cfg.getFullLine());
		}
	}
	
	public boolean isRelevant(Player p) {
		int level = 1;
		try {
			level = SkillAPI.getPlayerData(p).getClass("class").getLevel();
		}
		catch (Exception e) {
			Bukkit.getLogger().info("[NeoQuests] Failed to check if recommendation was relevant, " + e.getMessage());
			return false;
		}
		return level >= min && level <= max;
	}
	
	public Quest getQuest() {
		return quest;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public String getEndpoint() {
		return endpoint;
	}
}
