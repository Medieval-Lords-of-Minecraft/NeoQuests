package me.neoblade298.neoquests.commands;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.bukkit.commands.CommandArgument;
import me.neoblade298.neocore.bukkit.commands.CommandArguments;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.listeners.ObjectiveListener;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;

public class CmdQuestAdminDebug implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player"));

	@Override
	public String getDescription() {
		return "Checks a player's listened objectives";
	}

	@Override
	public String getKey() {
		return "debug";
	}

	@Override
	public String getPermission() {
		return "neoquests.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = Bukkit.getPlayer(args[0]);
		
		if (p == null) {
			Util.msg(s, "&cCould not check objectives, player not online: " + args[0]);
		}
		for (Entry<ObjectiveEvent, ArrayList<ObjectiveInstance>> entry : ObjectiveListener.getPlayerInstances(p).entrySet()) {
			if (entry.getValue() == null || entry.getValue().size() == 0) continue;
			Util.msg(s, "&e" + entry.getKey().name() + ":", false);
			for (ObjectiveInstance oi : entry.getValue()) {
				Util.msg(s, oi.getObjective().getDisplay(), false);
			}
			
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
