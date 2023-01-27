package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.shared.commands.Arg;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quest;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminComplete extends Subcommand {
	public CmdQuestAdminComplete(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("key"), new Arg("player", false), new Arg("stage", false), new Arg("success", false));
	}

	@Override
	public void run(CommandSender s, String[] args) {
		int stage = -1;
		boolean success = true;
		Player p = null;
		Quester quester = null;
		
		for (String arg : args) {
			if (Bukkit.getPlayer(arg) != null) {
				p = Bukkit.getPlayer(arg);
			}
			else if (StringUtils.isNumeric(arg)) {
				stage = Integer.parseInt(arg);
			}
			else if (arg.equalsIgnoreCase("false")) {
				success = false;
			}
		}
		
		if (p == null) {
			p = (Player) s;
		}
		
		quester = QuestsManager.getQuester(p);
		Quest q = QuestsManager.getQuest(args[0]);
		int lastStage = q.getStages().size() - 1;
		quester.addCompletedQuest(new CompletedQuest(QuestsManager.getQuest(args[0]), stage == -1 ? lastStage : stage, success));
		Util.msg(s, "&7Successfully added quest &6" + q.getDisplay() + " &7to player &e" + p.getName());
	}
}
