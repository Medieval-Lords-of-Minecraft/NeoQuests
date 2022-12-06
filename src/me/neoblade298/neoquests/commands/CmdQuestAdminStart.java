package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminStart implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("key"),
			new CommandArgument("player", false)));

	@Override
	public String getDescription() {
		return "Instantly starts a quest, ignoring all conditions";
	}

	@Override
	public String getKey() {
		return "start";
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
		Player p = null;
		if (args.length == 2) {
			p = Bukkit.getPlayer(args[1]);
		}
		else {
			p = (Player) s;
		}
		QuestsManager.startQuest(p, args[0], true);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
