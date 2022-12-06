package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationEditor implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Starts pathway editor";
	}

	@Override
	public String getKey() {
		return "editor";
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
		try {
			NavigationManager.startPathwayEditor(p);
		} catch (NeoIOException e) {
			NeoQuests.showWarning("Failed to start pathway editor", e);
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
