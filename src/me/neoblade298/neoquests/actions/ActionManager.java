package me.neoblade298.neoquests.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigManager;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.actions.builtin.*;

public class ActionManager {
	private static LineConfigManager<Action> mngr = new LineConfigManager<Action>(NeoQuests.inst(), "actions");
	private static HashSet<String> dialogueActions = new HashSet<String>();
	
	public ActionManager() {
		dialogueActions.add("npc");
		dialogueActions.add("player");
		dialogueActions.add("desc");
		dialogueActions.add("msg");

		mngr.register(new CommandAction());
		mngr.register(new DelayAction());
		mngr.register(new DescriptionDialogueAction());
		mngr.register(new FakeAction());
		mngr.register(new GiveClassExpAction());
		mngr.register(new GiveEffectAction());
		mngr.register(new GiveEssenceAction());
		mngr.register(new GiveGlobalTagAction());
		mngr.register(new GiveMoneyAction());
		mngr.register(new GiveProfessionExpAction());
		mngr.register(new GiveStoredItemAction());
		mngr.register(new GiveTagAction());
		mngr.register(new LeaveQuestlineAction());
		mngr.register(new MessageDialogueAction());
		mngr.register(new NPCDialogueAction());
		mngr.register(new PlayerDialogueAction());
		mngr.register(new PlaySoundAction());
		mngr.register(new StartConversationAction());
		mngr.register(new StartQuestAction());
		mngr.register(new StartQuestlineAction());
		mngr.register(new TeleportAction());
	}
	
	public static ArrayList<RewardAction> parseRewards(List<String> actionLines) throws NeoIOException {
		ArrayList<RewardAction> rs = new ArrayList<RewardAction>(actionLines.size());
		for (String line : actionLines) {
			try {
				Action action = mngr.get(new LineConfig(line));
				if (action instanceof RewardAction) {
					rs.add((RewardAction) action);
				}
			}
			catch (Exception e) {
				throw new NeoIOException("Failed to load reward action line: " + line);
			}
		}
		return rs;
	}
	
	public static Action get(LineConfig cfg) throws NeoIOException {
		return mngr.get(cfg);
	}
	
	public static boolean isDialogueAction(String key) {
		return dialogueActions.contains(key.toLowerCase());
	}
}
