package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;

public class CmdQuestsChallenges extends Subcommand {
	public CmdQuestsChallenges(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("page", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		CmdQuestsRecommended.run(s, args, true);
	}
}
