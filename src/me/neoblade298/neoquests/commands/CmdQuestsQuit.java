package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsQuit extends Subcommand {
	public CmdQuestsQuit(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("key"));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		Quester q = QuestsManager.getQuester(p);
		// /quests quit
		if (args.length == 0) {
			if (q.getActiveQuests().size() > 1) {
				((Subcommand) NeoQuests.getCommands().get("quest").getCommand("")).run(s, args);
			}
			else if (q.getActiveQuests().size() == 1) {
				q.cancelQuest(q.getActiveQuests().iterator().next().getQuest().getKey());
			}
			else {
				Util.msg(p, "§cYou don't have any active quests!");
			}
		}
		else {
			q.cancelQuest(args[0]);
		}
	}
}
