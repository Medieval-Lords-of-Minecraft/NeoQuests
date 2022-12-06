package me.neoblade298.neoquests.actions;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.builtin.StartQuestAction;

public class ActionSequence {
	private ArrayList<ActionSet> sets = new ArrayList<ActionSet>();
	private ActionSet curr = new ActionSet();
	private int runtime = 0;
	private String quest = null;
	
	// Used to avoid having to look for nulls
	public ActionSequence() {}
	
	public void load(List<String> list) throws NeoIOException {
		int delay = 0;
		for (String line : list) {
			LineConfig cfg = new LineConfig(line);
			
			Action action = ActionManager.get(cfg);
			
			if (action instanceof StartQuestAction) {
				quest = ((StartQuestAction) action).getQuest();
			}
			
			if (!(action instanceof EmptyAction)) { // DelayAction is empty
				addAction(action, runtime);
				delay = 0;
			}
			
			if (action instanceof DelayableAction) { // Delay is always after action
				int dl = ((DelayableAction) action).getDelay();
				runtime += dl;
				delay += dl;
			}
			
		}
		
		if (!curr.isEmpty()) {
			curr.setPostDelay(delay);
			sets.add(curr);
		}
	}
	
	private void addAction(Action action, int delay) {
		if (delay > 0) {
			curr.setPostDelay(delay);
			sets.add(curr);
			curr = new ActionSet();
		}
		curr.addAction(action);
	}
	
	public int run(Player p) {
		return run(p, 0);
	}
	
	public int run(Player p, int delay) {
		int tick = delay;
		for (ActionSet set : sets) {
			if (!set.isEmpty()) {
				if (tick != 0) {
					new BukkitRunnable() {
						public void run() {
							set.run(p);
						}
					}.runTaskLater(NeoQuests.inst(), tick);
				}
				else {
					set.run(p);
				}
			}
			tick += set.getPostDelay();
		}
		return tick;
	}
	
	public boolean isEmpty() {
		return sets.isEmpty();
	}
	
	public int getRuntime() {
		return runtime;
	}
	
	public String getQuest() {
		return quest;
	}
	
	public String toString() {
		String ts = "";
		for (ActionSet set : sets) {
			for (Action action : set.getActions()) {
				ts += action.getKey() + " ";
			}
		}
		return ts;
	}
}
