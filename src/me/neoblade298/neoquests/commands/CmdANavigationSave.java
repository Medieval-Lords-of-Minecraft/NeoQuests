package me.neoblade298.neoquests.commands;


import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationSave implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("bidirectional", false)));

	@Override
	public String getDescription() {
		return "Saves your pathway editor";
	}

	@Override
	public String getKey() {
		return "save";
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
		if (NavigationManager.getEditor(p) == null) {
			Util.msg(p, "§cYou aren't in a pathway editor!");
			return;
		}
		try {
			NavigationManager.getEditor(p).save(args.length == 0 ? false : args[0].equalsIgnoreCase("true"));
		} catch (NeoIOException e) {
			NeoQuests.showWarning("Failed to save pathway editor", e);
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
