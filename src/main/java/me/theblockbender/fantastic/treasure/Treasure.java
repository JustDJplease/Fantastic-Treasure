package me.theblockbender.fantastic.treasure;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Treasure extends JavaPlugin {

    public Language language;

    @Override
    public void onEnable() {
        getLogger().info("Enabling treasure chest feature.");
        language = new Language(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling treasure chest feature.");
    }
}

