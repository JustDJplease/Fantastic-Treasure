package me.theblockbender.fantastic.treasure;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class Language {

    private HashMap<String, String> messages = new HashMap<>();

    Language(Treasure treasure) {
        load(treasure.getConfig().getConfigurationSection("language"));
    }

    public String get(String key) {
        if (messages.containsKey(key))
            return messages.get(key);
        else
            return ChatColor.RESET + "Message Unspecified: " + key;
    }

    public String getWithPrefix(String key) {
        if (messages.containsKey(key))
            return messages.get("prefix") + messages.get(key);
        else
            return ChatColor.RESET + "Message Unspecified: " + key;
    }

    private void load(ConfigurationSection messageSection) {
        for (String key : messageSection.getKeys(false)) {
            messages.put(key, ChatColor.translateAlternateColorCodes('&', messageSection.getString(key)));
        }

    }
}