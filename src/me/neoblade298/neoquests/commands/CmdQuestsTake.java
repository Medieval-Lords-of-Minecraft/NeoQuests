package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsTake extends Subcommand {
	public CmdQuestsTake(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("key"), new Arg("skip conversation", false));
		hidden = true;
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
}
