package me.neoblade298.neoquests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import com.sucy.skill.SkillAPI;

import me.neoblade298.neocore.bukkit.NeoCore;
import me.neoblade298.neocore.bukkit.commands.SubcommandManager;
import me.neoblade298.neocore.bukkit.InstanceType;
import me.neoblade298.neocore.bukkit.Manager;
import me.neoblade298.neocore.bukkit.player.PlayerTags;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neoquests.actions.ActionManager;
import me.neoblade298.neoquests.commands.*;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.GeneralListener;
import me.neoblade298.neoquests.listeners.NavigationListener;
import me.neoblade298.neoquests.listeners.ConversationListener;
import me.neoblade298.neoquests.listeners.ObjectiveListener;
import me.neoblade298.neoquests.listeners.ObjectiveListenerSkillAPI;
import me.neoblade298.neoquests.listeners.ObjectiveListenerTowny;
import me.neoblade298.neoquests.listeners.QuesterListener;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.objectives.ObjectiveManager;
import me.neoblade298.neoquests.quests.QuestsManager;
import me.neoblade298.neoquests.worldguard.RequiredTagFlagHandler;

public class NeoQuests extends JavaPlugin implements org.bukkit.event.Listener {
	public static Random rand = new Random();

	private static NeoQuests inst;
	private static HashSet<Player> debuggers = new HashSet<Player>();
	private static ArrayList<Manager> managers = new ArrayList<Manager>();
	private static HashMap<String, SubcommandManager> commands = new HashMap<String, SubcommandManager>();
	private static PlayerTags[] accountTags = new PlayerTags[12];
	private static PlayerTags globalTags;
	public static StringFlag REQ_TAG_FLAG;
	public static boolean isTowny = false;

	public void onEnable() {
		inst = this;
		isTowny = NeoCore.getInstanceType() == InstanceType.TOWNY;
		Bukkit.getServer().getLogger().info("NeoQuests Enabled");

		// Minimized initialization if session host
		for (int i = 1; i <= 12; i++) {
			accountTags[i - 1] = NeoCore.createPlayerTags("questaccount_" + i, this, true);
		}
		globalTags = NeoCore.createPlayerTags("questaccount_global", this, true);
		if (NeoCore.getInstanceType() == InstanceType.SESSIONS) {
			return;
		}

		getServer().getPluginManager().registerEvents(new ConversationListener(), this);
		getServer().getPluginManager().registerEvents(new ObjectiveListener(), this);
		if (NeoCore.getInstanceType() == InstanceType.TOWNY) {
			getServer().getPluginManager().registerEvents(new ObjectiveListenerTowny(), this);
		}
		else {
			getServer().getPluginManager().registerEvents(new ObjectiveListenerSkillAPI(), this);
			getServer().getPluginManager().registerEvents(new QuesterListener(), this);
		}
		getServer().getPluginManager().registerEvents(new GeneralListener(), this);
		getServer().getPluginManager().registerEvents(new NavigationListener(), this);

		initCommands();

		// Managers
		try {
			new ActionManager();
			new ObjectiveManager();
			new ConditionManager();
			QuestsManager qm = new QuestsManager();
			NeoCore.registerIOComponent(this, qm, "QuestsManager");
			managers.add(new ConversationManager());
			managers.add(qm);
			managers.add(new NavigationManager());
		} catch (Exception e) {
			showWarning("Failed to enable managers on startup", e);
		}

		// WorldGuard
		SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
		sessionManager.registerHandler(RequiredTagFlagHandler.FACTORY, null);
	}

