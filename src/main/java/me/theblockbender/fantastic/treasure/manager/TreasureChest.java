package me.theblockbender.fantastic.treasure.manager;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.util.UtilVelocity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreasureChest {
    // Set when a new instance is created:
    private Treasure treasure;
    private MaterialData _chestDirection;
    private Location _center;

    // Set when a new session is started:
    private boolean _active = false;
    private Player _player;
    private HashMap<Location, Boolean> _loot = new HashMap<>();
    private TreasureType _type;
    private long _timeStarted;
    private List<TreasureReward> _rewards = new ArrayList<>();

    // Blocks to set in animation:
    private List<TreasureLootChest> _placeTask = new ArrayList<>();

    /**
     * Creates a new instance of TreasureChest.
     * Should be called close to onEnable();
     *
     * @param center         The central chest block
     * @param chestDirection MaterialData for the original chest. (Includes direction)
     */
    public TreasureChest(Treasure treasure, Location center, MaterialData chestDirection) {
        this.treasure = treasure;
        _center = center;
        _chestDirection = chestDirection;
    }

    /**
     * Start a new treasure chest session.
     * Should be called after a player opens a TreasureChest.
     */
    public void start(Player player, TreasureType type) {
        _active = true;
        _player = player;
        _type = type;
        _timeStarted = System.currentTimeMillis();
        treasure.schematicHandler.paste(_type,_center);
        //TODO create random set of loot based on type.
        //TODO give those rewards to the player.
        spawnInLootChests();
    }

    /**
     * Reset the treasure chest.
     */
    public void reset() {
        _placeTask.clear();
        _active = false;
        _player = null;
        _rewards.clear();
        for(Map.Entry<Location, Boolean> entry : _loot.entrySet()){
            entry.getKey().getBlock().setType(Material.AIR);
        }
        _loot.clear();
        _type = null;
        _timeStarted = 0L;
        treasure.schematicHandler.paste(TreasureType.NORMAL,_center);
        Block block = _center.getBlock();
        block.setType(Material.CHEST);
        block.getState().setData(_chestDirection);
        block.getWorld().playSound(_center, Sound.ENTITY_HORSE_SADDLE, 1f, 1f);
    }

    /**
     * Should be run every tick, as it will place blocks and manage time-outs.
     */
    public void run() {
        if (!_active)
            return;
        if (_rewards.isEmpty()) {
            reset();
            return;
        }
        if (_player == null) {
            // player quit.
            reset();
            return;
        }
        if (_timeStarted + 300000 <= System.currentTimeMillis()) {
            // session timed out.
            _player.sendMessage(treasure.language.getWithPrefix("timeout"));
            for (TreasureReward reward : _rewards) {
                _player.sendMessage(ChatColor.DARK_GRAY + "- " + reward.getDisplayName());
            }
            reset();
            return;
        }
        for (TreasureLootChest lootChest : _placeTask) {
            lootChest.run();
        }
        for (Entity entity : _center.getWorld().getNearbyEntities(_center, 5, 5, 5)) {
            if (entity instanceof Player){
                if(entity != _player) {
                    if (entity.getLocation().distanceSquared(_center) < 9) {
                        UtilVelocity.velocity(entity, UtilVelocity.calculateVector(_center, entity));
                    }
                }
            }else{
                if (entity.getLocation().distanceSquared(_center) < 9) {
                    UtilVelocity.velocity(entity, UtilVelocity.calculateVector(_center, entity));
                }
            }
        }
        Location location = _player.getLocation();
        if (_center.distanceSquared(location) > 9) {
            _center.setYaw(location.getYaw());
            _center.setPitch(location.getPitch());
            _player.teleport(_center);
        }
    }

    private void spawnInLootChests() {
        _center.getBlock().setType(Material.AIR);
        Location chest = _center.clone();
        Material material = _type.getMaterial();
        _placeTask.add(new TreasureLootChest(material, chest.add(1, 0, 2).clone(), _center, 1));
        _placeTask.add(new TreasureLootChest(material, chest.add(1, 0, -1).clone(), _center, 3));
        _placeTask.add(new TreasureLootChest(material, chest.add(0, 0, -2).clone(), _center, 5));
        _placeTask.add(new TreasureLootChest(material, chest.add(-1, 0, -1).clone(), _center, 7));
        _placeTask.add(new TreasureLootChest(material, chest.add(-2, 0, 0).clone(), _center, 9));
        _placeTask.add(new TreasureLootChest(material, chest.add(-1, 0, 1).clone(), _center, 11));
        _placeTask.add(new TreasureLootChest(material, chest.add(0, 0, 2).clone(), _center, 13));
        _placeTask.add(new TreasureLootChest(material, chest.add(1, 0, 1).clone(), _center, 15));
        for(TreasureLootChest lootChest : _placeTask){
            _loot.put(lootChest.getLocation(), false);
        }
    }

    public boolean isLootChestLootable(Location location) {
        return _loot.containsKey(location) && !_loot.get(location);
    }

    public boolean isActive() {
        return _active;
    }

    public boolean isOwner(Player player) {
        return _player == player;
    }

    public @NotNull Location getCenter() {
        return _center;
    }
}