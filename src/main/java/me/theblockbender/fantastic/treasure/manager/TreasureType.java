package me.theblockbender.fantastic.treasure.manager;

import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

public enum TreasureType {
    NORMAL("§f§lRegular Treasure Chest", Material.CHEST, "normal_treasure"), SUNKEN("§f§lSunken Treasure Chest",
            Material.TRAPPED_CHEST, "sunken_treasure"), HAUNTED("§f§lRegular Treasure Chest",
            Material.CHEST, "haunted_treasure"), LEGENDARY("§f§lRegular Treasure Chest", Material.ENDER_CHEST, "legendary_treasure");

    private final String _name;
    private final Material _material;
    private final String _schematic;

    TreasureType(String name, Material material, String schematic) {
        _name = name;
        _material = material;
        _schematic = schematic;
    }

    @Contract(pure = true)
    public String getSchematic(){
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
}
