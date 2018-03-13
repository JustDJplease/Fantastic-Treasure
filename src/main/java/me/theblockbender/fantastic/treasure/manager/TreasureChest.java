package me.theblockbender.fantastic.treasure.manager;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.util.FixedLocation;
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
    private FixedLocation _center;

    // Set when a new session is started:
    private boolean _active = false;
    private boolean _isAnimationFinished = false;
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
    public TreasureChest(Treasure treasure, FixedLocation center, MaterialData chestDirection) {
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
        _isAnimationFinished = false;
        _player = player;
        _type = type;
        _timeStarted = System.currentTimeMillis();
        treasure.schematicHandler.paste(_type, _center.toLocation());
        // TODO PLACEHOLDERS!
        _rewards.add(new TreasureReward());
        _rewards.add(new TreasureReward());
        _rewards.add(new TreasureReward());
        _rewards.add(new TreasureReward());
        //TODO create random set of loot based on type.
        //TODO give those rewards to the player.
        spawnInLootChests();
    }

    public void openLoot(Location loot, Player player) {
        TreasureReward reward = _rewards.get(0);
        _player.sendMessage(ChatColor.DARK_GRAY + "- " + reward.getDisplayName());
        _rewards.remove(0);
    }

    /**
     * Reset the treasure chest.
     */
    public void reset() {
        _placeTask.clear();
        _active = false;
        _isAnimationFinished = false;
        Location location = _player.getLocation();
        Location position = _center.toLocation().add(0.5, 1.0, 0.5);
        position.setYaw(location.getYaw());
        position.setPitch(location.getPitch());
        _player.teleport(position);
        _player = null;
        _rewards.clear();
        for (Map.Entry<Location, Boolean> entry : _loot.entrySet()) {
            entry.getKey().getBlock().setType(Material.AIR);
        }
        _loot.clear();
        _type = null;
        _timeStarted = 0L;
        treasure.schematicHandler.paste(TreasureType.NORMAL, _center.toLocation());
        Block block = _center.toLocation().getBlock();
        block.setType(Material.CHEST);
        block.getState().setData(_chestDirection);
        block.getWorld().playSound(_center.toLocation(), Sound.ENTITY_HORSE_SADDLE, 1f, 1f);
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
        if (_player == null || !_player.isOnline()) {
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
        if(_placeTask.isEmpty()){
            if(!_isAnimationFinished){
                _isAnimationFinished = true;
                _player.sendTitle(_type.getTitle(), treasure.language.get("subtitle"),10, 30, 10);
            }
        }
        List<TreasureLootChest> cleanUp = new ArrayList<>();
        for (TreasureLootChest lootChest : _placeTask) {
            if (lootChest.run())
                cleanUp.add(lootChest);
        }
        for (TreasureLootChest lootChest : cleanUp) {
            _placeTask.remove(lootChest);
        }
        cleanUp.clear();
        for (Entity entity : _center._world.getNearbyEntities(_center.toLocation(), 5, 5, 5)) {
            if (entity instanceof Player) {
                if (entity != _player) {
                    if (entity.getLocation().distanceSquared(_center.toLocation()) < 9) {
                        UtilVelocity.velocity(entity, UtilVelocity.calculateVector(_center.toLocation(), entity));
                    }
                }
            } else {
                if (entity.getLocation().distanceSquared(_center.toLocation()) < 9) {
                    UtilVelocity.velocity(entity, UtilVelocity.calculateVector(_center.toLocation(), entity));
                }
            }
        }
        Location location = _player.getLocation();
        Location position = _center.toLocation().add(0.5, 0, 0.5);
        if (position.distanceSquared(location) > 9) {
            position.setYaw(location.getYaw());
            position.setPitch(location.getPitch());
            _player.teleport(position);
        }
    }

    private void spawnInLootChests() {
        _center.toLocation().getBlock().setType(Material.AIR);
        Location chest = _center.toLocation();
        Material material = _type.getMaterial();
        Location middle = _center.toLocation();
        _placeTask.add(new TreasureLootChest(material, chest.add(1, 0, 2).clone(), middle, 1));
        _placeTask.add(new TreasureLootChest(material, chest.add(1, 0, -1).clone(), middle, 25));
        _placeTask.add(new TreasureLootChest(material, chest.add(0, 0, -2).clone(), middle, 50));
        _placeTask.add(new TreasureLootChest(material, chest.add(-1, 0, -1).clone(), middle, 75));
        _placeTask.add(new TreasureLootChest(material, chest.add(-2, 0, 0).clone(), middle, 100));
        _placeTask.add(new TreasureLootChest(material, chest.add(-1, 0, 1).clone(), middle, 125));
        _placeTask.add(new TreasureLootChest(material, chest.add(0, 0, 2).clone(), middle, 150));
        _placeTask.add(new TreasureLootChest(material, chest.add(1, 0, 1).clone(), middle, 175));
        for (TreasureLootChest lootChest : _placeTask) {
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
        return _center.toLocation();
    }

    public boolean isAnimationFinished() {
        return _isAnimationFinished;
    }
}