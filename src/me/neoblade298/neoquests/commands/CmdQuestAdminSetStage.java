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

public class CmdQuestAdminSetStage implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("key"),
			new CommandArgument("player", false), new CommandArgument("stage")));

	@Override
	public String getDescription() {
		return "Sets the stage of a quest";
	}

	@Override
	public String getKey() {
		return "setstage";
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
		Player p = null;
		int stage = -1;
		if (args.length == 3) {
			p = Bukkit.getPlayer(args[1]);
			stage = Integer.parseInt(args[2]);
		}
		else {
			p = (Player) s;
			stage = Integer.parseInt(args[1]);
		}
		QuestsManager.setStage(p, args[0], stage);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
