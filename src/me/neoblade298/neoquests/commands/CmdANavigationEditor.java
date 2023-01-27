package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.exceptions.NeoIOException;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationEditor extends Subcommand {
	public CmdANavigationEditor(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
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
}
