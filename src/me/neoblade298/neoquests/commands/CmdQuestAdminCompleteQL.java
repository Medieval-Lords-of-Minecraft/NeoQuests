package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.Questline;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminCompleteQL extends Subcommand {
	public CmdQuestAdminCompleteQL(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("key"), new Arg("player"));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = Bukkit.getPlayer(args[1]);
		Quester quester = null;
		
		quester = QuestsManager.getQuester(p);
		Questline ql = QuestsManager.getQuestline(args[0]);
		for (Quest q : ql.getQuests()) {
			quester.addCompletedQuest(new CompletedQuest(q, -1, true));
		}
		Util.msg(s, "&7Successfully added questline &6" + ql.getDisplay() + " &7to player &e" + p.getName());
	}
}
