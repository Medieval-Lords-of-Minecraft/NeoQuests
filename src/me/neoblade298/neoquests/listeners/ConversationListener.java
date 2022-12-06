package me.neoblade298.neoquests.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.neoblade298.neoquests.conversations.ConversationInstance;
import me.neoblade298.neoquests.conversations.ConversationManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class ConversationListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onInteractNPC(NPCRightClickEvent e) {
		Player p = e.getClicker();
		
		ConversationManager.startConversation(p, e.getNPC().getId());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		String msg = e.getMessage();
		if (msg.length() > 2 || !StringUtils.isNumeric(msg)) return; // Only allow short messages
		
		ConversationInstance ci = ConversationManager.getActiveConversation(p);
		if (msg.length() > 2 || !StringUtils.isNumeric(msg)) return; // Only allow short messages
		if (ci != null) {
			if (ci.chooseResponse(Integer.parseInt(msg) - 1)) {
				e.setCancelled(true);
			}
		}
	}
}
