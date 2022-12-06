package me.neoblade298.neoquests.actions;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class ActionSet {
	private ArrayList<Action> actions = new ArrayList<Action>();
	private int postDelay = 0;
	private boolean needsInstance = false;
	
	public ArrayList<Action> getActions() {
		return actions;
	}
	
	public void run(Player p) {
		for (Action action : actions) {
			action.run(p);
		}
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public boolean isEmpty() {
		return actions.isEmpty();
	}
	
	public boolean needsInstance() {
		return needsInstance;
	}
	
	public void setPostDelay(int postDelay) {
		this.postDelay = postDelay;
	}
	
	public int getPostDelay() {
		return postDelay;
	}
}
