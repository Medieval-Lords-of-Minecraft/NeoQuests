package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.conversations.ConversationInstance;
import me.neoblade298.neoquests.conversations.ConversationManager;

public class CmdConvAnswer implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("number"));

	@Override
	public String getDescription() {
		return "Answers an existing conversation";
	}

	@Override
	public String getKey() {
		return "answer";
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
		ConversationInstance ci = ConversationManager.getActiveConversation(p);
		if (ci == null) {
			Util.msg(s, "&cYou're not in a conversation!");
		}
		else if (StringUtils.isNumeric(args[0])) {
			ci.chooseResponse(Integer.parseInt(args[0]) - 1);
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
