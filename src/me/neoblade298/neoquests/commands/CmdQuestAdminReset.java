package me.neoblade298.neoquests.commands;

import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.player.PlayerTags;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminReset implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player", false),
			new CommandArgument("account #", false)));

	@Override
	public String getDescription() {
		return "Resets a player's completed quests";
	}

	@Override
	public String getKey() {
		return "reset";
	}

	@Override
	public String getPermission() {
		return "neoquests.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = null;
		int offset = 0;
		if (args.length == 0) {
			p = (Player) s;
		}
		else if (args.length == 1 && StringUtils.isNumeric(args[0])) {
			p = (Player) s;
		}
		else if (Bukkit.getPlayer(args[0]) != null) {
			p = Bukkit.getPlayer(args[0]);
			offset = 1;
		}
		else {
			Util.msg(s, "&cSomething's wrong with the command arguments!");
		}
		
		Statement stmt = NeoCore.getStatement();
		UUID uuid = p.getUniqueId();
		try {
			// No args, reset only the account you're on
			if (args.length == offset) {
				int account = SkillAPI.getPlayerAccountData(p).getActiveId();
				stmt.execute("DELETE FROM quests_completed WHERE uuid = '" + uuid + "' AND account = " + account + ";");
				stmt.execute("DELETE FROM quests_accounts WHERE uuid = '" + uuid + "' AND account = " +  account + ";");
				stmt.execute("DELETE FROM quests_questlines WHERE uuid = '" + uuid + "' AND account = " + account + ";");
				stmt.execute("DELETE FROM quests_quests WHERE uuid = '" + uuid + "' AND account = " + account + ";");
				QuestsManager.getQuester(p).reset();
				NeoQuests.getPlayerTags(p).resetAllTags(uuid);
			}
			// Has args, reset a specific account
			else {
				if (!StringUtils.isNumeric(args[offset])) {
					Util.msg(s, "&cAccount must be a number!");
				}
				int acct = Integer.parseInt(args[offset]);
				Quester quester = QuestsManager.getQuester(p, acct);
				if (quester == null) {
					Util.msg(s, "&cThis account doesn't exist!");
				}
				stmt.execute("DELETE FROM quests_completed WHERE uuid = '" + uuid + "' AND account = " + args[1] + ";");
				stmt.execute("DELETE FROM quests_accounts WHERE uuid = '" + uuid + "' AND account = " + args[1] + ";");
				stmt.execute("DELETE FROM quests_questlines WHERE uuid = '" + uuid + "' AND account = " + args[1] + ";");
				stmt.execute("DELETE FROM quests_quests WHERE uuid = '" + uuid + "' AND account = " + args[1] + ";");
				quester.reset();
				PlayerTags pTags = NeoQuests.getPlayerTags(acct);
				pTags.resetAllTags(uuid);
			}
			ConversationManager.endConversation(p, false);
			Util.msg(s, "&7Successfully reset player &6" + p.getName() + ".");
		}
		catch (Exception e) {
			Util.msg(s, "&cCommand failed! Stack trace in console.");
			e.printStackTrace();
		} 
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
