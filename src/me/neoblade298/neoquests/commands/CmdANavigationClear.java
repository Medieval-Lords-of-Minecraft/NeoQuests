package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationClear extends Subcommand {
	public CmdANavigationClear(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
	}

	@Override
	public void run(CommandSender s, String[] args) {
		NavigationManager.clearUnusedPoints(s);
		Util.msg(s, "&7Finished clearing all unused points.");
	}
}
