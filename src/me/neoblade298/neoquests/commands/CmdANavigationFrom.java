package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.navigation.EndPoint;
import me.neoblade298.neoquests.navigation.NavigationManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdANavigationFrom extends Subcommand {
	public CmdANavigationFrom(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("endpoint"), new Arg("player", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = null;
		if (args.length == 1) {
			p = (Player) s;
		}
		else {
			p = (Bukkit.getPlayer(args[1]));
		}
		
		if (p == null) {
			Util.msg(s, "&cPlayer is not online!");
		}
		
		EndPoint point = NavigationManager.getEndpoint(args[0]);
		int destsize = point.getDestinations().size();
		if (destsize > 0) {
			Util.msg(p, "Setting start point to &6" + point.getDisplay() + "&7. Choose a destination:");
			for (EndPoint dest : point.getDestinations().keySet()) {
				ComponentBuilder entry = new ComponentBuilder("§7- §6" + dest.getDisplay())
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nav start " + point.getKey() + " " + dest.getKey()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to set destination to " + dest.getDisplay())));
				p.spigot().sendMessage(entry.create());
			}
		}
		else if (destsize == 1) {
			String dest = null;
			for (EndPoint ep : point.getDestinations().keySet()) {
				dest = ep.getKey();
				break;
			}
			NavigationManager.startNavigation(p, args[0], dest);
		}
		else {
			Util.msg(p, "&cThis start point is not connected to any destinations!");
		}
	}
}
