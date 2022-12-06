package me.neoblade298.neoquests.listeners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.EndPoint;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.PathwayEditor;
import me.neoblade298.neoquests.navigation.Point;
import me.neoblade298.neoquests.navigation.PointType;

public class NavigationListener implements Listener {
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (e.getHand() != null && e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
		if (e.getItem() == null || !e.getItem().getType().equals(Material.STICK)) return;
		
		e.setCancelled(true);
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			Location loc = e.getClickedBlock().getLocation().add(0.5, 0, 0.5);
			if (p.isSneaking()) {
				deletePoint(p, editor, loc);
			}
			else {
				createOrTogglePoint(p, editor, loc);
			}
		}
		else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !p.isSneaking()) {
			connectPoint(p, editor, e.getClickedBlock().getLocation().add(0.5, 0, 0.5));
		}
		else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.isSneaking()) {
				undoConnection(p, editor);
			}
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (!e.getItemDrop().getItemStack().getType().equals(Material.STICK)) return;

		e.setCancelled(true);
		editEndpoint(p, editor, e.getPlayer().getTargetBlock(null, 5).getLocation().add(0.5, 0, 0.5), e);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		PathwayEditor editor = NavigationManager.getEditor(p);
		if (editor == null) return;
		if (!editor.editingEndpoint()) return;
		
		EndPoint ep = editor.getEditingEndpoint();
		if (ep.getKey() == null) {
			ep.setKey(e.getMessage().split(" ")[0]);
			Util.msg(p, "Type a display name for the endpoint!");
			e.setCancelled(true);
		}
		else if (ep.getDisplay() == null) {
			ep.setDisplay(Util.translateColors(e.getMessage()));
			NavigationManager.addEndpoint(ep, editor.getEditingPoint(), true);
			Util.msg(p, "Endpoint &6" + e.getMessage() + " &7successfully created!");
			editor.stopEditingEndpoint();
			e.setCancelled(true);
		}
	}
	
	private void createOrTogglePoint(Player p, PathwayEditor editor, Location loc) {
		Point point = NavigationManager.getOrCreatePoint(loc);
		if (point != null) {
			PointType type = point.toggleType();
			Util.msg(p, "Successfully toggled point to be type &6" + type + "&7!");
		}
		else {
			Util.msg(p, "Successfully created point!");
		}
	}
	
	private void deletePoint(Player p, PathwayEditor editor, Location loc) {
		editor.deletePoint(loc);
	}
	
	private void connectPoint(Player p, PathwayEditor editor, Location loc) {
		Point point = NavigationManager.getPoint(loc);
		if (point != null) {
			editor.connectPoints(point);
		}
	}
	
	private void undoConnection(Player p, PathwayEditor editor) {
		editor.undoConnection();
	}
	
	private void editEndpoint(Player p, PathwayEditor editor, Location loc, PlayerDropItemEvent e) {
		Point point = NavigationManager.getPoint(loc);
		if (point != null) {
			if (point.isEndpoint()) {
				EndPoint ep = point.getEndpoint();
				// Only able to remove endpoints if existing path is only connector
				if (ep.getStartPoints().size() != 0 || ep.getDestinations().size() != 0) {
					Util.msg(p, "&cCannot remove §6" + ep.getKey() + " §cas it is used in paths:");
					for (EndPoint start : ep.getStartPoints().keySet()) {
						Util.msg(p, "&7- &6from " + start.getKey());
					}
					for (EndPoint destination : ep.getDestinations().keySet()) {
						Util.msg(p, "&7- &6to " + destination.getKey());
					}
					return;
				}
				NavigationManager.removeEndpoint(ep.getKey());
				Util.msg(p, "Successfully removed endpoint!");
			}
			else {
				editor.editEndpoint(point, new EndPoint());
				Util.msg(p, "Type a key for the endpoint (No spaces)!");
			}
		}
	}
}
