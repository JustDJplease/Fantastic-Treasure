package me.theblockbender.fantastic.treasure.event;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.manager.TreasureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class InventoryEvent implements Listener {

    private Treasure treasure;

    public InventoryEvent(Treasure treasure) {
        this.treasure = treasure;
    }

    @EventHandler
    public void onPlayerClose(InventoryCloseEvent event) {
        treasure.gui.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if(!treasure.gui.containsKey(uuid))
            return;
        TreasureChest treasureChest = treasure.gui.get(uuid);
        event.setCancelled(true);
        int slot = event.getSlot();
        if(slot >= 0 && slot < 54) {
            switch (slot) {
                case 19:
                    treasureChest.start(player, TreasureType.NORMAL);
                    break;
                case 21:
                    treasureChest.start(player, TreasureType.HAUNTED);
                    break;
                case 23:
                    treasureChest.start(player, TreasureType.SUNKEN);
                    break;
                case 25:
                    treasureChest.start(player, TreasureType.LEGENDARY);
                    break;
                case 49:
                    player.closeInventory();
                    break;
                default:
                    break;
            }
        }
    }
}
