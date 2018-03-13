package me.theblockbender.fantastic.treasure.manager;

import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

public enum TreasureType {
    NORMAL("§f§lRegular Treasure Chest", Material.CHEST, "normal_treasure", "§fRegular Chest"), SUNKEN("§b§lSunken §3§lTreasure Chest",
            Material.TRAPPED_CHEST, "sunken_treasure", "§bSunken §3Chest"), HAUNTED("§4§lHaunted §c§lTreasure Chest",
            Material.CHEST, "haunted_treasure", "§4Haunted §cChest"), LEGENDARY("§6§lLegendary §e§lTreasure Chest", Material.ENDER_CHEST, "legendary_treasure", "§6Legendary §eChest");

    private final String _name;
    private final Material _material;
    private final String _schematic;
    private final String _title;

    TreasureType(String name, Material material, String schematic, String title) {
        _name = name;
        _material = material;
        _schematic = schematic;
        _title = title;
    }

    @Contract(pure = true)
    public String getSchematic() {
        return _schematic;
    }

    @Contract(pure = true)
    public String getName() {
        return _name;
    }

    @Contract(pure = true)
    public Material getMaterial() {
        return _material;
    }

    public String getTitle() {
        return _title;
    }
}
