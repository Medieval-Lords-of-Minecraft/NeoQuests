package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.Questline;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminCompleteQL implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("key"),
			new CommandArgument("player")));

	@Override
	public String getDescription() {
		return "Places a successful questline in the player's quest log";
	}

	@Override
	public String getKey() {
		return "completeql";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
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

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
