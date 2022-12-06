package me.neoblade298.neoquests.actions;
import net.md_5.bungee.api.ChatColor;

public interface DialogueAction extends Action, DelayableAction {
	public static final int CHARS_PER_TICK = 4;
	public static int getDelay(String text) {
		return 1 + (ChatColor.stripColor(text).length() / CHARS_PER_TICK);
	}
}
