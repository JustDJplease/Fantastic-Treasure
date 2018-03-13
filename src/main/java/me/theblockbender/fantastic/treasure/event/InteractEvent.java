package me.theblockbender.fantastic.treasure.event;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.util.UtilVelocity;
import org.bukkit.Location;
import org.bukkit.Material;
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
        if (clicked == null || clicked.getType() != Material.CHEST)
            return;
        Location location = clicked.getLocation();
        if (treasure.treasureChests.containsKey(location)) {
            event.setCancelled(true);
            TreasureChest treasureChest = treasure.treasureChests.get(location);
            if (treasureChest.isActive()) {
                player.sendMessage(treasure.language.getWithPrefix("already-active"));
                return;
            }
            treasure.treasureGUI.openGUI(player, treasureChest);
            return;
        }
        TreasureChest session = null;
        for (Map.Entry<Location, TreasureChest> entry : treasure.treasureChests.entrySet()) {
            TreasureChest treasureChest = entry.getValue();
            if (treasureChest.isLootChestLootable(location)) {
                session = treasureChest;
                break;
            }
        }
        if (session == null)
            return;
        event.setCancelled(true);
        if (session.isOwner(player)) {
            //TODO open loot.
            return;
        }
        player.sendMessage(treasure.language.getWithPrefix("not-your-session"));
        UtilVelocity.velocity(player, UtilVelocity.calculateVector(session.getCenter(), player));
    }
}
