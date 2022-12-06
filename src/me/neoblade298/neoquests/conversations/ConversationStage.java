package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.actions.DialogueAction;

public class ConversationStage {
	private int num;
	private String text;
	private DialogueAction textAction;
	private ActionSequence actions = new ActionSequence();
	private ArrayList<ConversationResponse> responses = new ArrayList<ConversationResponse>();
	
	public ConversationStage(ConfigurationSection cfg, int num) throws NeoIOException {
		this.num = num;
		String text = cfg.getString("text");
		LineConfig tcfg = new LineConfig(text);
		if (ActionManager.isDialogueAction(tcfg.getKey())) {
			this.textAction = (DialogueAction) ActionManager.get(tcfg);
		}
		else {
			this.text = text;
		}
		
		actions.load(cfg.getStringList("actions"));

		ConfigurationSection rcfg = cfg.getConfigurationSection("responses");
		for (String key : rcfg.getKeys(false)) {
			responses.add(new ConversationResponse(rcfg.getConfigurationSection(key)));
		}
		responses.add(new ConversationResponse());
	}
	
	public int getNumber() {
		return num;
	}
	
	public ConversationResponse getResponse(int num) {
		return responses.size() > num ? responses.get(num) : null;
	}
	
	public ArrayList<ConversationResponse> getResponses() {
		return responses;
	}
	
	public ActionSequence getActions() {
		return actions;
	}
	
	public void run(Player p) {
		run(p, 0);
	}
	
	public void run(Player p, int delay) {
		int runtime = actions.run(p, delay);
		new BukkitRunnable() {
			public void run() {
				show(p);
			}
		}.runTaskLater(NeoQuests.inst(), delay + runtime);
	}
	
	public void show(Player p) {
		if (textAction != null) {
			textAction.run(p);
		}
		else {
			p.sendMessage(text);
		}
		int num = 1;
		for (ConversationResponse resp : responses) {
			if (resp.showResponse(p, num)) {
				num++;
			}
		}
	}
	
	public ArrayList<ConversationResponse> getValidResponses(Player p) {
		ArrayList<ConversationResponse> resps = new ArrayList<ConversationResponse>();
		for (ConversationResponse resp : responses) {
			if (resp.isValidResponse(p)) {
				resps.add(resp);
			}
		}
		return resps;
	}
}
