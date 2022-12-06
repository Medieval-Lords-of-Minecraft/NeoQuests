package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.PaginatedList;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsList implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player", false),
			new CommandArgument("page", false)));

	@Override
	public String getDescription() {
		return "Lists all quests available to you";
	}

	@Override
	public String getKey() {
		return "list";
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
				list.displayFooter(s, page, nextCmd, prevCmd);
			}
		}.runTaskAsynchronously(NeoQuests.inst());
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
