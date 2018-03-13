package me.theblockbender.fantastic.treasure.event;

import me.theblockbender.fantastic.treasure.Treasure;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

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
        if(!treasure.gui.contains(event.getWhoClicked().getUniqueId()))
            return;
        event.setCancelled(true);
        //TODO manage gui.
    }
}
