package me.theblockbender.fantastic.treasure.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class UtilVelocity {

    public static Vector calculateVector(@NotNull Location from, @NotNull Entity to) {
        return to.getLocation().toVector().subtract(from.toVector()).setY(0).normalize();
    }

    public static void velocity(Entity entity, @NotNull Vector vector) {
        {
            if ((Double.isNaN(vector.getX())) || (Double.isNaN(vector.getY())) || (Double.isNaN(vector.getZ())) || (vector.length() == 0.0D)) {
                return;
            }
            vector.setY(0.8D);
            vector.normalize();
            vector.multiply(1.6D);
            vector.setY(vector.getY() + 0.0D);
            if (vector.getY() > 10.0D) {
                vector.setY(10.0D);
            }
            entity.setFallDistance(0.0F);
            entity.setVelocity(vector);
        }
    }
}
