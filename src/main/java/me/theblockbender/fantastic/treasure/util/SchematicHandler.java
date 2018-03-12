package me.theblockbender.fantastic.treasure.util;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.manager.TreasureType;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class SchematicHandler {
    private Treasure treasure;

    public SchematicHandler(Treasure treasure) {
        this.treasure = treasure;
    }

    public void paste(TreasureType type, Location pasteFrom) {
        try {
            ClipboardFormat.SCHEMATIC
                    .load(new File(treasure.getDataFolder() + File.separator + "resources" + File.separator
                            + type.getSchematic() + ".schematic"))
                    .paste(FaweAPI.getWorld(pasteFrom.getWorld().getName()),
                            new Vector(pasteFrom.getBlockX(), pasteFrom.getBlockY(), pasteFrom.getBlockZ()),
                            false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
