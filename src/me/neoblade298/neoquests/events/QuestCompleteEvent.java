package me.neoblade298.neoquests.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player p;
    private boolean success;
    
	public QuestCompleteEvent(Player p, boolean success) {
		this.p = p;
		this.success = success;
	}

	public Player getPlayer() {
		return p;
	}
	
	public boolean getSuccess() {
		return success;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
