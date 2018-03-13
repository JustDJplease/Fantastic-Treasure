package me.theblockbender.fantastic.treasure.util;

import me.theblockbender.fantastic.treasure.Treasure;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TreasureGUI {

    private Treasure treasure;

    public TreasureGUI(Treasure treasure){
        this.treasure = treasure;
    }

    public void openGUI(Player player){
        treasure.gui.add(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, 54, "Treasure Chests");
        inventory.setItem(0, new ItemStack(Material.BARRIER, 1));
        player.openInventory(inventory);
        // TODO set items.
    }
}
