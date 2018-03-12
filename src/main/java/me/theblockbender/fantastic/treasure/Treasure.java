package me.theblockbender.fantastic.treasure;


import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.util.SchematicHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Treasure extends JavaPlugin {

    public Language language;
    public SchematicHandler schematicHandler;
    public HashMap<Location, TreasureChest> treasureChests = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Enabling treasure chest feature.");
        language = new Language(this);
        schematicHandler = new SchematicHandler(this);
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        saveDefaultConfig();
        if (!getConfig().getString("version").equalsIgnoreCase(this.getDescription().getVersion())) {
            getLogger().warning("Your configuration file is outdated.");
        }
        language = new Language(this);
        saveResource("resources" + File.separator + "haunted_treasure.schematic", true);
        saveResource("resources" + File.separator + "legendary_treasure.schematic", true);
        saveResource("resources" + File.separator + "normal_treasure.schematic", true);
        saveResource("resources" + File.separator + "sunken_treasure.schematic", true);
        startTasks();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling treasure chest feature.");
    }

    private void startTasks() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Map.Entry<Location, TreasureChest> entry : treasureChests.entrySet()) {
                entry.getValue().run();
            }
        }, 1L, 1L);
    }
}

