package me.theblockbender.fantastic.treasure;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Treasure extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable");
    }
}

