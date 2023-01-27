package me.neoblade298.neoquests.commands;

import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.bukkit.NeoCore;
import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.player.PlayerTags;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminReset extends Subcommand {
	public CmdQuestAdminReset(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false), new Arg("account #", false));
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
		
		UUID uuid = p.getUniqueId();
		try (Connection con = NeoCore.getConnection("QuestsManager");
				Statement stmt = con.createStatement();){
			// No args, reset only the account you're on
			if (args.length == offset) {
				int account = NeoQuests.isTowny ? 1 : SkillAPI.getPlayerAccountData(p).getActiveId();
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
}
