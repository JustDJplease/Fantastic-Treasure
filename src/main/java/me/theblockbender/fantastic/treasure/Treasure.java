package me.theblockbender.fantastic.treasure;


import me.theblockbender.fantastic.treasure.command.TreasureCommand;
import me.theblockbender.fantastic.treasure.event.InteractEvent;
import me.theblockbender.fantastic.treasure.event.InventoryEvent;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.util.SchematicHandler;
import me.theblockbender.fantastic.treasure.util.TreasureGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class Treasure extends JavaPlugin {

    public Language language;
    public SchematicHandler schematicHandler;
    public TreasureGUI treasureGUI;
    public HashMap<Location, TreasureChest> treasureChests = new HashMap<>();
    public HashMap<UUID, TreasureChest> gui = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        getLogger().info("Enabling treasure chest feature.");
        language = new Language(this);
        schematicHandler = new SchematicHandler(this);
        treasureGUI = new TreasureGUI(this);
        getCommand("treasure").setExecutor(new TreasureCommand(this));
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InteractEvent(this), this);
        pm.registerEvents(new InventoryEvent(this), this);
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        saveDefaultConfig();
        if (!getConfig().getString("version").equalsIgnoreCase(this.getDescription().getVersion())) {
            getLogger().warning("Your configuration file is outdated.");
            getLogger().warning("This might lead to problems with the plugin.");
        }
        loadTreasure();
        language = new Language(this);
        saveResource("haunted_treasure.schematic", true);
        saveResource("legendary_treasure.schematic", true);
        saveResource("normal_treasure.schematic", true);
        saveResource("sunken_treasure.schematic", true);
        startTasks();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling treasure chest feature.");
    }

    public void loadTreasure() {
        List<String> strings = getConfig().getStringList("chests");
        if (strings == null || strings.isEmpty())
            return;
        for (String string : strings) {
            Location chest;
            String[] parts = string.split("\\|");
            try {
                chest = new Location(Bukkit.getWorld(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                Block block = chest.getBlock();
                if (block.getType() == Material.CHEST) {
                    MaterialData data = block.getState().getData();
                    treasureChests.put(chest, new TreasureChest(this, chest, data));
                }
            } catch (Exception ignored) {
            }
        }
        strings.clear();
        saveTreasure();
    }

    public void saveTreasure() {
        List<String> strings = new ArrayList<>();
        for (Map.Entry<Location, TreasureChest> entry : treasureChests.entrySet()) {
            Location accepted = entry.getKey();
            strings.add(accepted.getWorld().getName() + "|" + accepted.getBlockX() + "|" + accepted.getBlockY() + "|" + accepted.getBlockZ());
        }
        getConfig().set("chests", strings);
        saveConfig();
    }

    private void startTasks() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Map.Entry<Location, TreasureChest> entry : treasureChests.entrySet()) {
                entry.getValue().run();
            }
        }, 1L, 1L);
    }
}

