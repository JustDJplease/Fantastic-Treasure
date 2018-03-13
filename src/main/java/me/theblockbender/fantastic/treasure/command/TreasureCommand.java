package me.theblockbender.fantastic.treasure.command;

import me.theblockbender.fantastic.treasure.Treasure;
import me.theblockbender.fantastic.treasure.manager.TreasureChest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TreasureCommand implements CommandExecutor {

    private Treasure treasure;

    public TreasureCommand(Treasure treasure) {
        this.treasure = treasure;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("treasure.admin")) {
            sender.sendMessage(treasure.language.getWithPrefix("no-permission"));
            return true;
        }
        if (args.length != 1) {
            help(sender, label);
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            treasure.reloadConfig();
            treasure.treasureChests.clear();
            treasure.loadTreasure();
            sender.sendMessage(treasure.language.getWithPrefix("reload"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(treasure.language.getWithPrefix("no-player"));
            return true;
        }
        Player player = (Player) sender;
        Block target = player.getTargetBlock(null, 10);
        Location location = target.getLocation();
        if (args[0].equalsIgnoreCase("create")) {
            if (target.getType() != Material.CHEST) {
                player.sendMessage(treasure.language.getWithPrefix("no-chest"));
                return true;
            }
            if(treasure.treasureChests.containsKey(location)){
                player.sendMessage(treasure.language.getWithPrefix("is-treasure"));
                return true;
            }
            treasure.treasureChests.put(location, new TreasureChest(treasure, location, target.getState().getData()));
            treasure.saveTreasure();
            player.sendMessage(treasure.language.getWithPrefix("created"));
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (target.getType() != Material.CHEST) {
                player.sendMessage(treasure.language.getWithPrefix("no-chest"));
                return true;
            }
            if(!treasure.treasureChests.containsKey(location)){
                player.sendMessage(treasure.language.getWithPrefix("no-treasure"));
                return true;
            }
            TreasureChest treasureChest = treasure.treasureChests.get(location);
            treasureChest.reset();
            treasure.treasureChests.remove(location);
            treasure.saveTreasure();
            player.sendMessage(treasure.language.getWithPrefix("removed"));
            return true;
        }
        help(sender, label);
        return true;
    }

    private void help(@NotNull CommandSender sender, String label) {
        sender.sendMessage(treasure.language.getWithPrefix("help"));
        sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " " + ChatColor.AQUA + "create " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + treasure.language.get("help-create"));
        sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " " + ChatColor.AQUA + "remove " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + treasure.language.get("help-remove"));
        sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " " + ChatColor.AQUA + "reload " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + treasure.language.get("help-reload"));
    }
}
