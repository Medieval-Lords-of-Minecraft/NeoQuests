package me.neoblade298.neoquests.conversations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.events.ConversationEvent;

public class ConversationInstance {
	private Player p;
	private Conversation conv;
	private int stage = 0;
	
	public ConversationInstance(Player p, Conversation conv) {
		this.p = p;
		this.conv = conv;
		
		ActionSequence start = conv.getStartActions();
		start.run(p);
		int runtime = start.getRuntime();
		conv.getStage(0).run(p, runtime);
	}
	
	public void show() {
		this.conv.getStage(stage).show(p);
	}
	
	public void endConversation(boolean runActions) {
		if (runActions) {
			conv.getEndActions().run(p);
		}
	}
	
	public boolean chooseResponse(int num) {
		ConversationResponse resp = conv.getStage(stage).getValidResponses(p).get(num);
		if (resp != null) {
			if (resp.tryResponse(p)) {
				// Successfully choose response
				new BukkitRunnable() {
					public void run() {
						Bukkit.getPluginManager().callEvent(new ConversationEvent(p, conv, conv.getStage(stage), resp));
						int runtime = resp.getActions().run(p);
						
						// If no next, try stage + 1, else just stay the same
						if (resp.getNext() != -3) {
							stage = resp.getNext();
						}
						else if (stage + 1 < conv.getTotalStages()) {
							stage++;
						}

						if (stage == -1 || stage == -2) {
							ConversationManager.endConversation(p, stage == -1);
						}
						else {
							conv.getStage(stage).run(p, runtime);
						}
					}
				}.runTask(NeoQuests.inst());
			}
			return true;
		}
		else {
			return false;
		}
	}
}
