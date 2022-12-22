package me.neoblade298.neoquests.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;

import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import me.neoblade298.neoquests.objectives.builtin.CreateTownObjective;
import me.neoblade298.neoquests.objectives.builtin.JoinTownObjective;

public class ObjectiveListenerTowny implements Listener {

	
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

	private static HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> getPlayerInstances(Player p) {
		HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>> pmap = ObjectiveListener.objs.getOrDefault(p, new HashMap<ObjectiveEvent, ArrayList<ObjectiveInstance>>());
		ObjectiveListener.objs.putIfAbsent(p, pmap);
		return pmap;
	}
}
