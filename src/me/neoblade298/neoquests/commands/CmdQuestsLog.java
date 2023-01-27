package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.util.PaginatedList;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsLog extends Subcommand {
	public CmdQuestsLog(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		int offset = 0;
		if (args.length > 0 && Bukkit.getPlayer(args[0]) != null) {
			p = Bukkit.getPlayer(args[0]);
			offset++;
		}
		
		Quester quester = QuestsManager.initializeOrGetQuester(p);
		if (quester.getCompletedQuests().size() == 0) {
			Util.msg(s, "&cYou don't yet have any completed quests!");
			return;
		}
		
		if (args.length > offset && !StringUtils.isNumeric(args[offset])) {
			Util.msg(s, "&cInvalid argument! Must be a page number.");
			return;
		}
		
		PaginatedList<CompletedQuest> list = new PaginatedList<CompletedQuest>(quester.getCompletedQuests());
		int page = args.length > offset ? Integer.parseInt(args[offset]) - 1 : 0;
		if (page < 0 || page >= list.pages()) {
			Util.msg(s, "&cInvalid page number! Max page is " + list.pages());
			return;
		}
		
		Util.msg(s, "&6-[Quest Log]-", false);
		for (CompletedQuest cq : list.get(page)) {
			String msg = "&7- ";
			msg += cq.isSuccess() ? "&a" : "&c";
			msg += cq.getQuest().getDisplay();
			Util.msg(s, msg, false);
		}
		String nextCmd = "/quests log " + p.getName() + " "+ (page + 2);
		String prevCmd = "/quests log " + p.getName() + " " + page;
		s.spigot().sendMessage(list.getFooter(page, nextCmd, prevCmd));
	}
}
