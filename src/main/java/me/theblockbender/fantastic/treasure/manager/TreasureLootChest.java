package me.theblockbender.fantastic.treasure.manager;

import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TreasureLootChest {
    private Material _material;
    private int _delay;
    private int _pitch;
    private Location _loc;
    private byte _direction;

    TreasureLootChest(Material material, @NotNull Location location, @NotNull Location center, Integer delayTicks) {
        _material = material;
        _delay = delayTicks;
        _pitch = delayTicks;
        _loc = location;
        int relX = (int) (center.getX() - location.getX());
        int relZ = (int) (center.getZ() - location.getZ());
        if (Math.abs(relX) > Math.abs(relZ)) {
            if (relX > 0)
                _direction = (byte) 5;
            else
                _direction = (byte) 4;
        } else {
            if (relZ > 0)
                _direction = (byte) 3;
            else
                _direction = (byte) 2;
        }
    }

    @SuppressWarnings("deprecation")
    public void run() {
        if (_delay < 1) {
            Block block = _loc.getBlock();
            block.setType(_material);
            block.setData(_direction);
            _loc.getWorld().playSound(_loc, Sound.BLOCK_WOOD_PLACE, 1f, 1f);
            playSpawnEffect();
            playNote();
        } else {
            if (_delay == 1)
                playPrepareEffect();
            _delay--;
        }
    }

    private void playNote() {
        for (Player player : _loc.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(_loc) < 144) {
                player.playNote(_loc, Instrument.GUITAR, getNote(_pitch));
            }
        }
    }

    @org.jetbrains.annotations.NotNull
    private Note getNote(int height) {
        switch (height) {
            case 1:
                return Note.natural(0, Tone.G);
            case 3:
                return Note.natural(0, Tone.A);
            case 5:
                return Note.natural(0, Tone.B);
            case 7:
                return Note.natural(0, Tone.C);
            case 9:
                return Note.natural(0, Tone.D);
            case 11:
                return Note.natural(0, Tone.E);
            case 13:
                return Note.natural(0, Tone.F);
            case 15:
                return Note.natural(1, Tone.G);
        }
        return Note.natural(0, Tone.G);
    }

    private void playPrepareEffect() {
        _loc.getWorld().spawnParticle(Particle.CLOUD, _loc.getBlockX() + 0.5, _loc.getBlockY() + 1,
                _loc.getBlockZ() + 0.5, 10, 0.1, 0.5, 0.1, 0);
    }

    private void playSpawnEffect() {
        _loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, _loc.getBlockX() + 0.5, _loc.getBlockY() + 0.5,
                _loc.getBlockZ() + 0.5, 20, 0.7, 0.5, 0.7, 0, _loc.getBlock().getState().getData());
    }

    public Location getLocation() {
        return _loc;
    }
}
