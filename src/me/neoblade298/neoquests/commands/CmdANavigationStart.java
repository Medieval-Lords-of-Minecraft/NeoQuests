package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationStart implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player", false), 
			new CommandArgument("start"),
			new CommandArgument("end")));

	@Override
	public String getDescription() {
		return "Starts navigation for a player";
	}

	@Override
	public String getKey() {
		return "start";
	}

	@Override
	public String getPermission() {
		return "neoquests.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = null;
		int offset = 0;
		if (args.length == 2) {
			p = (Player) s;
		}
		else {
			p = (Bukkit.getPlayer(args[0]));
			offset = 1;
		}
		
		if (p == null) {
			Util.msg(s, "&cPlayer is not online!");
		}
		NavigationManager.startNavigation(p, args[offset], args[offset + 1]);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
