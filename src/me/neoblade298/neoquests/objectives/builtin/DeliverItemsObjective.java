package me.neoblade298.neoquests.objectives.builtin;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neoquests.objectives.Objective;
import me.neoblade298.neoquests.objectives.ObjectiveEvent;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.md_5.bungee.api.ChatColor;

public class DeliverItemsObjective extends Objective {
	private int npcid, modeldata, loreNum;
	private String npcname, mythicitem, nbtstring, lorestring;
	private Material material;
	
	public DeliverItemsObjective() {
		super();
	}

	public DeliverItemsObjective(LineConfig cfg) {
		super(ObjectiveEvent.INTERACT_NPC, cfg);
		
		npcid = cfg.getInt("id", -1);
		mythicitem = cfg.getString("mythicitem", null);
		String matString = cfg.getString("material", null);
		material = matString == null ? null : Material.valueOf(matString.toUpperCase());
		modeldata = cfg.getInt("modeldata", -1);
		nbtstring = cfg.getString("nbtstring", null);
		loreNum = cfg.getInt("lore", -1);
		lorestring = cfg.getLine();
	}

	@Override
	public Objective create(LineConfig cfg) {
		return new DeliverItemsObjective(cfg);
	}

	@Override
	public String getKey() {
		return "deliver-items";
	}

	public boolean checkEvent(NPCRightClickEvent e, ObjectiveInstance o) {
		if (e.getNPC().getId() != npcid) return false;
		
		int remaining = o.getObjective().getNeeded() - o.getCount();
		if (remaining <= 0) return false;
		else {
			Inventory inv = o.getPlayer().getInventory();
			boolean found = false;
			for (ItemStack item : inv.getContents()) {
				if (checkItemStack(item)) {
					found = true;
					ItemStack clone = item.clone();
					if (item.getAmount() >= remaining) {
						clone.setAmount(remaining); // If it's more than needed, only take what's needed
					}
					remaining -= clone.getAmount();
					inv.removeItem(clone);
					o.addCount(clone.getAmount());
					if (remaining <= 0) {
						return true;
					}
				}
			}
			return found;
		}
	}

	@Override
	public String getDisplay() {
		if (npcname == null) {
			npcname = CitizensAPI.getNPCRegistry().getById(npcid).getFullName();
		}
		return "Deliver items to NPC " + npcname;
	}
	
	private boolean checkItemStack(ItemStack item) {
		if (item == null) return false;
		
		NBTItem nbti = new NBTItem(item);
		if (mythicitem != null) {
			return mythicitem.equals(nbti.getString("MYTHIC_TYPE"));
		}
		
		if (material != null && item.getType() != material) return false;
		if (nbtstring != null && !nbti.getString("quests").equals(nbtstring)) return false;

		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (modeldata != -1 && meta.getCustomModelData() != modeldata) return false;
			if (loreNum != -1) {
				if (!meta.hasLore()) return false;
				if (meta.getLore().size() <= loreNum) return false;
				String line = ChatColor.stripColor(meta.getLore().get(loreNum));
				if (!line.equals(lorestring));
			}
			return true;
		}
		// If the item has no meta but requires it
		else {
			return modeldata == -1 && loreNum == -1;
		}
	}

}
