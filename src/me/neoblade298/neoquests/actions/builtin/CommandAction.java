package me.neoblade298.neoquests.actions.builtin;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.actions.Action;
import me.neoblade298.neoquests.actions.DialogueAction;
import me.neoblade298.neoquests.actions.RewardAction;

public class CommandAction extends RewardAction {
	private static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private static String key = "command";
	private String command;
	private boolean isConsole;

	public static void register(HashMap<String, Action> actions, HashMap<String, DialogueAction> dialogueActions) {
		actions.put(key, new CommandAction());
	}
	
	public CommandAction() {}
	
	public CommandAction(LineConfig cfg) {
		super(cfg);
		this.command = cfg.getLine();
		this.isConsole = cfg.getBool("isConsole", true);
	}

	@Override
	public Action create(LineConfig cfg) {
		return new CommandAction(cfg);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDisplay() {
		return null;
	}

	@Override
	public void run(Player p) {
		if (isConsole) {
			Bukkit.dispatchCommand(console, command.replaceAll("<player>", p.getName()));
		}
		else {
			Bukkit.dispatchCommand(p, command.replaceAll("<player>", p.getName()));
		}
	}

	@Override
	public boolean isHidden() {
		return true;
	}
	
	public String getCommand() {
		return command;
	}
}
