package me.theblockbender.fantastic.treasure.manager;

import org.bukkit.inventory.ItemStack;

public class TreasureReward {

    private String _name;
    private TreasureRarity _rarity;
    private ItemStack _displayItem;

    public TreasureReward(String name, TreasureRarity rarity, ItemStack display) {
        _name = name;
        _rarity = rarity;
        _displayItem = display;
    }

    public String getDisplayName() {
        return _rarity.getNameColor() + _name;
    }

    public TreasureRarity getRarity() {
        return _rarity;
    }

    public ItemStack getDisplayItem() {
        return _displayItem;
    }
    //TODO rarity
    //TODO effect (cmd, perm, item, etc.)
    //TODO name
}
