package me.neoblade298.neoquests.listeners;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sucy.skill.api.event.PlayerClassChangeEvent;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;

import me.Neoblade298.NeoProfessions.Events.OpenProfessionInvEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionCraftSuccessEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionPlantSeedEvent;
import me.Neoblade298.NeoProfessions.Events.ProfessionSlotSuccessEvent;
import me.Neoblade298.NeoProfessions.Events.ReceiveStoredItemEvent;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import me.neoblade298.neoquests.objectives.builtin.GetSkillObjective;
import me.neoblade298.neoquests.objectives.builtin.GetStoredItemObjective;
import me.neoblade298.neoquests.objectives.builtin.OpenProfessionInventoryObjective;
import me.neoblade298.neoquests.objectives.builtin.PlantSeedObjective;
import me.neoblade298.neoquests.objectives.builtin.ProfessionCraftObjective;
import me.neoblade298.neoquests.objectives.builtin.ReachLevelObjective;
import me.neoblade298.neoquests.objectives.builtin.ReachTierObjective;
import me.neoblade298.neoquests.objectives.builtin.SlotAugmentObjective;

public class ObjectiveListenerSkillAPI implements Listener {
	
	@EventHandler
	public void onReceiveStoredItem(ReceiveStoredItemEvent e) {
		Player p = e.getPlayer();

		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.RECEIVE_STORED_ITEM);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetStoredItemObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onLevelUp(PlayerLevelUpEvent e) {
		Player p = e.getPlayerData().getPlayer();

		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.LEVEL_UP);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ReachLevelObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onChangeClass(PlayerClassChangeEvent e) {
		Player p = e.getPlayerData().getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.CHANGE_CLASS);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ReachTierObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onUnlockSkill(PlayerSkillUnlockEvent e) {
		Player p = e.getPlayerData().getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.GET_SKILL);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetSkillObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onUpgradeSkill(PlayerSkillUpgradeEvent e) {
		Player p = e.getPlayerData().getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.GET_SKILL);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((GetSkillObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onProfessionInvOpen(OpenProfessionInvEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.OPEN_PROFESSION_INV);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((OpenProfessionInventoryObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onPlantSeed(ProfessionPlantSeedEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.PLANT_SEED);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((PlantSeedObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onProfessionCraft(ProfessionCraftSuccessEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.PROFESSION_CRAFT);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((ProfessionCraftObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
	
	@EventHandler
	public void onSlotAugment(ProfessionSlotSuccessEvent e) {
		Player p = e.getPlayer();
		ArrayList<ObjectiveInstance> insts = ObjectiveListener.getPlayerInstances(p).get(ObjectiveEvent.SLOT_AUGMENT);
		if (insts != null) {
			for (ObjectiveInstance o : insts) {
				((SlotAugmentObjective) o.getObjective()).checkEvent(e, o);
			}
		}
	}
}
