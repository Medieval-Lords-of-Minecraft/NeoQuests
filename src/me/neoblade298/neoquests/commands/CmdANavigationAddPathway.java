package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.EndPoint;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.PathwayEditor;

public class CmdANavigationAddPathway implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("start"), new CommandArgument("end")));

	@Override
	public String getDescription() {
		return "Adds an existing pathway to your path";
	}

	@Override
	public String getKey() {
		return "addpathway";
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

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
