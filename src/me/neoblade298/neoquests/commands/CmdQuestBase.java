package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestBase extends Subcommand {
	public CmdQuestBase(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p;
		if (args.length == 0) {
			p = (Player) s;
		}
		else {
			p = Bukkit.getPlayer(args[0]);
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
		q.displayQuests(s);
	}
}
