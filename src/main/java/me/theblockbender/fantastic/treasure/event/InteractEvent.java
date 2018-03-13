package me.theblockbender.fantastic.treasure.event;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.util.FixedLocation;
import me.theblockbender.fantastic.treasure.util.UtilVelocity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class InteractEvent implements Listener {

    private Treasure treasure;

    public InteractEvent(Treasure treasure) {
        this.treasure = treasure;
    }

    @EventHandler
    public void onPlayerClickChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clicked = event.getClickedBlock();
        if (clicked == null || (clicked.getType() != Material.CHEST && clicked.getType() != Material.TRAPPED_CHEST && clicked.getType() != Material.ENDER_CHEST))
            return;
        Location location = clicked.getLocation();
        TreasureChest treasureChest = null;
        for(Map.Entry<FixedLocation, TreasureChest> entry : treasure.treasureChests.entrySet()){
            if(entry.getKey().isSameAs(new FixedLocation(location))){
                treasureChest = entry.getValue();
                break;
            }
        }
        for(Map.Entry<FixedLocation, TreasureChest> entry : treasure.treasureChests.entrySet()){
            if(entry.getValue().isOwner(player)){
                event.setCancelled(true);
            }
        }
        if (treasureChest != null) {
            event.setCancelled(true);
            if (treasureChest.isActive()) {
                player.sendMessage(treasure.language.getWithPrefix("already-active"));
                return;
            }
            treasure.treasureGUI.openGUI(player, treasureChest);
            return;
        }
        TreasureChest session = null;
        for (Map.Entry<FixedLocation, TreasureChest> entry : treasure.treasureChests.entrySet()) {
            TreasureChest tc = entry.getValue();
            if (tc.isLootChestLootable(location)) {
                session = tc;
                break;
            }
        }
        if (session == null)
            return;
        event.setCancelled(true);
        if (session.isOwner(player)) {
            if(session.isAnimationFinished()) {
                session.openLoot(location);
            }else{
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1f, 1f);
            }
            return;
        }
        player.sendMessage(treasure.language.getWithPrefix("not-your-session"));
        UtilVelocity.velocity(player, UtilVelocity.calculateVector(session.getCenter(), player));
    }
}
