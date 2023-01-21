package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neocore.bukkit.commands.CommandArgument;
import me.neoblade298.neocore.bukkit.commands.CommandArguments;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminCanStart implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("key"),
			new CommandArgument("player", false)));

	@Override
	public String getDescription() {
		return "Returns if a player can start a quest, or the blocker if not";
	}

	@Override
	public String getKey() {
		return "canstart";
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
		if (args.length == 2) {
			p = Bukkit.getPlayer(args[1]);
		}
		else {
			p = (Player) s;
		}
		
		Quest q = QuestsManager.getQuest(args[0]);
		if (q == null) {
			Util.sendMessage(s, "&cPlayer cannot start quest: &6" + args[0] + "&c, quest doesn't exist.");
			return;
		}

		Condition c = ConditionManager.getBlockingCondition(p, q.getConditions());
		if (c != null) {
			Util.sendMessage(s, "&cPlayer cannot start quest: &6" + q.getDisplay() + "&c: " + c.getExplanation(p));
			return;
		}
		Util.sendMessage(s, "&7Player can start quest");
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
