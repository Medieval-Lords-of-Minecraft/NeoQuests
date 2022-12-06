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

import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.sucy.skill.api.event.PlayerClassChangeEvent;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.Neoblade298.NeoProfessions.Events.OpenProfessionInvEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionCraftSuccessEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionPlantSeedEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionSlotSuccessEvent;
import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.neoblade298.neocore.events.PlayerTagChangedEvent;
import me.neoblade298.neoquests.events.ConversationEvent;
import me.neoblade298.neoquests.objectives.*;
import me.neoblade298.neoquests.objectives.builtin.*;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class ObjectiveListener implements Listener {
	
	private static HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>> objs = new HashMap<Player, HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>>();
	
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

	private static HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> getPlayerInstances(Player p) {
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
	public void onReceiveStoredItem(ReceiveStoredItemEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.RECEIVE_STORED_ITEM);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetStoredItemObjective) o.getObjective()).checkEvent(e, o);
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
	public void onLevelUp(PlayerLevelUpEvent e) {
		Player p = e.getPlayerData().getPlayer();

		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.LEVEL_UP);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ReachLevelObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onChangeClass(PlayerClassChangeEvent e) {
		Player p = e.getPlayerData().getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.CHANGE_CLASS);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ReachTierObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onUnlockSkill(PlayerSkillUnlockEvent e) {
		Player p = e.getPlayerData().getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.GET_SKILL);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetSkillObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onUpgradeSkill(PlayerSkillUpgradeEvent e) {
		Player p = e.getPlayerData().getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.GET_SKILL);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetSkillObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onProfessionInvOpen(OpenProfessionInvEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.OPEN_PROFESSION_INV);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((OpenProfessionInventoryObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onPlantSeed(ProfessionPlantSeedEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.PLANT_SEED);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((PlantSeedObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onProfessionCraft(ProfessionCraftSuccessEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.PROFESSION_CRAFT);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ProfessionCraftObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onSlotAugment(ProfessionSlotSuccessEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.SLOT_AUGMENT);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((SlotAugmentObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onJoinTown(TownAddResidentEvent e) {
		Player p = e.getResident().getPlayer();
		
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.JOIN_TOWN);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((JoinTownObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onCreateTown(NewTownEvent e) {
		Player p = e.getTown().getMayor().getPlayer();
		
		ArrayList<ObjectiveInstance> insts = getPlayerInstances(p).get(ObjectiveEvent.CREATE_TOWN);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((CreateTownObjective) o.getObjective()).checkEvent(e, o);
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
