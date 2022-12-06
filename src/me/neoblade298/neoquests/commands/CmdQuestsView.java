package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsView implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player")));

	@Override
	public String getDescription() {
		return "Checks another user's quests";
	}

	@Override
	public String getKey() {
		return "view";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
			Util.msg(s, "&cThat player is not online!");
			return;
		}
		
		QuestsManager.getQuester(p).displayQuests(s);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
