package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationClear implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Removes all currently unused points";
	}

	@Override
	public String getKey() {
		return "clear";
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
		NavigationManager.clearUnusedPoints(s);
		Util.msg(s, "&7Finished clearing all unused points.");
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
