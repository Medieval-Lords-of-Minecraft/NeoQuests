package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsTake implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("key"),
			new CommandArgument("skip conversation", false)));

	@Override
	public String getDescription() {
		return "Starts the specified quest";
	}

	@Override
	public String getKey() {
		return "take";
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
		Player p = (Player) s;
		String conv = QuestsManager.getQuest(args[0]).getStartConversation();
		if (conv != null) {
			ConversationManager.startConversation(p, conv, false);
		}
		else {
			QuestsManager.startQuest(p, args[0]);
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