	@Override
	public void onLoad() {
		// WorldGuard
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try {
			// create a flag with the name "my-custom-flag"
			StringFlag flag = new StringFlag("required-tag");
			registry.register(flag);
			REQ_TAG_FLAG = flag; // only set our field if there was no error
		} catch (FlagConflictException e) {
			// some other plugin registered a flag by the same name already.
			// you can use the existing flag, but this may cause conflicts - be sure to
			// check type
			e.printStackTrace();

		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void initCommands() {
		String cmd = "quest";
		SubcommandManager quest = new SubcommandManager(cmd, null, ChatColor.RED, this);
		quest.register(new CmdQuestBase("", "Shows current quests", null, SubcommandRunner.PLAYER_ONLY));
		commands.put(cmd, quest);

		cmd = "quests";
		SubcommandManager quests = new SubcommandManager(cmd, null, ChatColor.RED, this);
		quests.registerCommandList("");
		quests.register(new CmdQuestsQuit("quit", "Quits the specified quest", null, SubcommandRunner.PLAYER_ONLY));
		quests.register(new CmdQuestsTake("take", "Quits", null, SubcommandRunner.PLAYER_ONLY));
		quests.register(new CmdQuestsLog("log", "Lists all completed quests", null, SubcommandRunner.PLAYER_ONLY));
		quests.register(new CmdQuestsView("view", "Views a player's quests", null, SubcommandRunner.BOTH));
		quests.register(new CmdQuestsGuide("guide", "Useful guide for finding the next quest to take", null, SubcommandRunner.BOTH));
		quests.register(new CmdQuestsRecommended("recommended", "Lists recommended quests for the level", null, SubcommandRunner.PLAYER_ONLY));
		quests.register(new CmdQuestsChallenges("challenges", "Lists challenging quests for the level", null, SubcommandRunner.PLAYER_ONLY));
		quests.register(new CmdQuestsList("list", "Lists every uncompleted quest", null, SubcommandRunner.PLAYER_ONLY));
		commands.put(cmd, quests);

		cmd = "questadmin";
		SubcommandManager questadmin = new SubcommandManager(cmd, "neoquests.admin", ChatColor.DARK_RED, this);
		questadmin.registerCommandList("");
		questadmin.register(new CmdQuestAdminReload("reload", "Safely reloads quests", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminStart("start", "Instantly starts a quest ignoring conditions", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminCanStart("Checks if a player can start a quest", "Quits", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminCanStartConv("canstartconv", "Checks if a player can start a conversation", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminCompleteQL("completeql", "Places a successful questline in the player's quest log", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminAddTag("addtag", "Adds a tag to the player's current quest account", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminRemoveTag("removetag", "Removes a tag from the player's current quest account", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminReset("reset", "Resets a player's completed quests", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminComplete("complete", "Completes a quest for a player", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminIsComplete("iscomplete", "Checks if a quest is complete for a player", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminDebug("debug", "Checks a player's listened objectives", null, SubcommandRunner.BOTH));
		questadmin.register(new CmdQuestAdminSetStage("setstage", "Sets the stage for an active quest", null, SubcommandRunner.BOTH));
		commands.put(cmd, questadmin);

		cmd = "navigation";
		SubcommandManager navigation = new SubcommandManager(cmd, null, ChatColor.RED, this);
		navigation.registerCommandList("");
		navigation.register(new CmdNavigationTo("to", "Starts navigation to an endpoint", null, SubcommandRunner.PLAYER_ONLY));
		navigation.register(new CmdNavigationFrom("from", "Starts navigation from an endpoint", null, SubcommandRunner.PLAYER_ONLY));
		navigation.register(new CmdNavigationStart("start", "Starts navigation between two endpoints", null, SubcommandRunner.PLAYER_ONLY));
		navigation.register(new CmdNavigationStop("stop", "Ends current navigation", null, SubcommandRunner.PLAYER_ONLY));
		commands.put(cmd, navigation);

		cmd = "adminnavigation";
		SubcommandManager anavigation = new SubcommandManager(cmd, "neoquests.admin", ChatColor.DARK_RED, this);
		anavigation.registerCommandList("");
		anavigation.register(new CmdANavigationSave("save", "Saves your pathway editor", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationStart("start", "Starts navigation for a player", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationEditor("editor", "Starts navigation editor", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationExit("exit", "Exits navigation editor", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationTo("to", "Starts navigation to an endpoint", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationFrom("from", "Starts navigation from an endpoint", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationAddPathway("addpathway", "Adds an existing pathway to your path", null, SubcommandRunner.PLAYER_ONLY));
		anavigation.register(new CmdANavigationClear("clear", "Removes all currently unused points", null, SubcommandRunner.PLAYER_ONLY));
		commands.put(cmd, anavigation);

		cmd = "conversation";
		SubcommandManager cm = new SubcommandManager(cmd, null, ChatColor.RED, this);
		cm.register(new CmdConvAnswer("answer", "Answers an existing conversation", null, SubcommandRunner.PLAYER_ONLY));
	}

	public void onDisable() {
		for (Manager mngr : managers) {
			mngr.cleanup();
		}
		org.bukkit.Bukkit.getServer().getLogger().info("NeoQuests Disabled");
		super.onDisable();
	}

	public static NeoQuests inst() {
		return inst;
	}

	public static void showWarning(String warning) {
		Bukkit.getLogger().warning("[NeoQuests] " + warning);
		for (Player p : debuggers) {
			p.sendMessage(warning);
		}
	}

	public static void showWarning(String warning, Exception e) {
		Bukkit.getLogger().warning("[NeoQuests] " + warning);
		Bukkit.getLogger().warning("[NeoQuests] " + e.getMessage());
		for (Player p : debuggers) {
			p.sendMessage(warning);
			p.sendMessage(e.getMessage());
		}
		e.printStackTrace();
	}

	public static boolean reloadAll() {
		for (Manager mngr : managers) {
			mngr.reload();
			Bukkit.getLogger().info("[NeoQuests] Reloaded manager " + mngr.getKey());
		}
		return true;
	}

	public static void addDebugger(Player p) {
		debuggers.add(p);
	}

	public static HashMap<String, SubcommandManager> getCommands() {
		return commands;
	}

	public static PlayerTags getPlayerTags(Player p) {
		int account = isTowny ? 1 : SkillAPI.getPlayerAccountData(p).getActiveId();
		return getPlayerTags(account);
	}

	public static PlayerTags getPlayerTags(int account) {
		return accountTags[account - 1];
	}

	public static PlayerTags[] getAllPlayerTags() {
		return accountTags;
	}

	public static PlayerTags getGlobalPlayerTags() {
		return globalTags;
	}
}
