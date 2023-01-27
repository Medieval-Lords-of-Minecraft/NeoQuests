package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminSetStage extends Subcommand {
	public CmdQuestAdminSetStage(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("key"), new Arg("player", false), new Arg("stage"));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = null;
		int stage = -1;
		if (args.length == 3) {
			p = Bukkit.getPlayer(args[1]);
			stage = Integer.parseInt(args[2]);
		}
		else {
			p = (Player) s;
			stage = Integer.parseInt(args[1]);
		}
		QuestsManager.setStage(p, args[0], stage);
	}
}
