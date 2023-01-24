package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.bukkit.commands.CommandArgument;
import me.neoblade298.neocore.bukkit.commands.CommandArguments;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.BukkitUtil;
import me.neoblade298.neoquests.navigation.EndPoint;
import me.neoblade298.neoquests.navigation.NavigationManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdNavigationFrom implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("endpoint")));

	@Override
	public String getDescription() {
		return "Starts navigation from an endpoint";
	}

	@Override
	public String getKey() {
		return "from";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		
		EndPoint point = NavigationManager.getEndpoint(args[0]);
		int destsize = point.getDestinations().size();
		if (destsize > 0) {
			BukkitUtil.msg(p, "Setting start point to &6" + point.getDisplay() + "&7. Choose a destination:");
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
			BukkitUtil.msg(p, "&cThis start point is not connected to any destinations!");
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
