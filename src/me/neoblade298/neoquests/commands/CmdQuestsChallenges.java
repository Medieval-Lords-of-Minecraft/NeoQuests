package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class CmdQuestsChallenges implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("page", false)));

	@Override
	public String getDescription() {
		return "Lists challenging quests for you to take";
	}

	@Override
	public String getKey() {
		return "challenges";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		CmdQuestsRecommended.run(s, args, true);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
