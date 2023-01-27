package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminIsComplete extends Subcommand {
	public CmdQuestAdminIsComplete(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("key"), new Arg("player", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p;
		if (args.length == 1) {
			p = (Player) s;
		}
		else {
			p = Bukkit.getPlayer(args[1]);
			if (p == null) {
				Util.msg(s, "&cThis player is not online!");
				return;
			}
		}
		
		Quester q = QuestsManager.getQuester(p);
		if (q == null) {
			Util.msg(s, "&cThis account hasn't loaded in yet! Try again in a few seconds.");
			return;
		}
		CompletedQuest cq = q.getCompletedQuest(args[0]);
		if (cq != null) {
			Util.msg(s, q.getPlayer().getName() + " &7has completed the quest &e" + cq.getQuest().getDisplay());
		}
		else {
			Util.msg(s, q.getPlayer().getName() + " &7has not completed this quest");
		}
	}
}
