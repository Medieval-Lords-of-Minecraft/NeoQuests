package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdNavigationStart extends Subcommand {
	public CmdNavigationStart(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("start"), new Arg("end"), new Arg("player", false));
		hidden = true;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = null;
		if (args.length == 2) {
			p = (Player) s;
		}
		else {
			p = Bukkit.getPlayer(args[2]);
		}
		NavigationManager.startNavigation(p, args[0], args[1]);
	}
}
