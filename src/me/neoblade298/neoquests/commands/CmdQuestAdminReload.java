package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;


import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.NeoQuests;

public class CmdQuestAdminReload extends Subcommand {
	public CmdQuestAdminReload(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
	}

	@Override
	public void run(CommandSender s, String[] args) {
		if (NeoQuests.reloadAll()) {
			s.sendMessage("§4[§c§lMLMC§4] §7Successful reload.");
		}
		else {
			s.sendMessage("§4[§c§lMLMC§4] §cReload failed! Check the error messages!");
		}
	}
}
