package me.theblockbender.fantastic.treasure;


import me.theblockbender.fantastic.treasure.command.TreasureCommand;
import me.theblockbender.fantastic.treasure.event.InteractEvent;
import me.theblockbender.fantastic.treasure.event.InventoryEvent;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import me.theblockbender.fantastic.treasure.util.FixedLocation;
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
    public HashMap<FixedLocation, TreasureChest> treasureChests = new HashMap<>();
    public HashMap<UUID, TreasureChest> gui = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        getLogger().info("Enabling treasure chest feature.");
        schematicHandler = new SchematicHandler(this);
        treasureGUI = new TreasureGUI(this);
        getCommand("treasure").setExecutor(new TreasureCommand(this));
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InteractEvent(this), this);
        pm.registerEvents(new InventoryEvent(this), this);
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        createConfig();
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
        for (Map.Entry<FixedLocation, TreasureChest> entry : treasureChests.entrySet()) {
            entry.getValue().reset();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    FixedLocation fl = new FixedLocation(chest);
                    treasureChests.put(fl, new TreasureChest(this, fl, data));
                }
            } catch (Exception ignored) {
            }
        }
        strings.clear();
        saveTreasure();
    }

    public void saveTreasure() {
        List<String> strings = new ArrayList<>();
        for (Map.Entry<FixedLocation, TreasureChest> entry : treasureChests.entrySet()) {
            FixedLocation accepted = entry.getKey();
            strings.add(accepted._world.getName() + "|" + accepted._x + "|" + accepted._y + "|" + accepted._z);
        }
        getConfig().set("chests", strings);
        saveConfig();
    }

    private void startTasks() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Map.Entry<FixedLocation, TreasureChest> entry : treasureChests.entrySet()) {
                entry.getValue().run();
            }
        }, 1L, 1L);
    }
}

