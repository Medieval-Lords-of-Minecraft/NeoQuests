package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsGuide extends Subcommand {
	public CmdQuestsGuide(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false));
	}
	
	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		if (args.length == 1) {
			p = Bukkit.getPlayer(args[0]);
		}
		Quester q = QuestsManager.getQuester(p);
		q.displayGuide(s);
	}
}
