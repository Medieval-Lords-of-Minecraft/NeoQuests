package me.neoblade298.neoquests.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationExit extends Subcommand {
	public CmdANavigationExit(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		NavigationManager.exitPathwayEditor(p);
	}
}
