package me.neoblade298.neoquests.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.neoblade298.neocore.events.PlayerTagChangedEvent;
import me.neoblade298.neoquests.events.ConversationEvent;
import me.neoblade298.neoquests.objectives.*;
import me.neoblade298.neoquests.objectives.builtin.*;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class ObjectiveListener implements Listener {
	
	protected static HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>> objs = new HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>>();
	
	public static void startListening(ObjectiveInstance o) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap = getPlayerInstances(o.getPlayer());
		ObjectiveEvent event = o.getObjective().getType();
		if (event == null) {
			return; // Should only happen if an objective needs no listeners (Fake objective)
		}
		ArrayList<ObjectiveInstance> insts = pmap.getOrDefault(event, new ArrayList<ObjectiveInstance>());
		insts.add(o);
		pmap.putIfAbsent(event, insts);
	}
	
	public static void stopListening(Player p) {
		objs.remove(p);
	}
	
	public static void stopListening(ObjectiveInstance o) {
		getPlayerInstances(o.getPlayer()).remove(o.getObjective().getType());
	}

	public static HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> getPlayerInstances(Player p) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap = objs.getOrDefault(p, new HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>());
		objs.putIfAbsent(p, pmap);
		return pmap;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onInteractNPC(NPCRightClickEvent e) {
		Player p = e.getClicker();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.INTERACT_NPC);
		if (insts != null) {
			e.setCancelled(true);
			for (ObjectiveInstance o : insts) {
				if (o.getObjective() instanceof InteractNpcObjective) {
					((InteractNpcObjective) o.getObjective()).checkEvent(e, o);
				}
				else {
					((DeliverItemsObjective) o.getObjective()).checkEvent(e, o);
				}
			}
		}
	}
	
	@EventHandler
	public void onKillMythicMob(MythicMobDeathEvent e) {
		if (e.getKiller() == null || !(e.getKiller() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getKiller();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.KILL_MYTHICMOB);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((KillMythicmobsObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.MOVE);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ReachLocationObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onTagReceive(PlayerTagChangedEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.RECEIVE_TAG);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetTagObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onConversationResponse(ConversationEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.RESPOND_CONVERSATION);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((RespondConversationObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.CHAT);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				if (((SayObjective) o.getObjective()).checkEvent(e, o)) e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.COMMAND);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((RunCommandObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
}
