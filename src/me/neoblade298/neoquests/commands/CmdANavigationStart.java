package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationStart extends Subcommand {
	public CmdANavigationStart(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false), new Arg("start"), new Arg("end"));
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
}
