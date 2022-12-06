package me.neoblade298.neoquests.worldguard;

import java.util.Set;

import org.bukkit.Bukkit;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import me.neoblade298.neoquests.NeoQuests;

public class RequiredTagFlagHandler extends FlagValueChangeHandler<String>{
    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<RequiredTagFlagHandler> {
        @Override
        public RequiredTagFlagHandler create(Session session) {
            // create an instance of a handler for the particular session
            // if you need to pass certain variables based on, for example, the player
            // whose session this is, do it here
            return new RequiredTagFlagHandler(session);
        }
    }
    // construct with your desired flag to track changes
    public RequiredTagFlagHandler(Session session) {
        super(session, NeoQuests.REQ_TAG_FLAG);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        if (entered.isEmpty() && exited.isEmpty()
                && from.getExtent().equals(to.getExtent())) { // sets don't include global regions (we don't need to check those for this)
            return true; // no changes to flags if regions didn't change
        }
        
    	String req = toSet.queryValue(player, NeoQuests.REQ_TAG_FLAG);
        if (req == null) return true;
        if (player.hasPermission("neoquests.bypass")) return true;

        boolean bypass = getSession().getManager().hasBypass(player, (World) to.getExtent());
        boolean allowed = NeoQuests.getPlayerTags(Bukkit.getPlayer(player.getUniqueId())).exists(req, player.getUniqueId());

        return (bypass || allowed) && moveType.isCancellable();
    }

	@Override
	protected boolean onAbsentValue(LocalPlayer arg0, com.sk89q.worldedit.util.Location arg1,
			com.sk89q.worldedit.util.Location arg2, ApplicableRegionSet arg3, String arg4, MoveType arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onInitialValue(LocalPlayer arg0, ApplicableRegionSet arg1, String arg2) {}

	@Override
	protected boolean onSetValue(LocalPlayer arg0, com.sk89q.worldedit.util.Location arg1,
			com.sk89q.worldedit.util.Location arg2, ApplicableRegionSet arg3, String arg4, String arg5, MoveType arg6) {
		return false;
	}
}
