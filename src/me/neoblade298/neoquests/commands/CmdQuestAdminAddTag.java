package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;

public class CmdQuestAdminAddTag implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player"),
			new CommandArgument("tag"));

	@Override
	public String getDescription() {
		return "Adds a tag to a player's current quest account";
	}

	@Override
	public String getKey() {
		return "addtag";
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
		Player p = Bukkit.getPlayer(args[0]);
		
		if (p == null) {
			Util.msg(s, "&cCould not add quest tag, player not online: " + args[0]);
		}
		NeoQuests.getPlayerTags(p).set(args[1], p.getUniqueId());
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
