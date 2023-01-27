package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.navigation.EndPoint;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.PathwayEditor;

public class CmdANavigationAddPathway extends Subcommand {
	public CmdANavigationAddPathway(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("start"), new Arg("end"));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) {
			Util.msg(p, "&cYou need to be in the editor to use this command!");
			return;
		}
		
		EndPoint start = NavigationManager.getEndpoint(args[0].toUpperCase());
		EndPoint end = NavigationManager.getEndpoint(args[1].toUpperCase());
		if (start == null) {
			Util.msg(p, "&cThe start point doesn't exist!");
			return;
		}
		if (end == null) {
			Util.msg(p, "&cThe destination point doesn't exist!");
			return;
		}
		
		editor.addExistingPathway(p, start, end);
	}
}
