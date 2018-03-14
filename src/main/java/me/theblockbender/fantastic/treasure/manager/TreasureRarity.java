package me.theblockbender.fantastic.treasure.manager;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;

public enum TreasureRarity {
    COMMON("§7Common", Color.SILVER, ChatColor.WHITE), RARE("§2Rare",Color.GREEN, ChatColor.GREEN), EPIC("§5Epic", Color.PURPLE, ChatColor.LIGHT_PURPLE), LEGENDARY("§6Legendary",Color.ORANGE, ChatColor.YELLOW), MYTHICAL("§4Mythical", Color.RED, ChatColor.RED);

    private final String _name;
    public final Color _color;
    private final ChatColor _chatColor;

    TreasureRarity(String name, Color fireworkBase, ChatColor color){
        _name = name;
        _color = fireworkBase;
        _chatColor = color;
    }

    public String getDisplayName(){
        return _name;
    }

    public  ChatColor getNameColor(){
        return _chatColor;
    }
}
