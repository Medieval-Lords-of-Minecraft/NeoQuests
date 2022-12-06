package me.neoblade298.neoquests.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import me.neoblade298.neoquests.NeoQuests;

public class GeneralListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasPermission("neoquests.admin")) {
			NeoQuests.addDebugger(e.getPlayer());
		}
	}
}
