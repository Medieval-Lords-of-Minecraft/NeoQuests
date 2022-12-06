package me.neoblade298.neoquests.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationExit implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Creates a new pathway editor";
	}

	@Override
	public String getKey() {
		return "exit";
	}

	@Override
	public String getPermission() {
		return "neoquests.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		NavigationManager.exitPathwayEditor(p);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
