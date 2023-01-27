package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.conversations.ConversationInstance;
import me.neoblade298.neoquests.conversations.ConversationManager;

public class CmdConvAnswer extends Subcommand {
	public CmdConvAnswer(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("number"));
		hidden = true;
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
}
