package me.theblockbender.fantastic.treasure.util;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.manager.TreasureType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TreasureGUI {

    private Treasure treasure;

    public TreasureGUI(Treasure treasure) {
        this.treasure = treasure;
    }

    public void openGUI(Player player, TreasureChest treasureChest) {
        treasure.gui.put(player.getUniqueId(), treasureChest);
        Inventory inventory = Bukkit.createInventory(null, 54, "Treasure Chests");
        inventory.setItem(49, new UtilItem(Material.BARRIER, 1).setName(ChatColor.RED + "" + ChatColor.BOLD + "Exit").toItemStack());
        inventory.setItem(19, new UtilItem(Material.SKULL_ITEM, 1).setName(TreasureType.NORMAL.getName()).setSkullTexture("http://textures.minecraft.net/texture/58bc8fa716cadd004b828cb27cc0f6f6ade3be41511688ca9eceffd1647fb9").toItemStack());
        inventory.setItem(21, new UtilItem(Material.SKULL_ITEM, 1).setName(TreasureType.HAUNTED.getName()).setSkullTexture("http://textures.minecraft.net/texture/68d2183640218ab330ac56d2aab7e29a9790a545f691619e38578ea4a69ae0b6").toItemStack());
        inventory.setItem(23, new UtilItem(Material.SKULL_ITEM, 1).setName(TreasureType.SUNKEN.getName()).setSkullTexture("http://textures.minecraft.net/texture/776ad9ff7d606f31adb624b1496f67eb6d269944e147052e57e48741b1482a4").toItemStack());
        inventory.setItem(25, new UtilItem(Material.SKULL_ITEM, 1).setName(TreasureType.LEGENDARY.getName()).setSkullTexture("http://textures.minecraft.net/texture/945f47feb4d75cb333914bfdb999a489c9d0e320d548f310419ad738d1e24b9").toItemStack());
        player.openInventory(inventory);
    }
}
