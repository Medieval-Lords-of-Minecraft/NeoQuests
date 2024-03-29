package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.util.PaginatedList;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsList extends Subcommand {
	public CmdQuestsList(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false), new Arg("page", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		new BukkitRunnable() {
			public void run() {
				Player p = (Player) s;
				int offset = 0;
				if (args.length > 0 && Bukkit.getPlayer(args[0]) != null) {
					p = Bukkit.getPlayer(args[0]);
					offset++;
				}
				
				if (args.length > offset && !StringUtils.isNumeric(args[offset])) {
					Util.msg(s, "&cInvalid argument! Must be a page number.");
					return;
				}
				
				PaginatedList<Quest> list = new PaginatedList<Quest>();
				for (Quest q : QuestsManager.getQuests()) {
					if (ConditionManager.getBlockingCondition(p, q.getConditions()) == null) {
						list.add(q);
					}
				}
				
				int page = args.length > offset ? Integer.parseInt(args[offset]) - 1 : 0;
				if (page < 0 || page >= list.pages()) {
					Util.msg(s, "&cInvalid page number! Max page is " + list.pages());
					return;
				}
				
				Util.msg(s, "&6-[Available Quests]-", false);
				for (Quest q : list.get(page)) {
					String msg = "&7- ";
					msg += q.getDisplay();
					Util.msg(s, msg, false);
				}
				String nextCmd = "/quests list " + (page + 2);
				String prevCmd = "/quests list " + page;
				s.spigot().sendMessage(list.getFooter(page, nextCmd, prevCmd));
			}
		}.runTaskAsynchronously(NeoQuests.inst());
	}
}
