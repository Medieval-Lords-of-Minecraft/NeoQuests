package me.neoblade298.neoquests.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.exceptions.NeoIOException;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationSave extends Subcommand {
	public CmdANavigationSave(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("bidirectional", false));
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
}
