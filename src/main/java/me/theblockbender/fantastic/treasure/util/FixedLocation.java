package me.theblockbender.fantastic.treasure.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class FixedLocation {
    public World _world;
    public int _x;
    public int _y;
    public int _z;

    public FixedLocation(@NotNull Location location) {
        _world = location.getWorld();
        _x = location.getBlockX();
        _y = location.getBlockY();
        _z = location.getBlockZ();
    }

    public boolean isSameAs(FixedLocation fixedLocation) {
        return fixedLocation._world.getName().equalsIgnoreCase(_world.getName()) && fixedLocation._x == _x && fixedLocation._y == _y && fixedLocation._z == _z;
    }

    public Location toLocation() {
        return new Location(_world, _x, _y, _z);
    }
}
